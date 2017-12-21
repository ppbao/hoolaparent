package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.bean.FollowBean;
import com.unisharing.hoola.hoolacommon.bean.InviteBean;
import com.unisharing.hoola.hoolacommon.model.FriendModel;
import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolacommon.vo.SystemRmdUserForm;
import com.unisharing.hoola.hoolacommon.vo.TipsShowUser;
import com.unisharing.hoola.hoolaredis.service.hot.IRedisHotService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class RelationshipController extends BaseController {
	
	Log logger = LogFactory.getLog(RelationshipController.class);
	
	private static int FRIEND_PAGE_SIZE = 20;
	@Resource
	IRedisRelationshipService redisRelationshipService;
	@Resource
	IRedisHotService redisHotService;
	
	
	/**
	 * 推荐好友
	 * @param page
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/recommend", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result recommendUser(
			@RequestParam(value = "page", required = false ,defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		Map<String,Object> map = new HashMap<String,Object>();
		long uid = this.getUid(token);
		List<SimpleUserForm> recommendUsers = redisHotService.loadRecommendUsers(uid);
		map.put("editorRmd", recommendUsers);
	
		TreeSet<SystemRmdUserForm> users = new TreeSet<SystemRmdUserForm>();
		//手机
		List<SystemRmdUserForm> mobileUsers = redisRelationshipService.loadMatchedFriends(uid, FriendModel.FriendType.MOBILE.getMark());
		users.addAll(mobileUsers);
		//新浪
		List<SystemRmdUserForm> sinaUsers = redisRelationshipService.loadMatchedFriends(uid, FriendModel.FriendType.SINA.getMark());
		users.addAll(sinaUsers);
		//腾讯
		List<SystemRmdUserForm> tencentUsers = redisRelationshipService.loadMatchedFriends(uid, FriendModel.FriendType.TENCENT.getMark());
		users.addAll(tencentUsers);
		
		users.comparator();
		redisUserService.updateUserLastVist(uid, System.currentTimeMillis(), "recommend");//记录最后一次访问时间
		map.put("systemRmd", users);
		result.setCode(200);
		result.setData(map);
		result.setPages(0);
		result.setMessage("数据获取成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}

	/****
	 *
	 * @param time
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/recommend/tips", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result recommendUserTips(
			@RequestParam(value = "time", required = false ,defaultValue = "0") long time,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);

		TipsShowUser user = redisRelationshipService.getNewestRecommend(uid, time);
		result.setCode(200);
		result.setData(user);
		result.setPages(0);
		result.setMessage("数据获取成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 推荐达人（公众帐号）
	 * @param page
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/talent/recommend", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result recommendTalent(
			@RequestParam(value = "page", required = false ,defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = this.getUid(token);
		List<SimpleUserForm> recommendTalents = redisHotService.loadRecommendTalents(uid);
		result.setCode(200);
		result.setData(recommendTalents);
		result.setPages(0);
		result.setMessage("数据获取成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@RequestMapping(value = "/user/open/friends", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadOpenFriends(
			@RequestParam("openType") int openType,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("token") String token) {
		
		int start = (page - 1) * FRIEND_PAGE_SIZE;
		int end = page * FRIEND_PAGE_SIZE - 1;
		UserModel user = this.getUser(token);
		List<SystemRmdUserForm> matchedUser = redisRelationshipService.loadMatchedFriends(user.getUid(), openType);
		List<OpenFriend> friends = redisRelationshipService.loadNoMatchFriends(openType, start, end, user.getUid());
		int count = redisRelationshipService.countNoMatchFriends(openType,user.getUid());
		Result result = new Result();
		Map map = new HashMap();
		map.put("match", matchedUser);
		map.put("noMatch", friends);
		result.setCode(200);
		result.setData(map);
		result.setPages((count - 1) / FRIEND_PAGE_SIZE + 1);
		result.setMessage("数据获取成功！");
		result.setTime(System.currentTimeMillis());
		
		return result;
	}
	
	
	@RequestMapping(value = "/user/open/friends/all", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadAllOpenFriends(
			@RequestParam("openType") int openType,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("token") String token) {
		
		int start = (page - 1) * FRIEND_PAGE_SIZE;
		int end = page * FRIEND_PAGE_SIZE - 1;
		UserModel user = this.getUser(token);
		List<OpenFriend> friends = redisRelationshipService.loadAllOpenFriends(openType, start, end, user.getUid());
		int count = redisRelationshipService.countNoMatchFriends(openType,user.getUid());
		Result result = new Result();
		result.setCode(200);
		result.setData(friends);
		result.setPages((count - 1) / FRIEND_PAGE_SIZE + 1);
		result.setMessage("数据获取成功！");
		result.setTime(System.currentTimeMillis());
		
		return result;
	}
	
	@RequestMapping(value = "/user/fans", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadFans(@RequestParam(value = "uid", required = true, defaultValue = "0") long uid,
			@RequestParam(value = "page", required = false ,defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long curUid = getUid(token);
		uid = uid == 0? curUid:uid;
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			int newFansCount = redisRelationshipService.countNewFans(curUid);
			List<SimpleUserForm> fans = redisRelationshipService.loadFans(uid, page, FRIEND_PAGE_SIZE,curUid);
			List<FriendForm> friends = new ArrayList<FriendForm>();
			if (uid == curUid){ //访问自己粉丝列表
				int count = 0;
				for (SimpleUserForm fan : fans){
					count ++;
					FriendForm friend = new FriendForm();
					friend.setUser(fan);
					boolean isFollow = redisRelationshipService.isFollowed( uid,fan.getUid());//判断我是否关注对方
					friend.setIsFollow(isFollow ? 2 : 0); 
					if (count <= newFansCount){
						friend.setIsNew(1);
					}
					friends.add(friend);
				}
			}else{ //访问别人的关注列表
				for (SimpleUserForm fan : fans){
					FriendForm friend = new FriendForm();
					friend.setUser(fan);
					boolean isFollow = redisRelationshipService.isFollowed(curUid, fan.getUid());
					boolean isFans = redisRelationshipService.isFollowed(fan.getUid(), curUid);
					if (isFollow){
						friend.setIsFollow(1);
						if (isFans){
							friend.setIsFollow(2);
						}
					}
					friends.add(friend);
				}
			}
			int count = redisRelationshipService.countFans(uid);
			result.setCode(200);
			result.setData(friends);
			result.setPages((count - 1) /FRIEND_PAGE_SIZE + 1);
			if (uid == 1){
				result.setTotal(0);
			}else{
				result.setTotal(count);
			}
			result.setMessage("数据获取成功！");
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	/**
	 * 关注用户
	 * @param uid	//被关注用户ID
	 * @param token	//当前登陆用户ID
	 * @return
	 */
	@RequestMapping(value = "/user/follow", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result followUser(
			@RequestParam(value = "uid", required = true, defaultValue = "0") long uid,
			@RequestParam("token")String token) {
		Result result = new Result();
		UserModel user = super.getUser(token);
		if (user == null || user.getUid() == 0){
			result.setCode(HoolaErrorCode.TOKEN_ERROR.getCode());
			result.setData(token);
			result.setMessage(HoolaErrorCode.TOKEN_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		if (user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		UserModel followUser = redisUserService.getUser(uid);
		
		if (followUser == null || followUser.getUid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("关注的用户不存在！");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		if (uid == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("关注用户ID 不能为0");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		if (uid == user.getUid()){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("不能关注自己！");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		//是否是黑名单
		boolean isBlack = redisRelationshipService.isBlackUser(uid,user.getUid());
		if (isBlack) {
			result = new Result();
			result.setCode(HoolaErrorCode.BLACKUSER.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.BLACKUSER.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}	
		
		boolean isFollow = redisRelationshipService.isFollowed(user.getUid(),uid);
		/*if (isFollow){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("已经关注过了！");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}*/
		boolean flag = false;//标识是否关注成功！
		if (!isFollow){
			try{
				redisRelationshipService.follow(user.getUid(), uid);
				//插入消息队列以便更新好友数和粉丝数等
				redisRelationshipService.insertUserUpdateQueue(user.getUid());
				redisRelationshipService.insertUserUpdateQueue(uid);
				flag = true;
			}catch(Exception e){
				logger.error("关注用户出错！"+e.getMessage()+";Params:uid="+uid+",token="+token);
			}
		}
		result.setCode(200);
		result.setData("");
		result.setMessage("关注成功！");
		result.setTime(System.currentTimeMillis());
		try{
			if (flag){
				FollowBean bean = new FollowBean();
				bean.setFid(uid);
				bean.setUid(user.getUid());
				bean.setCreatetime(System.currentTimeMillis());
				redisJMSMessageService.insertFollowMessageQueue(bean);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.FOLLOW));
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData(e.getMessage());
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		return result;
	}
	
	/**
	 * 批量关注用户
	 * @param :uid	//被关注用户ID
	 * @param token	//当前登陆用户ID
	 * @return
	 */
	@RequestMapping(value = "/user/batch/follow", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result batchFollowUser(
			@RequestParam(value = "uids", required = true, defaultValue = "") String uids,
			@RequestParam("token")String token) {
		Result result = new Result();
		UserModel user = super.getUser(token);
		if (user == null || user.getUid() == 0){
			result.setCode(HoolaErrorCode.TOKEN_ERROR.getCode());
			result.setData(token);
			result.setMessage(HoolaErrorCode.TOKEN_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		//用户是否被禁言
		if (user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		if (uids == null){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData(token);
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}else{
			String[] uidStrings = uids.split(",");
			for (String uidString : uidStrings){
				long uid = 0;
				if (StringUtils.hasText(uidString)){
					try{
						uid = Long.parseLong(uidString);
					}catch(Exception e){
						logger.error("数据类型转化出错！");
					}
				}
				if (uid > 0){
					UserModel followUser = redisUserService.getUser(uid);
					if (followUser == null || followUser.getUid() == 0){
						break;
					}
					if (uid == user.getUid()){
						break;
					}
					boolean isFollow = redisRelationshipService.isFollowed(user.getUid(),uid);
					
					boolean flag = false;//标识是否关注成功！
					if (!isFollow){
						try{
							redisRelationshipService.follow(user.getUid(), uid);
							//插入消息队列以便更新好友数和粉丝数等
							redisRelationshipService.insertUserUpdateQueue(user.getUid());
							redisRelationshipService.insertUserUpdateQueue(uid);
							flag = true;
						}catch(Exception e){
							logger.error("关注用户出错！"+e.getMessage()+";Params:uid="+uid+",token="+token);
						}
					}
					try{
						if (flag){
							FollowBean bean = new FollowBean();
							bean.setFid(uid);
							bean.setUid(user.getUid());
							bean.setIsTalent(1);
							bean.setCreatetime(System.currentTimeMillis());
							redisJMSMessageService.insertFollowMessageQueue(bean);
							hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.FOLLOW));
						}
					}catch(Exception e){
						logger.error("发送关注消息出错！");
					}
				}
			}
			result.setCode(200);
			result.setData("");
			result.setMessage("关注成功！");
			result.setTime(System.currentTimeMillis());
			return result;
		}
	}
	
	
	/**
	 * 关注用户
	 * @param uid	//被关注用户ID
	 * @param token	//当前登陆用户ID
	 * @return
	 */
	@RequestMapping(value = "/user/unfollow", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result unFollowUser(
			@RequestParam(value = "uid", required = true, defaultValue = "0") long uid,
			@RequestParam("token")String token) {
		Result result = new Result();
		UserModel user = super.getUser(token);
		if (user == null || user.getUid() == 0){
			result.setCode(HoolaErrorCode.TOKEN_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.TOKEN_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		if (uid == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		redisRelationshipService.unfollow(user.getUid(), uid);
		//插入消息队列以便更新好友数粉丝数等
		redisRelationshipService.insertUserUpdateQueue(uid);
		redisRelationshipService.insertUserUpdateQueue(user.getUid());
		
		result.setCode(200);
		result.setData("");
		result.setMessage("取消关注成功！");
		result.setTime(System.currentTimeMillis());
		//发送取消关注信息
		try{
			FollowBean bean = new FollowBean();
			bean.setFid(uid);
			bean.setUid(user.getUid());
			bean.setCreatetime(System.currentTimeMillis());
			redisJMSMessageService.insertUnFollowMessageQueue(bean);
			hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.UNFOLLOW));
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData(e.getMessage());
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		return result;
	}
	
	/**
	 * 用户关注列表
	 * @param uid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/follows", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result follows(
			@RequestParam(value = "uid", required = true, defaultValue = "0") long uid,
			@RequestParam(value = "page", required = false ,defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		uid = uid == 0? getUid(token):uid;
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			List<SimpleUserForm> follows = redisRelationshipService.loadFollows(uid, page, FRIEND_PAGE_SIZE);
			List<FriendForm> friends = new ArrayList<FriendForm>();
			if (uid == getUid(token)){ //访问自己关注列表
				for (SimpleUserForm follow : follows){
					FriendForm friend = new FriendForm();
					friend.setUser(follow);
					boolean isFriend = redisRelationshipService.isFollowed(follow.getUid(), uid);
					friend.setIsFollow(isFriend ? 2 : 1); 
					friends.add(friend);
				}
			}else{ //访问别人的关注列表
				for (SimpleUserForm follow : follows){
					FriendForm friend = new FriendForm();
					friend.setUser(follow);
					boolean isFollow = redisRelationshipService.isFollowed(getUid(token), follow.getUid());
					boolean isFans = redisRelationshipService.isFollowed(follow.getUid(), getUid(token));
					if (isFollow){
						friend.setIsFollow(1);
						if (isFans){
							friend.setIsFollow(2);
						}
					}
					friends.add(friend);
				}
			}
			
			int count = redisRelationshipService.countFollows(uid);
			result.setCode(200);
			result.setData(friends);
			result.setPages((count - 1) / FRIEND_PAGE_SIZE + 1);
			if (uid == 1){
				result.setTotal(0);
			}else{
				result.setTotal(count);
			}
			result.setMessage("数据获取成功！");
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	/**
	 * 用户关注列表
	 * @param uid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/friends", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result friends(
			@RequestParam(value = "uid", required = true, defaultValue = "0") long uid,
			@RequestParam(value = "page", required = false ,defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		uid = uid == 0? getUid(token):uid;
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			List<SimpleUserForm> friends = redisRelationshipService.loadFriend(uid, page, 0);//取所有
			result.setCode(200);
			result.setData(friends);
			result.setTotal(friends.size());
			result.setMessage("数据获取成功！");
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	/**
	 * 用户邀请好友
	 * @param
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/invite", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result inviteFriends(@RequestParam("name") String name,
			@RequestParam("openType") int openType,
			@RequestParam("token") String token) {
		Result result = new Result();
		InviteBean invite = new InviteBean();
		invite.setName(name);
		invite.setOpenType(openType);
		invite.setUid(getUid(token));
		try{
			redisJMSMessageService.insertInviteQueue(invite);
			hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.INVITE));
			result.setCode(200);
			result.setData("");
			result.setMessage("邀请成功！");
		}catch(Exception e){
			result.setCode(200);
			result.setData("");
			result.setMessage("邀请失败！"+e.getMessage());
		}
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	/**
	 * 添加黑名单
	 * @param uid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/black/add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result addBlacklist(@RequestParam("token") String token,
			@RequestParam("uid") long uid) {
		Result result = new Result();
		long curUid = super.getUid(token);
		redisRelationshipService.addBlacklist(curUid, uid);
		result.setCode(200);
		result.setMessage("操作成功！");
		result.setData("");
		result.setTime(System.currentTimeMillis());
		
		//发送取消关注信息
		try{
			boolean needSys = false;
			if (redisRelationshipService.isFollowed(curUid, uid)){
				//取消关注（取消我对ta的关注）
				redisRelationshipService.unfollow(curUid, uid);
				FollowBean bean = new FollowBean();
				bean.setFid(uid);
				bean.setUid(curUid);
				bean.setCreatetime(System.currentTimeMillis());
				redisJMSMessageService.insertUnFollowMessageQueue(bean);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.UNFOLLOW));
				needSys = true;
			}
			
			if (redisRelationshipService.isFollowed(uid, curUid)){
				//取消ta对我的关注
				redisRelationshipService.unfollow(uid, curUid);
				FollowBean bean2 = new FollowBean();
				bean2.setFid(curUid);
				bean2.setUid(uid);
				bean2.setCreatetime(System.currentTimeMillis());
				redisJMSMessageService.insertUnFollowMessageQueue(bean2);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.UNFOLLOW));
				needSys = true;
			}
			//同步粉丝数
			if (needSys){
				//插入消息队列以便更新好友数粉丝数等
				redisRelationshipService.insertUserUpdateQueue(uid);
				redisRelationshipService.insertUserUpdateQueue(curUid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		return result;
	}
	
	/**
	 * 添加黑名单
	 * @param uid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/user/black/cancel", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result cancelBlacklist(@RequestParam("token") String token,
			@RequestParam("uid") long uid) {
		Result result = new Result();
		redisRelationshipService.cancelBlacklist(super.getUid(token), uid);
		result.setCode(200);
		result.setMessage("操作成功！");
		result.setData("");
		result.setTime(System.currentTimeMillis());
		return result;
	}

	/***
	 *
	 * @param token
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/user/black/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result listBlackList(@RequestParam("token") String token,
			@RequestParam(value ="page" , required = false, defaultValue = "1") int page) {
		Result result = new Result();
		long curUid = super.getUid(token);
		int count = redisRelationshipService.countBlacklist(curUid);
		List<SimpleUserForm> blackUsers = redisRelationshipService
				.loadBlacklist(curUid, (page - 1) * FRIEND_PAGE_SIZE, page
						* FRIEND_PAGE_SIZE);
		result.setCode(200);
		result.setData(blackUsers);
		result.setMessage("获取成功");
		result.setTotal(count);
		result.setPages((count - 1) / FRIEND_PAGE_SIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
}
