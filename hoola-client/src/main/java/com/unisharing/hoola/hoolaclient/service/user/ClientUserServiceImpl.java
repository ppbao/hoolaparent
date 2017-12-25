package com.unisharing.hoola.hoolaclient.service.user;


import com.unisharing.hoola.hoolaclient.dao.ClientUserMapper;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolaredis.service.syn.user.IUserSynService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("clientUserService")
public class ClientUserServiceImpl implements IClientUserService {
	Log logger = LogFactory.getLog(ClientUserMapper.class);
    @Autowired
	ClientUserMapper clientUserMapper;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	@Autowired
	IUserSynService userSynService;

	@Override
	public void insertRegister(UserModel userModel) {
		userModel.setRoleId(3);
		clientUserMapper.insertUserInfo(userModel);
		clientUserMapper.insertUserToken(userModel);
	}
	

	@Override
	public void updateUserInfoMobile(long uid, String mobile) {
		UserModel userModel=new UserModel();
		userModel.setUid(uid);
		userModel.setMobile(mobile);
		clientUserMapper.updateUserInfo(userModel);
		
	}
	
	


	@Override
	public void updateUserInfo(UserModel userModel) {
		clientUserMapper.updateUserInfo(userModel);
		
	}


	@Override
	public void insertRegisterTh(UserModel userModel, BindModel bindModel) {
		UserModel um = clientUserMapper.selectUserInfo(userModel);
		if (um != null) {
			clientUserMapper.updateUserInfo(userModel);
			clientUserMapper.updateUserBind(bindModel);
		} else {
			clientUserMapper.insertUserInfo(userModel);
			clientUserMapper.insertUserBind(bindModel);
		}

	}

	@Override
	public void updateLogin(UserModel userModel) {
		clientUserMapper.updateUserInfo(userModel);
		clientUserMapper.updateUserToken(userModel);
	}
	
	

	@Override
	public void updateUserBind(BindModel bindModel) {
		clientUserMapper.updateUserBind(bindModel);
	}


	@Override
	public void insertOpenFriend(long uid, List<OpenFriend> list, int openType) {
		List<OpenFriend> newList = new ArrayList<OpenFriend>();
		if (list != null) {
			for (Iterator<OpenFriend> iterator = list.iterator(); iterator
					.hasNext();) {
				OpenFriend openFriend = iterator.next();
				openFriend.setUid(uid);
				OpenFriend dBopenFriend = clientUserMapper
						.selectOpenFriend(openFriend);
				if (dBopenFriend == null) {
					try {
						openFriend.setOpenNickName(EmojiUtils.filterEmoji(openFriend.getOpenNickName()));//过滤特殊字符
						clientUserMapper.inserOpenFriend(openFriend);
						newList.add(openFriend);
					} catch (Exception e) {
						logger.error("插入第三方好友数据出错！params=" + openFriend	+ ";Message=" + e);
					}
				}
			}
		}
		if (newList.size() > 0) {
			redisRelationshipService.insertNoMatchFriends(uid, newList,	openType);
		}
	}

	@Override
	public void insertMobiles(long uid, List<UserMobileModel> list) {
		if (list != null) {
			for (Iterator<UserMobileModel> iterator = list.iterator(); iterator
					.hasNext();) {
				UserMobileModel userMobileModel = iterator.next();
				userMobileModel.setUid(uid);
				UserMobileModel userDB=clientUserMapper.getMobiles(uid,userMobileModel.getMobile());
				if(userDB==null){
				try {
					clientUserMapper.insertMobiles(userMobileModel);
				} catch (Exception e) {
					logger.error("插入通讯录数据出错！params=" + userMobileModel+ ";Message=" + e);
				}
				}
			}
		}

	}

	@Override
	public void updateThfriend(Map<String, Object> map) {
		clientUserMapper.updateThfriend(map);
	}

	@Override
	public void insertBindModel(BindModel bindModel) {
		clientUserMapper.insertUserBind(bindModel);
	}

	/*
	 * @Override public void getUserFriendList() { List<BindModel>
	 * list=clientUserMapper.queryBindModelList();
	 * if(list!=null&&list.size()>0){ for(Iterator<BindModel>
	 * iterator=list.iterator();iterator.hasNext();){ BindModel
	 * bindModel=iterator.next(); int openType=bindModel.getOpenType();
	 * List<OpenFriend> listFriend; if(openType==1){
	 * listFriend=shareService.loadSinaFollows(bindModel.getOpenUid(),
	 * bindModel.getAccessToken()); }else{
	 * listFriend=shareService.loadQQFollows(bindModel.getOpenUid(),
	 * bindModel.getAccessToken()); }
	 * insertOpenFriend(bindModel.getUid(),listFriend); Map<String, Object>
	 * map=new HashMap<String, Object>(); map.put("p_exectype", 1);
	 * map.put("p_functype", openType); map.put("p_uid", bindModel.getUid());
	 * updateThfriend(map); } } }
	 */

	@Override
	public List<OpenFriend> queryOpenFriendListByUid(OpenFriend openFriend) {

		return clientUserMapper.queryOpenFriendlByUid(openFriend);
	}

