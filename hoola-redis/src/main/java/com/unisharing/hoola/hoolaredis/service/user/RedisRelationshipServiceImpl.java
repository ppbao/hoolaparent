package com.unisharing.hoola.hoolaredis.service.user;


import com.unisharing.hoola.hoolacommon.index.IIndexBuilder;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolacommon.vo.SystemRmdUserForm;
import com.unisharing.hoola.hoolacommon.vo.TipsShowUser;
import com.unisharing.hoola.hoolaredis.key.FriendShipKeyManager;
import com.unisharing.hoola.hoolaredis.key.HotContentKeyManager;
import com.unisharing.hoola.hoolaredis.key.UserKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.util.StringUtils;

import java.util.*;

public class RedisRelationshipServiceImpl extends BaseRedisService implements
		IRedisRelationshipService{

	Log logger = LogFactory.getLog(RedisRelationshipServiceImpl.class);

	IRedisUserService redisUserService;
	IIndexBuilder indexBuilder;
	
	private final HashMapper<OpenFriend, String, String> openUserMapper = new DecoratingStringHashMapper<OpenFriend>(
			new JacksonHashMapper<OpenFriend>(OpenFriend.class));
	
	
	
	@Override
	public OpenFriend getOpenFriend(String key) {
		return openUserMapper.fromHash(getUserMap(key));
		
	}
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getUserMap(String key) {
		return new DefaultRedisMap<String, String>(key, super.openUserTemplate);
	}
	@SuppressWarnings({ "unchecked", "unused" })
	private RedisMap<String, String> getBindInfoMap(String key) {
		return new DefaultRedisMap<String, String>(key, openUserTemplate);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertOpenFriend(long uid,OpenFriend openFriend) {
		String key = null;
		if (openFriend != null){
			//手机
			if (openFriend.getOpenType() == BindModel.OpenType.MOBILE.getMark()){
				key = FriendShipKeyManager.getMobilesUserInfoKey(uid, openFriend.getOpenUserId());
			}
			//新浪
			if(openFriend.getOpenType() == BindModel.OpenType.SINA.getMark()){
				key = FriendShipKeyManager.getSinaUserInfoKey(openFriend.getOpenUserId());
			}
			//腾讯
			if(openFriend.getOpenType() == BindModel.OpenType.TENCENT.getMark()){
				key = FriendShipKeyManager.getTencentUserInfoKey(openFriend.getOpenUserId());
			}
		}
		if(!openUserTemplate.hasKey(key)){
			openFriend.setUid(uid);
			BoundHashOperations<String, String, Object> hopts = super.openUserTemplate.boundHashOps(key);
			hopts.putAll(openUserMapper.toHash(openFriend));
			IndexOpenUserModel indexOpenUser = new IndexOpenUserModel();
			indexOpenUser.setId(openFriend.getOpenUserId()+"|"+openFriend.getUid());
			indexOpenUser.setOpenNickName(openFriend.getOpenNickName());
			indexOpenUser.setOpenType(openFriend.getOpenType());
			indexOpenUser.setOpenUserId(openFriend.getOpenUserId());
			indexOpenUser.setUid(openFriend.getUid());
			indexBuilder.buildOpenUserIndex(indexOpenUser);
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void follow(long srcUid, long targetUid) {
		// 更新关注列表,通过Sort Set存储，以便后期进行交集 
		super.userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(srcUid)).add(targetUid+"", System.currentTimeMillis());
		// 更新粉丝列表,通过Sort Set存储，以便后期进行交集 
		super.userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getNewFansKey(targetUid)).add(
				srcUid + "",System.currentTimeMillis());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void unfollow(long srcUid, long targetUid) {
		// 更新关注列表
		super.userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getFollowKey(srcUid)).remove(targetUid+"");
		//从老粉丝列表删除粉丝数据
		super.userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getFansKey(targetUid)).remove(srcUid + "");
		//从新粉丝列表中删除粉丝数据
		super.userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getNewFansKey(targetUid)).remove(srcUid + "");

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadFans(long uid, int page, int pageSize, long curUid) {
		int start = (page - 1) * pageSize;
		int end = 0;
		if (pageSize == 0){
			end = -1;
		}else{
			end = page * pageSize - 1;
		}
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
				userRelationTemplate.getConnectionFactory().getConnection());
		if (connection != null){
			//合并
			int[] weights = {1,1};
			connection.zUnionStore(FriendShipKeyManager.getFansKey(uid),
					Aggregate.MAX, weights, FriendShipKeyManager.getFansKey(uid),
					FriendShipKeyManager.getNewFansKey(uid));
			connection.close();
			if (uid == curUid){
				userRelationTemplate.delete(FriendShipKeyManager.getNewFansKey(uid));
			}
		}
		Set fanSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFansKey(uid)).reverseRange(start, end);
		List<SimpleUserForm> fans = new ArrayList<SimpleUserForm>();
		Iterator it = fanSet.iterator();
		while (it.hasNext()) {
			String obj = (String) it.next();
			try {
				long value = Long.parseLong(obj);
				SimpleUserForm fan = redisUserService.getUser(value).asSimpleUserForm();
				if (fan != null) {
					fans.add(fan);
				}
			} catch (Exception e) {
				logger.error("loadFans:" + e.getMessage());
			}
		}
		return fans;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set loadFansUid(long uid, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		int end = 0;
		if (pageSize == 0) {
			end = -1;
		} else {
			end = page * pageSize - 1;
		}
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
				userRelationTemplate.getConnectionFactory().getConnection());
		int[] weights = {1,1};
		if (connection != null){
			connection.zUnionStore(FriendShipKeyManager.getFansKey(uid),
					Aggregate.MAX, weights, FriendShipKeyManager.getFansKey(uid),
					FriendShipKeyManager.getNewFansKey(uid));
			connection.close();
		}
		Set fanSet = userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getFansKey(uid)).reverseRange(start, end);
		return fanSet;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadFollows(long uid,int page ,int pageSize) {
		int start = (page - 1) * pageSize;
		int end = page * pageSize - 1;
		Set followSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid)).reverseRange(start, end);
		List<SimpleUserForm> follows = new ArrayList<SimpleUserForm>();
		Iterator it = followSet.iterator();
		while (it.hasNext()) {
			String obj = (String) it.next();
			try{
				long value = Long.parseLong(obj);
				UserModel model = redisUserService.getUser(value);
				if (model == null || model.getUid()==0){
					userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid)).remove(String.valueOf(value));
				}else{
					follows.add(model.asSimpleUserForm());
				}
			}catch (Exception e) {
				logger.error("loadFollows:"+e.getMessage());
			}
		}
		return follows;
	}



	@SuppressWarnings("unchecked")
	@Override
	public int countFans(long uid) {
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
				userRelationTemplate.getConnectionFactory().getConnection());
		if (connection != null){
			//合并
			int[] weights = {1,1};
			connection.zUnionStore(FriendShipKeyManager.getFansKey(uid),
					Aggregate.MAX, weights, FriendShipKeyManager.getFansKey(uid),
					FriendShipKeyManager.getNewFansKey(uid));
			connection.close();
		}
		return userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFansKey(uid)).size().intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countNewFans(long uid){
		return userRelationTemplate.boundZSetOps(FriendShipKeyManager.getNewFansKey(uid)).size().intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countFollows(long uid) {
		return userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid)).size().intValue();
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean isFollowed(long srcUid, long targetUid) {
		Long rank = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(srcUid)).rank(String.valueOf(targetUid));
		return rank != null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadFriend(long uid,int page,int pageSize) {
		if (page == 1){
			userRelationTemplate.delete(FriendShipKeyManager.getUserFriendKey(uid));
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid))
					.intersectAndStore(FriendShipKeyManager.getFansKey(uid),
							FriendShipKeyManager.getUserFriendKey(uid));
		}
		
		int start = (page - 1) * pageSize;
		
		int end = 0;
		if (pageSize > 0)
			end = page * pageSize - 1;
		else
			end = -1;
		Set friendSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFriendKey(uid)).reverseRange(start, end);
		List<SimpleUserForm> friends = new ArrayList<SimpleUserForm>();
		Iterator it = friendSet.iterator();
		while (it.hasNext()) {
			String obj = (String) it.next();
			try{
				long value = Long.parseLong(obj);
				UserModel model = redisUserService.getUser(value);
				friends.add(model.asSimpleUserForm());
			}catch (Exception e) {
				logger.error("loadFollows:"+e.getMessage());
			}
		}
		return friends;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countFriends(long uid) {
		Long size = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFriendKey(uid)).size();
		if (size == null || size == 0){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid))
			.intersectAndStore(FriendShipKeyManager.getFansKey(uid),
					FriendShipKeyManager.getUserFriendKey(uid));
			size = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFriendKey(uid)).size();
		}
		if (size != null){
			return size.intValue();
		}
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SystemRmdUserForm> loadMatchedFriends(long uid, int friendType) {
		List<SystemRmdUserForm> users = new ArrayList<SystemRmdUserForm>();
		if (friendType == FriendModel.FriendType.MOBILE.getMark()){
			//取出新数据
			Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator newdIt = newSet.iterator();
			while(newdIt.hasNext()){
				TypedTuple value = (TypedTuple)newdIt.next();
				String key = FriendShipKeyManager.getMobilesUserInfoKey(uid, (String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(1,openFriend);
				if (user != null){
					users.add(user);
				}
			}
			//取出老数据
			Set oldSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator oldIt = oldSet.iterator();
			while(oldIt.hasNext()){
				TypedTuple value = (TypedTuple)oldIt.next();
				String key = FriendShipKeyManager.getMobilesUserInfoKey(uid, (String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(0,openFriend);
				if (user != null){
					users.add(user);
				}
				
			}
			//合并数据
			userRelationTemplate.boundZSetOps(
					FriendShipKeyManager.getUserMobileNewFriendKey(uid))
					.unionAndStore(
							FriendShipKeyManager.getUserMobileFriendKey(uid),
							FriendShipKeyManager.getUserMobileFriendKey(uid));
			//删除新数据
			userRelationTemplate.delete(FriendShipKeyManager.getUserMobileNewFriendKey(uid));
		}
		if (friendType == FriendModel.FriendType.SINA.getMark()){
			//取出新数据
			Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator newIt = newSet.iterator();
			while(newIt.hasNext()){
				TypedTuple value = (TypedTuple)newIt.next();
				String key = FriendShipKeyManager.getSinaUserInfoKey((String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(1,openFriend);
				if (user != null){
					try{
						long matchedUid = Long.parseLong(openFriend.getMatchedUid());
						boolean isFollowed = this.isFollowed(uid, matchedUid);
						if (isFollowed) {
							deleteMatchedFriends(uid,matchedUid);
						} else {
							users.add(user);
						}
					}catch (Exception e) {
						logger.error("数据类型转化错误！"+e.getMessage()+"OpenFriend.MatchedUid="+openFriend.getMatchedUid());
					}
				}
			}
			//取出老数据
			Set oldSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator oldIt = oldSet.iterator();
			while(oldIt.hasNext()){
				TypedTuple value = (TypedTuple)oldIt.next();
				String key = FriendShipKeyManager.getSinaUserInfoKey((String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(0,openFriend);
				if (user != null && !users.contains(user)){
					try{
						long matchedUid = Long.parseLong(openFriend.getMatchedUid());
						boolean isFollowed = this.isFollowed(uid, matchedUid);
						if (isFollowed) {
							deleteMatchedFriends(uid,matchedUid);
						} else {
							users.add(user);
						}
					}catch (Exception e) {
						logger.error("数据类型转化错误！"+e.getMessage()+"OpenFriend.MatchedUid="+openFriend.getMatchedUid());
					}
					
				}
			}
			//合并数据
			userRelationTemplate.boundZSetOps(
					FriendShipKeyManager.getUserSinaFriendKey(uid))
					.unionAndStore(
							FriendShipKeyManager.getUserSinaNewFriendKey(uid),
							FriendShipKeyManager.getUserSinaFriendKey(uid));
			//删除新数据
			userRelationTemplate.delete(FriendShipKeyManager.getUserSinaNewFriendKey(uid));
			
		}
		if (friendType == FriendModel.FriendType.TENCENT.getMark()){
			//取出新数据
			Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator newIt = newSet.iterator();
			while(newIt.hasNext()){
				TypedTuple value = (TypedTuple)newIt.next();
				String key = FriendShipKeyManager.getTencentUserInfoKey((String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(1,openFriend);
				if (user != null){
					try{
						long matchedUid = Long.parseLong(openFriend.getMatchedUid());
						boolean isFollowed = this.isFollowed(uid, matchedUid);
						if (isFollowed) {
							deleteMatchedFriends(uid,matchedUid);
						} else {
							users.add(user);
						}
					}catch (Exception e) {
						logger.error("数据类型转化错误！"+e.getMessage()+"OpenFriend.MatchedUid="+openFriend.getMatchedUid());
					}
				}
			}
			//取出老数据
			Set oldSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator oldIt = oldSet.iterator();
			while(oldIt.hasNext()){
				TypedTuple value = (TypedTuple)oldIt.next();
				String key = FriendShipKeyManager.getTencentUserInfoKey((String)value.getValue());
				OpenFriend openFriend = this.getOpenFriend(key);
				SystemRmdUserForm user = parseToMatchedUser(0,openFriend);
				if (user != null && !users.contains(user)){
					try{
						long matchedUid = Long.parseLong(openFriend.getMatchedUid());
						boolean isFollowed = this.isFollowed(uid, matchedUid);
						if (isFollowed) {
							deleteMatchedFriends(uid,matchedUid);
						} else {
							users.add(user);
						}
					}catch (Exception e) {
						logger.error("数据类型转化错误！"+e.getMessage()+"OpenFriend.MatchedUid="+openFriend.getMatchedUid());
					}
				}
			}
			//合并数据
			userRelationTemplate.boundZSetOps(
					FriendShipKeyManager.getUserTencentFriendKey(uid))
					.unionAndStore(
							FriendShipKeyManager.getUserTencentNewFriendKey(uid),
							FriendShipKeyManager.getUserTencentFriendKey(uid));
			//删除新数据
			userRelationTemplate.delete(FriendShipKeyManager.getUserTencentNewFriendKey(uid));
			
		}
		return users;
	}
	
	
	private SystemRmdUserForm parseToMatchedUser(int isNew ,OpenFriend openFriend){
		try{
			long matchedUid = 0;
			try{
				matchedUid = Long.parseLong(openFriend.getMatchedUid());
			}catch (Exception e) {
				logger.error("数据转化出错！params="+openFriend.getMatchedUid());
				matchedUid = 0;
			}
			UserModel model = redisUserService.getUser(matchedUid);
			if (model != null && model.getUid()>0){
				SystemRmdUserForm user = new SystemRmdUserForm();
				user.setAvatarUrl(model.getAvatarUrl());
				user.setAvatarUrl(model.getAvatarUrl() == null?"default.jpg":model.getAvatarUrl());
				if (user.getAvatarUrl().indexOf("http://") < 0) {
					user.setAvatarUrl(HoolaConfig.getAliyunAvatarDomain() + user.getAvatarUrl());
				}
				user.setFriendType(openFriend.getOpenType());
				user.setScreenName(openFriend.getOpenNickName());
				user.setIsNew(isNew);
				user.setNickName(model.getNickName());
				user.setSignature(model.getSignature());
				user.setTime(System.currentTimeMillis());
				user.setUid(model.getUid());
				return user;
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings({ "unused" })
	private SystemRmdUserForm parse(MatchedUserModel matchUser,int isNew,int openType){
		SystemRmdUserForm user = new SystemRmdUserForm();
		try{
			UserModel model = redisUserService.getUser(matchUser.getUid());
			if (model != null && model.getUid()>0){
				user.setAvatarUrl(model.getAvatarUrl());
				user.setAvatarUrl(model.getAvatarUrl() == null?"default.jpg":model.getAvatarUrl());
				if (user.getAvatarUrl().indexOf("http://") < 0) {
					user.setAvatarUrl(HoolaConfig.getAliyunAvatarDomain() + user.getAvatarUrl());
				}
				user.setFriendType(openType);
				user.setScreenName(matchUser.getUserName());
				user.setIsNew(isNew);
				user.setNickName(model.getNickName());
				user.setSignature(model.getSignature());
				user.setTime(System.currentTimeMillis());
				user.setUid(matchUser.getUid());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertMatchedFriends(long uid,List<OpenFriend> users) {
		for (OpenFriend user : users){
			long matchedUidLong = 0;
			try{
				matchedUidLong = Long.parseLong(user.getMatchedUid());
			}catch (Exception e) {
				matchedUidLong = 0;
			}			
			if (!this.isFollowed(uid, matchedUidLong) && uid != matchedUidLong){
				if (user.getOpenType() == FriendModel.FriendType.MOBILE.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getMobilesUserInfoKey(uid, user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						Long index = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileFriendKey(uid)).rank(user.getOpenUserId());
						if (index == null){//排重
							userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileNewFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
						}
					}else{//老数据
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				
				if (user.getOpenType() == FriendModel.FriendType.SINA.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getSinaUserInfoKey(user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						Long index = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaFriendKey(uid)).rank(user.getOpenUserId());
						if (index == null){
							userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
						}
					}else{
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				
				if (user.getOpenType() == FriendModel.FriendType.TENCENT.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getTencentUserInfoKey(user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						Long index = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentFriendKey(uid)).rank(user.getOpenUserId());
						if (index == null){
							userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentNewFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
						}
					}else{
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentFriendKey(uid)).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				this.deleteUserFromNoMatchedList(user, user.getOpenType());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void batchInsertMatchFriends(List<OpenFriend> users) {
		for (OpenFriend user : users){
			long matchedUidLong = 0;
			try{
				matchedUidLong = Long.parseLong(user.getMatchedUid());
			}catch (Exception e) {
				matchedUidLong = 0;
			}	
			if (!this.isFollowed(user.getUid(), matchedUidLong) && user.getUid() != matchedUidLong){
				if (user.getOpenType() == FriendModel.FriendType.MOBILE.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getMobilesUserInfoKey(user.getUid(), user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileNewFriendKey(user.getUid())).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				
				if (user.getOpenType() == FriendModel.FriendType.SINA.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getSinaUserInfoKey(user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(user.getUid())).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				
				if (user.getOpenType() == FriendModel.FriendType.TENCENT.getMark()){
					openUserTemplate.boundHashOps(FriendShipKeyManager.getTencentUserInfoKey(user.getOpenUserId())).put("matchedUid", String.valueOf(user.getMatchedUid()));
					if (user.getIsMatched() == 2){//新匹配的
						userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentNewFriendKey(user.getUid())).add(user.getOpenUserId(), System.currentTimeMillis());
					}
				}
				this.deleteUserFromNoMatchedList(user, user.getOpenType());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertNoMatchFriends(long uid,List<OpenFriend> openFriends, int openType) {
		//插入详细信息
		for (OpenFriend openFriend : openFriends){
			this.insertOpenFriend(uid, openFriend);
		}
		if (openType == FriendModel.FriendType.MOBILE.getMark()) {
			for (OpenFriend openFriend : openFriends) {
				Long index = userRelationTemplate.boundZSetOps(
						FriendShipKeyManager.getUserMobileFriendKey(uid)).rank(
						openFriend.getOpenUserId());
				if (index == null) {
					userRelationTemplate.boundZSetOps(
							FriendShipKeyManager.getMobileNoMatchedFriends(uid))
							.add(openFriend.getOpenUserId(),
									System.currentTimeMillis());
				}
			}
		}

		if (openType == FriendModel.FriendType.SINA.getMark()) {
			for (OpenFriend openFriend : openFriends) {
				Long index = userRelationTemplate.boundZSetOps(
						FriendShipKeyManager.getUserSinaFriendKey(uid)).rank(
						openFriend.getOpenUserId());
				if (index == null) {
					userRelationTemplate.boundZSetOps(
							FriendShipKeyManager.getSinaNoMatchedFriends(uid))
							.add(openFriend.getOpenUserId(),
									System.currentTimeMillis());
				}

			}
		}
		
		if (openType == FriendModel.FriendType.TENCENT.getMark()) {
			for (OpenFriend openFriend : openFriends) {
				Long index = userRelationTemplate.boundZSetOps(
						FriendShipKeyManager.getUserTencentFriendKey(uid))
						.rank(openFriend.getOpenUserId());
				if (index == null) {
					userRelationTemplate.boundZSetOps(
							FriendShipKeyManager.getTencentNoMatchedFriends(uid)).add(
							openFriend.getOpenUserId(),
							System.currentTimeMillis());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteUserFromNoMatchedList(OpenFriend matchedUser, int openType) {
		if (openType == FriendModel.FriendType.MOBILE.getMark()){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getMobileNoMatchedFriends(matchedUser.getUid())).remove(matchedUser.getOpenUserId());
		}
		
		if (openType == FriendModel.FriendType.SINA.getMark()){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getSinaNoMatchedFriends(matchedUser.getUid())).remove(matchedUser.getOpenUserId());
		}
		
		if (openType == FriendModel.FriendType.TENCENT.getMark()){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getTencentNoMatchedFriends(matchedUser.getUid())).remove(matchedUser.getOpenUserId());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OpenFriend> loadNoMatchFriends(int openType,int start, int end, long uid) {
		List<OpenFriend> friends = new ArrayList<OpenFriend>();
		Set set = null;
		if (openType == FriendModel.FriendType.MOBILE.getMark()){
			set = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getMobileNoMatchedFriends(uid)).reverseRange(start, end);
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String monile = (String)it.next();
					try{
						String key = FriendShipKeyManager.getMobilesUserInfoKey(uid, monile);
						OpenFriend openFriend = this.getOpenFriend(key);
						friends.add(openFriend);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (openType == FriendModel.FriendType.SINA.getMark()){
			set = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getSinaNoMatchedFriends(uid)).reverseRange(start, end);
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String openUid = (String)it.next();
					try{
						String key = FriendShipKeyManager.getSinaUserInfoKey(openUid);
						OpenFriend openFriend = this.getOpenFriend(key);
						friends.add(openFriend);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (openType == FriendModel.FriendType.TENCENT.getMark()){
			set = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getTencentNoMatchedFriends(uid)).reverseRange(start, end);
			
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String openUid = (String)it.next();
					try{
						String key = FriendShipKeyManager.getTencentUserInfoKey(openUid);
						OpenFriend openFriend = this.getOpenFriend(key);
						friends.add(openFriend);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return friends;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countNoMatchFriends(int openType, long uid) {
		Long count = 0l;
		if (openType == FriendModel.FriendType.MOBILE.getMark()){
			count = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getMobileNoMatchedFriends(uid)).size();
		}
		
		if (openType == FriendModel.FriendType.SINA.getMark()){
			count =userRelationTemplate.boundZSetOps(FriendShipKeyManager.getSinaNoMatchedFriends(uid)).size();
		}
		
		if (openType == FriendModel.FriendType.TENCENT.getMark()){
			count = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getTencentNoMatchedFriends(uid)).size();
		}
		return count == null ? 0 : count.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isFriend(long uid, long fid) {
		Long index = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFriendKey(uid)).rank(String.valueOf(fid));
		return index != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteMatchedFriends(long uid,long fid) {
		BindModel sinaBind = redisUserService.getBindInfo(fid, BindModel.OpenType.SINA.getMark());
		if (sinaBind != null && StringUtils.hasText(sinaBind.getOpenUid())){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaFriendKey(uid)).remove(sinaBind.getOpenUid());
		}
		
		BindModel tencentBind = redisUserService.getBindInfo(fid, BindModel.OpenType.TENCENT.getMark());
		if (tencentBind != null && StringUtils.hasText(tencentBind.getOpenUid())){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentFriendKey(uid)).remove(tencentBind.getOpenUid());
		}
		
		UserModel user = redisUserService.getUser(fid);
		if (user != null && StringUtils.hasText(user.getMobile())){
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileFriendKey(uid)).remove(user.getMobile());
		}
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OpenFriend> loadAllOpenFriends(int openType, int start,
			int end, long uid) {
		List<OpenFriend> friends = new ArrayList<OpenFriend>();
		Set set = null;
		if (openType == BindModel.OpenType.SINA.getMark()){
			if (start == 0){
				userRelationTemplate.delete(FriendShipKeyManager.getUserAllSinaFollowsKey(uid));
				userRelationTemplate.boundZSetOps(
						FriendShipKeyManager.getUserSinaFriendKey(uid)).unionAndStore(
						FriendShipKeyManager.getSinaNoMatchedFriends(uid),
						FriendShipKeyManager.getUserAllSinaFollowsKey(uid));
			}
			set = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserAllSinaFollowsKey(uid)).reverseRange(start, end);
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String openUid = (String)it.next();
					try{
						String key = FriendShipKeyManager.getSinaUserInfoKey(openUid);
						OpenFriend openFriend = this.getOpenFriend(key);
						friends.add(openFriend);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (openType == BindModel.OpenType.TENCENT.getMark()){
			if (start == 0){
				userRelationTemplate.delete(FriendShipKeyManager.getUserAllTencentFollowsKey(uid));
				userRelationTemplate.boundZSetOps(
						FriendShipKeyManager.getUserTencentFriendKey(uid)).unionAndStore(
						FriendShipKeyManager.getTencentNoMatchedFriends(uid),
						FriendShipKeyManager.getUserAllTencentFollowsKey(uid));
			}
			set = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserAllTencentFollowsKey(uid)).reverseRange(start, end);
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String openUid = (String)it.next();
					try{
						String key = FriendShipKeyManager.getTencentUserInfoKey(openUid);
						OpenFriend openFriend = this.getOpenFriend(key);
						friends.add(openFriend);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
				
		return friends;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void rename() {
		Set set = super.openUserTemplate.keys("*tencent");
		for (Object obj : set){
			String name = (String)obj;
			openUserTemplate.rename(name, name.replaceAll("：", ":"));
		}
		
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countNewRecommendFriendsNum(long uid) {
		int sina = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).size().intValue();
		int tencent = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentNewFriendKey(uid)).size().intValue();
		int mobile = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileNewFriendKey(uid)).size().intValue();
		String time=(String)userTemplate.boundHashOps(UserKeyManager.getUserLastVisit(uid)).get("recommend");
		int editorRecommend=0;
		if(time!=null){
			editorRecommend=countNewEditorRecommendFriendNum(Long.valueOf(time));
		}
		return sina + tencent + mobile+editorRecommend;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countNewEditorRecommendFriendNum(long time) {
		Set editorRecommendSet = jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).reverseRangeByScoreWithScores(time, Long.MAX_VALUE);
		if(editorRecommendSet!=null)
			return editorRecommendSet.size();
		return 0;
	}
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}
	
	public void setIndexBuilder(IIndexBuilder indexBuilder) {
		this.indexBuilder = indexBuilder;
	}
	@SuppressWarnings("unchecked")
	@Override
	public long getUpdateUserIdFromQueue() {
		Object obj = userRelationTemplate.boundListOps(UserKeyManager.getUserUpdateQueueKey()).rightPop();
		if (obj != null) {
			try {
				long uid = Long.parseLong((String) obj);
				return uid;
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertUserUpdateQueue(long uid) {
		userRelationTemplate.boundListOps(UserKeyManager.getUserUpdateQueueKey()).leftPush(String.valueOf(uid));
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addBlacklist(long uid, long blackUid) {
		userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getUserBlacklistKey(uid)).add(
				String.valueOf(blackUid), System.currentTimeMillis());
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void cancelBlacklist(long uid, long blackUid) {
		userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getUserBlacklistKey(uid)).remove(
				String.valueOf(blackUid));
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SimpleUserForm> loadBlacklist(long uid, int start, int end) {
		Set blacklistSet = userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getUserBlacklistKey(uid)).reverseRange(
				start, end);
		List<SimpleUserForm> blacklist = new ArrayList<SimpleUserForm>();
		Iterator it = blacklistSet.iterator();
		while (it.hasNext()) {
			String obj = (String) it.next();
			try{
				long value = Long.parseLong(obj);
				UserModel model = redisUserService.getUser(value);
				blacklist.add(model.asSimpleUserForm());
			}catch (Exception e) {
				logger.error("loadFollows:"+e.getMessage());
			}
		}
		return blacklist;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean isBlackUser(long uid, long blackUid) {
		Long index = userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getUserBlacklistKey(uid)).rank(
				String.valueOf(blackUid));
		return index != null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countBlacklist(long uid) {
		Long count = userRelationTemplate.boundZSetOps(
				FriendShipKeyManager.getUserBlacklistKey(uid)).size();
		return count == null?0:count.intValue();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public TipsShowUser getNewestRecommend(long uid, long time) {
		TipsShowUser tipsUser = new TipsShowUser();
		Map<String, Object> matchedUser = getNewMatchedUser(uid);
		SimpleUserForm simpleUserForm=(SimpleUserForm)matchedUser.get("simpleUserForm");
		if(simpleUserForm!=null){
			tipsUser.setAvatar(simpleUserForm.getAvatar());
			tipsUser.setMemo((String)matchedUser.get("message"));
			tipsUser.setNickName(simpleUserForm.getNickName());
			tipsUser.setUid(simpleUserForm.getUid());
		}
		/*if (matchedUser == null){
			TypedTuple editorRecommedData = null;
			String timeLast=(String)userTemplate.boundHashOps(UserKeyManager.getUserLastVisit(uid)).get("recommend");
			if(timeLast!=null)
				time=Long.valueOf(timeLast);
			if (time >0){
				Set editorRecommendSet = jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).reverseRangeByScoreWithScores(time, Long.MAX_VALUE);
				Iterator editorRecommendIt = editorRecommendSet.iterator();
				while (editorRecommendIt.hasNext()){
					Object obj = editorRecommendIt.next();
					editorRecommedData = (TypedTuple)obj;
				}
			}
			if (editorRecommedData != null){
				long resultUid = Long.parseLong(String.valueOf(editorRecommedData.getValue()));
				if (resultUid > 0 && resultUid != uid){
					UserModel user = redisUserService.getUser(resultUid);
					if (user != null && user.getUid()>0){
						tipsUser = new TipsShowUser();
						tipsUser.setAvatar(DuanquConfig.getAliyunAvatarDomain()+user.getAvatarUrl());
						tipsUser.setMemo("趣拍推荐了TA");
						tipsUser.setNickName(user.getNickName());
						tipsUser.setUid(user.getUid());
						return tipsUser;
					}
				}
			}
		}else{
			tipsUser = new TipsShowUser();
			tipsUser.setAvatar(matchedUser.getAvatar());
			tipsUser.setMemo("TA加入了趣拍");
			tipsUser.setNickName(matchedUser.getNickName());
			tipsUser.setUid(matchedUser.getUid());
		}*/
		return tipsUser;
	}
	/**
	 * 获取匹配成功的用户，三个平台只获取一个用来 弹出层展示
	 * @param uid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> getNewMatchedUser(long uid){
		Map<String,Object> map=new HashMap<String, Object>();
		String key = null;
		double thirdScore=0;
		double editorScore=0;
		SimpleUserForm simpleUserFormThird=new SimpleUserForm();
		SimpleUserForm simpleUserFormEditor=new SimpleUserForm();
		//合并第三方平台新加入的好友，并取最大值
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
				userRelationTemplate.getConnectionFactory().getConnection());
		connection.del(FriendShipKeyManager.getUserAllFriendNew(uid));
		int[] wights={1,1,1};
		connection.zUnionStore(
				FriendShipKeyManager.getUserAllFriendNew(uid), 
				Aggregate.MAX, wights,
				FriendShipKeyManager.getUserSinaNewFriendKey(uid),
				FriendShipKeyManager.getUserTencentNewFriendKey(uid),
				FriendShipKeyManager.getUserMobileNewFriendKey(uid)
				);
		connection.close();
		Set setNewAll=userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserAllFriendNew(uid)).reverseRangeWithScores(0,0);
		if(setNewAll!=null){
			Iterator newIt = setNewAll.iterator();
			while(newIt.hasNext()){
				TypedTuple value = (TypedTuple)newIt.next();
				thirdScore=value.getScore();
				key = FriendShipKeyManager.getSinaUserInfoKey((String)value.getValue());
			}
		}
		if (key != null){
			OpenFriend openFriend = this.getOpenFriend(key);
			if (openFriend != null){
				UserModel user = redisUserService.getUser(openFriend.getMatchedUid());
				if (user != null){
					simpleUserFormThird= user.asSimpleUserForm();
				}
			}
		}
		String timeLast=(String)userTemplate.boundHashOps(UserKeyManager.getUserLastVisit(uid)).get("recommend");
		long time=0;
		TypedTuple typedTuple=null;
		if(timeLast!=null)
			time=Long.valueOf(timeLast);
		if (time >0){
			Set editorRecommendSet = jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).reverseRangeByScoreWithScores(time, Long.MAX_VALUE);
			Iterator editorRecommendIt = editorRecommendSet.iterator();
			while (editorRecommendIt.hasNext()){
				
				 Object obj = editorRecommendIt.next();
				 typedTuple = (TypedTuple)obj;
				 editorScore=typedTuple.getScore();
				 break;	 //因为前面是降序，我只需要取到这个时间区间的最大值即可以，因此取到一个值后，就立刻退出循环
			}
		}
		if (typedTuple != null){
			long resultUid = Long.parseLong(String.valueOf(typedTuple.getValue()));
			if (resultUid > 0 && resultUid != uid){
				UserModel user = redisUserService.getUser(resultUid);
				if (user != null && user.getUid()>0){
					simpleUserFormEditor.setAvatar(HoolaConfig.getAliyunAvatarDomain()+user.getAvatarUrl());
					simpleUserFormEditor.setNickName(user.getNickName());
					simpleUserFormEditor.setUid(user.getUid());
					/*tipsUser = new TipsShowUser();
					tipsUser.setAvatar(DuanquConfig.getAliyunAvatarDomain()+user.getAvatarUrl());
					tipsUser.setMemo("趣拍推荐了TA");
					tipsUser.setNickName(user.getNickName());
					tipsUser.setUid(user.getUid());
					return tipsUser;*/
				}
			}
		}
		if(thirdScore>editorScore){
			map.put("simpleUserForm",simpleUserFormThird );
			map.put("message", "TA加入了趣拍");
		}else{
			map.put("simpleUserForm", simpleUserFormEditor);
			map.put("message", "趣拍推荐了TA");
		}
		/*//新浪
		Long sinaCount = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).size();
		if (sinaCount != null && sinaCount.intValue()>0){
			//取出新数据
			Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator newIt = newSet.iterator();
			while(newIt.hasNext()){
				TypedTuple value = (TypedTuple)newIt.next();
				value.getScore();
				key = FriendShipKeyManager.getSinaUserInfoKey((String)value.getValue());
			}
		}
		
		if (key != null){
			OpenFriend openFriend = this.getOpenFriend(key);
			if (openFriend != null){
				UserModel user = redisUserService.getUser(openFriend.getMatchedUid());
				if (user != null){
					return user.asSimpleUserForm();
				}
			}
		}
		//腾讯
		Long tencentCount = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserTencentNewFriendKey(uid)).size();
		if (tencentCount != null && tencentCount.intValue()>0){
			//取出新数据
			Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserSinaNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
			Iterator newIt = newSet.iterator();
			while(newIt.hasNext()){
				TypedTuple value = (TypedTuple)newIt.next();
				key = FriendShipKeyManager.getTencentUserInfoKey((String)value.getValue());
			}
		}
		if (key != null){
			OpenFriend openFriend = this.getOpenFriend(key);
			if (openFriend != null){
				UserModel user = redisUserService.getUser(openFriend.getMatchedUid());
				if (user != null){
					return user.asSimpleUserForm();
				}
			}
		}
		
		//手机
		Set newSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserMobileNewFriendKey(uid)).reverseRangeWithScores(0 ,-1);
		Iterator newdIt = newSet.iterator();
		while(newdIt.hasNext()){
			TypedTuple value = (TypedTuple)newdIt.next();
			key = FriendShipKeyManager.getMobilesUserInfoKey(uid, (String)value.getValue());
		}
		if (key != null){
			OpenFriend openFriend = this.getOpenFriend(key);
			if (openFriend != null){
				UserModel user = redisUserService.getUser(openFriend.getMatchedUid());
				if (user != null){
					return user.asSimpleUserForm();
				}
			}
		}
		*/
		return map;
	}
}