	@Override
	public void insertFriend(FriendModel friendModel) {
		long uid=friendModel.getUid();
		long fid=friendModel.getFid();
		clientUserMapper.insertFriend(friendModel);//插入关系表
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("num", -1);
		map.put("num2", 0);
		Map<String, Object> mapZfss=new HashMap<String, Object>();
		mapZfss.put("uid", fid);
		mapZfss.put("num", -1);
		mapZfss.put("num2", 0);
		//userSynService.synFollow(friendModel.getUid(), friendModel.getFid());//更新缓存
		if (friendModel.getIsFriend() == 1) {
			clientUserMapper.updateIsFriend(friendModel);
			map.put("num2", -1);
			mapZfss.put("num2", -1);
		}
		
		//更新好友数和关注数，粉丝数由异步线程操作
		/*if(uid>fid){//用户关注Hoola君
			clientUserMapper.updateUserHysAndGzs(map);//更新自己的好友数和关注数
			clientUserMapper.updateUserHysAndZfss(mapZfss);//更新对方的好友数和真粉丝数
		}else{//Hoola君关注我
			clientUserMapper.updateUserHysAndZfss(mapZfss);//更新对方的好友数和真粉丝数
			clientUserMapper.updateUserHysAndGzs(map);//更新自己的好友数和关注数
		}*/
		
	}
	@Override
	public void deleteFriend(FriendModel friendModel) {
		clientUserMapper.deleteFriend(friendModel);
		long uid=friendModel.getUid();
		long fid=friendModel.getFid();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("num", 1);
		map.put("num2", 1);
		Map<String, Object> mapZfss=new HashMap<String, Object>();
		mapZfss.put("uid", fid);
		mapZfss.put("num", 1);
		mapZfss.put("num2", 1);
		//由异步线程进行更新
		//clientUserMapper.updateUserHysAndGzs(map);//删除自己的好友数
		//clientUserMapper.updateUserHysAndZfss(mapZfss);//删除自己的好友数
		friendModel.setIsFriend(0);
		clientUserMapper.updateIsFriend(friendModel);

	}

	@Override
	public List<UserModel> queryMathchUserList(int type, long uid,
			long matchedTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UserModel> list = null;
		map.put("uid", uid);
		map.put("matchedTime", matchedTime);
		if (type == 0)
			list = clientUserMapper.queryAllList(map);
		else if (type == 1)
			list = clientUserMapper.queryThfriendList(map);
		else if (type == 2)
			list = clientUserMapper.queryMobileList(map);
		return list;
	}

	@Override
	public List<OpenFriend> queryMatchedOpenFriends(long uid) {
		return clientUserMapper.queryMatchedOpenFriends(uid);
	}
	

	@Override
	public void insertUserBind(BindModel bindModel) {
		clientUserMapper.insertUserBind(bindModel);
		
	}


	@Override
	public void updateOpenFriend(long uid) {
		clientUserMapper.updateOpenFriend(uid);
	}

	@Override
	public List<OpenFriend> queryMatchUserMobileModels(long uid) {

		return clientUserMapper.queryMatchUserMobileModels(uid);
	}

	@Override
	public void updateMatchUserMobileModels(long uid) {
		clientUserMapper.updateMatchUserMobileModels(uid);

	}

	public void setClientUserMapper(ClientUserMapper clientUserMapper) {
		this.clientUserMapper = clientUserMapper;
	}


	public IRedisRelationshipService getRedisRelationshipService() {
		return redisRelationshipService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}


	public IUserSynService getUserSynService() {
		return userSynService;
	}


	public void setUserSynService(IUserSynService userSynService) {
		this.userSynService = userSynService;
	}


	@Override
	public UserModel getUser(long uid) {
		UserModel user = new UserModel();
		user.setUid(uid);
		this.clientUserMapper.selectUserInfo(user);
		return this.clientUserMapper.selectUserInfo(user);
	}


	@Override
	public void followEachOther(long uid, long fid) {
		FriendModel friendModel1 = new FriendModel();
		friendModel1.setUid(uid);
		friendModel1.setCreateTime(System.currentTimeMillis());
		friendModel1.setFid(fid);
		friendModel1.setIsFriend(1);
		friendModel1.setIsTrue(1);
		clientUserMapper.insertFriend(friendModel1);//插入关系表
		
		FriendModel friendModel2 = new FriendModel();
		friendModel2.setUid(fid);
		friendModel2.setCreateTime(System.currentTimeMillis());
		friendModel2.setFid(uid);
		friendModel2.setIsFriend(1);
		friendModel2.setIsTrue(1);
		clientUserMapper.insertFriend(friendModel2);//插入关系表
		
	}


	@Override
	public void hoolaUpdateUserRelationshipCount(long uid, long fid) {
		clientUserMapper.updateUserRelationshipCount1(uid);
		clientUserMapper.updateUserRelationshipCount1(fid);
	}


	@Override
	public void insertInvite(InviteModel inviteModel) {
		clientUserMapper.insertInvite(inviteModel);
		
	}
	
	

}
