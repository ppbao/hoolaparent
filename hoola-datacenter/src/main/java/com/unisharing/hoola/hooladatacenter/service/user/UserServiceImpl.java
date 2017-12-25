package com.unisharing.hoola.hooladatacenter.service.user;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.group.IClientGroupService;
import com.unisharing.hoola.hoolaclient.service.message.IClientMessageService;
import com.unisharing.hoola.hoolaclient.service.user.IClientUserService;
import com.unisharing.hoola.hoolacommon.bean.FollowBean;
import com.unisharing.hoola.hoolacommon.bean.InviteBean;
import com.unisharing.hoola.hoolacommon.bean.MobileMessageBean;
import com.unisharing.hoola.hoolacommon.indexService.IIndexBuilder;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.service.IShareService;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.GroupForm;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.key.TimelineKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.group.IRedisGroupService;
import com.unisharing.hoola.hoolaredis.service.jms.IRedisJMSMessageService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
@Component
public class UserServiceImpl extends BaseRedisService implements IUserService {
	private static long QUPAI_UID = 1; //趣拍用户ID
	
	Log logger = LogFactory.getLog(UserServiceImpl.class);
	
	private static String INVITE_TEXT = "@{0}，我在Hoola发了一大波视频巨作，就等你来看了！快一起来玩吧>>>"+ HoolaConfig.getHost()+" 有乐同享才是好基友，抱拳！";
	
	//private static String SYNWEIBO_TEXT = "我在玩趣拍，手机微视频神器。正在发我自己拍的视频巨作，快下一个一起来玩吧>>>https://itunes.apple.com/cn/app/qu-pai-shou-ji-wei-shi-pin/id672243559?mt=8（来自@趣拍APP ）";
	
	private static String SYNWEIBO_TEXT = "我在玩Hoola，手机微视频神器。能天马行空当导演，更能和男神女神一起视频互动！子曰，有乐同享才是好基友。快和我一起玩哟>>>https://itunes.apple.com/cn/app/qu-pai-shou-ji-wei-shi-pin/id672243559?mt=8 （来自@趣拍APP ）";
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	@Autowired
	IShareService shareService;
	@Autowired
	IClientUserService clientUserService;
	@Autowired
	IRedisGroupService redisGroupService;
	@Autowired
	IClientGroupService clientGroupService;
	@Autowired
	IRedisMessageService redisMessageService;
	@Autowired
	IClientMessageService clientMessageService;
	@Autowired
	IRedisJMSMessageService redisJMSMessageService;
	@Autowired
	IRedisTimelineService redisTimelineService;
	@Autowired
	IIndexBuilder indexBuilder;
//	IESIndexBuilder esIndexBuilder;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleUserRegister() {
		String submitJson = "";
		UserModel user = null;
		do{
			submitJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getNewUserMQKey()).rightPop();
			System.out.println(submitJson);
			user = JSON.parseObject(submitJson, UserModel.class);
			if (user != null){
				user.setNickName(EmojiUtils.filterEmoji(user.getNickName()));//对特殊字符进行编码
				user.setRoleId(3);//注册的为普通用户
				// 插入用户基本信息
				BindModel bind = null;
				bind = redisUserService.getBindInfo(user.getUid(), BindModel.OpenType.SINA.getMark());
				if (bind == null || bind.getUid() == 0){
					bind = redisUserService.getBindInfo(user.getUid(), BindModel.OpenType.TENCENT.getMark());
				}
				try{
					// 插入数据库
					if (bind != null && bind.getUid() > 0) {
						bind.setOpenNickName(EmojiUtils.filterEmoji(bind.getOpenNickName()));
						clientUserService.insertRegisterTh(user, bind);
					} else {
						clientUserService.insertRegister(user);
					}
				}catch (Exception e){
					logger.error("用户信息插入数据库出错：Params="+user+";Error Message="+e);
				}
				
				//自动关注趣拍官方帐号,趣拍（Hoola君）的数据采用拉的方式
				redisRelationshipService.follow(user.getUid(), 1);
				//官方帐号自动关注新用户
				redisRelationshipService.follow(1, user.getUid());
				//新用户和趣拍相互关注
				try {
					clientUserService.followEachOther(QUPAI_UID,user.getUid());
					
					//clientUserService.duanquUpdateUserRelationshipCount(QUPAI_UID, user.getUid());
					//插入异步更新消息队列
					redisRelationshipService.insertUserUpdateQueue(QUPAI_UID);
					redisRelationshipService.insertUserUpdateQueue(user.getUid());
				} catch (Exception e) {
					logger.error("新用户和趣拍相互关注插入数据库出错！Error=" + e);
				}
				
				//推送一条趣拍最新的内容给新用户
				Set createSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(QUPAI_UID)).reverseRangeWithScores(0, 0);
				Iterator it = createSet.iterator();
				while (it.hasNext()){
					ZSetOperations.TypedTuple obj = (ZSetOperations.TypedTuple)it.next();
					relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(user.getUid())).add((String)obj.getValue(), obj.getScore().longValue());
				}
				
				
				List<OpenFriend> friends = new ArrayList<OpenFriend>();
				if (bind != null && bind.getUid() > 0) {
					try {
						if (bind.getOpenType() == BindModel.OpenType.SINA.getMark()) {
							friends = shareService.loadSinaFollows(bind.getOpenUid(), bind.getAccessToken());
						}

						if (bind.getOpenType() == BindModel.OpenType.TENCENT.getMark()) {
							friends = shareService.loadQQFollows(bind.getOpenUid(), bind.getAccessToken());
						}
					} catch (Exception e) {
						logger.error("获取第三方好友信息出错！Params=" + bind + ";Error Message=" + e);
					}
				}
				if (friends != null && friends.size()>0){
					try{
						//插入第三方关系表信息
						clientUserService.insertOpenFriend(user.getUid(),friends,bind.getOpenType());
					}catch(Exception e){
						logger.error("插入第三方好友信息入库出错！;Error Message=" + e);
					}
				}
				
				
				if (friends != null && friends.size() > 0){
					try{
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("p_exectype", 1);//表示程序调用执行
						map.put("p_functype", 1);//表示匹配平台类型 type=0，获取全部匹配的用户信息，type=1 第三方平台，type=2手机通讯录
						map.put("p_uid", user.getUid());//用户ID
						clientUserService.updateThfriend(map);
						//1、取出最新匹配成功数据
						List<OpenFriend> matchedFriends = clientUserService.queryMatchedOpenFriends(user.getUid());
						//2、插入缓存
						redisRelationshipService.insertMatchedFriends(user.getUid(), matchedFriends);
						//3、更新数据库状态
						clientUserService.updateOpenFriend(user.getUid());
						//4、TODO 插入搜索引擎
						//this.buildOpenUserIndex(uid,friends);
					}catch(Exception e){
						logger.error("第三方好友匹配出错！;Error Message=" + e);
					}
				}
				
				//自动发私信
				try{
					MessageModel model = new MessageModel();
					model.setCreateTime(System.currentTimeMillis());
					model.setIsDelete(0);
					model.setIsNew(1);
					model.setMessageText("你好，欢迎来到趣拍！让趣拍君陪着你一起记录生活，认识新盆友吧！PS：趣拍君不是机器人，有什么不懂的，都可以私信我哟！≥ω≤");
					model.setRecUid(user.getUid());
					model.setUid(QUPAI_UID);
					//插入私信详细信息
					model = redisMessageService.insertMessage(model);
					//插入对话列表
					redisMessageService.insertDialog(QUPAI_UID, user.getUid());
					//插入对话详细列表
					RedisMessageModel redisModel = model.asRedisForm();
					redisMessageService.insertDialogMessage(redisModel);
					
					//插入数据库 自动发的私信不写入数据库了
					//model.setMessageText(EmojiUtils.encodeEmoji(model.getMessageText()));
					//clientMessageService.insertUserMessage(model);
					
				}catch (Exception e) {
					logger.error("私信发送失败!Error Message="+e);
				}
				
				//创建索引
				//indexBuilder.buildUserSuggestIndex(user.getNickName());
				//indexBuilder.buildUserIndex(user.getNickName(), user.getUid());
				try{
					IndexUserModel indexModel = new IndexUserModel();
					indexModel.setNickName(user.getNickName());
					indexModel.setSignature(user.getSignature());
					indexModel.setTime(user.getCreateTime());
					indexModel.setUid(user.getUid());
					indexBuilder.buildUserIndex(indexModel);
				}catch(Exception e){
					logger.error("创建用户索引出错！Error Message"+e);
				}
				
			}
		}while(user != null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleSynWeibo() {
		String message = null;
		do{
			message = (String)jmsTemplate.boundListOps(JMSKeyManager.getSynWeiboMqKey()).rightPop();
			if (message != null){
				try{
					long uid = Long.parseLong(message);
					BindModel bind = redisUserService.getBindInfo(uid, BindModel.OpenType.SINA.getMark());
					if (bind != null && bind.getUid() > 0) {
						try{
							if (bind.getOpenType() == BindModel.OpenType.SINA.getMark()){
								//发送新浪微博
								shareService.shareToSina("register_syn_weibo.jpg", SYNWEIBO_TEXT, bind.getAccessToken());
							}
						}catch(Exception e){
							logger.error("注册自动发微博失败！"+e.getMessage());
						}
						
						try{
							if (bind.getOpenType() == BindModel.OpenType.SINA.getMark()){
								//关注趣拍APP
								shareService.sinaFollow( bind.getAccessToken());
							}
						}catch(Exception e){
							logger.error("注册自动发微博失败！"+e.getMessage());
						}
						
					}
				}catch (Exception e) {
					logger.error("同步微博出错！"+e.getMessage()+",e="+e);
				}
			}
		}while(message != null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleLogin() {
		String json = null;
		BindModel bind = null;
		do{
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getLoginListKey()).rightPop();
			bind = JSON.parseObject(json, BindModel.class);
			 if (bind != null){
				try {
					 //更新access_token
					clientUserService.updateUserBind(bind);
					//取第三方平台好友关系
					List<OpenFriend> friends = new ArrayList<OpenFriend>();
					try {
						if (bind.getOpenType() == BindModel.OpenType.SINA.getMark()) {
							friends = shareService.loadSinaFollows(bind.getOpenUid(), bind.getAccessToken());
						}

						if (bind.getOpenType() == BindModel.OpenType.TENCENT.getMark()) {
							friends = shareService.loadQQFollows(bind.getOpenUid(), bind.getAccessToken());
						}
					} catch (Exception e) {
						logger.error("获取第三方好友信息出错！Params=" + bind + ";Error Message=" + e.getMessage());
					}
					clientUserService.insertOpenFriend(bind.getUid(), friends, bind.getOpenType());
					//0、匹配
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("p_exectype", 1);// 表示程序调用执行
					map.put("p_functype", 1);// 表示匹配第三方平台
					map.put("p_uid", bind.getUid());// 用户ID
					clientUserService.updateThfriend(map);
					//1、取出匹配成功数据
					List<OpenFriend> matchedFriends = clientUserService.queryMatchedOpenFriends(bind.getUid());
					//2、插入缓存
					redisRelationshipService.insertMatchedFriends(bind.getUid(), matchedFriends);
					//3、更新数据库状态
					clientUserService.updateOpenFriend(bind.getUid());
					
					//匹配手机号码：
					UserModel user = redisUserService.getUser(bind.getUid());
					
					if (StringUtils.isNotBlank(user.getMobile())){
						//匹配手机号码
						map.put("p_exectype", 1);// 表示程序调用执行
						map.put("p_functype", 2);// 表示匹配第三方平台
						map.put("p_uid", bind.getUid());// 用户ID
						clientUserService.updateThfriend(map);
						List<OpenFriend> mobiles = clientUserService.queryMatchUserMobileModels(user.getUid());
						redisRelationshipService.insertMatchedFriends(bind.getUid(), mobiles);
						clientUserService.updateMatchUserMobileModels(bind.getUid());
					}
				} catch (Exception e) {
					logger.error("第三方好友匹配出错！;Error Message=" + e.getMessage());
					e.printStackTrace();
				}
			 }
		}while(bind != null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleUserBinding(long uid) {
		String json = null;
		BindModel bind = null;
		do{
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getBindingListKey()).rightPop();
			bind = JSON.parseObject(json, BindModel.class);
			if (bind != null){
				//插入绑定信息表
				
				UserModel user = redisUserService.getUser(bind.getUid());
				if (user != null && user.getUid() >0){
					if (bind.getOpenType() != BindModel.OpenType.MOBILE.getMark()){
						//clientUserService.insertRegisterTh(user, bind);
						BindModel oldBind = redisUserService.getBindInfo(uid, bind.getOpenType());
						if (oldBind == null || oldBind.getUid() == 0){//以前没绑定过
							clientUserService.insertBindModel(bind);
						}
					}
					List<OpenFriend> friends = new ArrayList<OpenFriend>();
					try{
						if (bind.getOpenType() == BindModel.OpenType.SINA.getMark()){
							friends = shareService.loadSinaFollows(bind.getOpenUid(), bind.getAccessToken());
						}
						
						if (bind.getOpenType() == BindModel.OpenType.TENCENT.getMark()){
							friends = shareService.loadQQFollows(bind.getOpenUid(), bind.getAccessToken());
						}
						
						if (bind.getOpenType() == BindModel.OpenType.MOBILE.getMark()){
							//更新数据库用户表手机号码
							clientUserService.updateUserInfoMobile(bind.getUid(), bind.getOpenNickName());
							
						}
					} catch (Exception e) {
						logger.error("获取第三方好友信息出错！Params=" + bind + ";Error Message=" + e.getMessage());
					}
					// 插入第三方关系表信息
					if (friends.size() > 0) {
						try{
							//插入第三方关系表信息
							clientUserService.insertOpenFriend(bind.getUid(),friends,bind.getOpenType());
						}catch(Exception e){
							logger.error("插入第三方好友信息入库出错！;Error Message=" + e.getMessage());
						}
						try{
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("p_exectype", 1);// 表示程序调用执行
							map.put("p_functype", 1);// 表示匹配平台类型,第三方平台
							map.put("p_uid", bind.getUid());// 用户ID
							//0、匹配
							clientUserService.updateThfriend(map);
							//1、取出最新匹配成功数据
							List<OpenFriend> matchedFriends = clientUserService.queryMatchedOpenFriends(bind.getUid());
							//2、插入缓存
							redisRelationshipService.insertMatchedFriends(bind.getUid(), matchedFriends);
							//3、更新数据库状态
							clientUserService.updateOpenFriend(bind.getUid());
							//4、TODO 插入搜索引擎
							//this.buildOpenUserIndex(uid,friends);
						}catch(Exception e){
							logger.error("第三方好友匹配出错！;Error Message=" + e.getMessage());
						}
					}
				}
			}
		}while(bind != null);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleFollow() {
		String submitJson = null;
		FollowBean bean = null;
		do{
			 submitJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getNewFollowListKey()).rightPop();
			 bean = JSON.parseObject(submitJson, FollowBean.class);
			 if (bean != null){
				 //非名人和不是趣拍（Hoola君才进行推送）
				 if (!redisUserService.isFamous(bean.getFid()) && bean.getFid() != 1){
					 //推送发布的内容
					 Set createSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(bean.getFid())).rangeWithScores(0, -1);
					 //推送转发的
					 Set forwardSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(bean.getFid())).rangeWithScores(0, -1);
					 Set pushSet = new HashSet();
					 pushSet.addAll(createSet);
					 pushSet.addAll(forwardSet);
					 Iterator it = pushSet.iterator();
					 while (it.hasNext()){
						 ZSetOperations.TypedTuple obj = (ZSetOperations.TypedTuple)it.next();
						 ActionModel action = ActionModel.parse((String)obj.getValue());
						 if (action.getAction() == ActionModel.Action.LIKE.getMark() || action.getAction() == ActionModel.Action.FORWARD.getMark()){
							 if (!redisTimelineService.isRevLikeTime(bean.getUid(), action.getCid())){
								 //插入总体内容列表
								 relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(bean.getUid())).add((String)obj.getValue(), obj.getScore().longValue());
								 //插入转发的推送列表
								 relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsForwardTimelineKey(bean.getUid())).add((String)obj.getValue(), obj.getScore().longValue());
								 //插入用户接受喜欢推送列表
								 redisTimelineService.insertUserRevLikeTime(bean.getUid(), action.getCid(),action.getUid());
							 }
						 }else{
							 //创建的插入 总体的推送列表
							 relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(bean.getUid())).add((String)obj.getValue(), obj.getScore().longValue());
							 
							 //插入创建的 推送列表
							 relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsCreateTimelineKey(bean.getUid())).add((String)obj.getValue(), obj.getScore().longValue());
							 
						 }
					 }
				 }
				 //删除推荐好友中的数据
				redisRelationshipService.deleteMatchedFriends(bean.getUid(),bean.getFid());
				 
				boolean bool = redisRelationshipService.isFollowed(bean.getFid(), bean.getUid());
				FriendModel friendModel = new FriendModel();
				friendModel.setCreateTime(System.currentTimeMillis());
				friendModel.setFid(bean.getFid());
				friendModel.setIsFriend(bool ? 1 : 0);
				friendModel.setIsTrue(1);
				friendModel.setUid(bean.getUid());
				try {
					clientUserService.insertFriend(friendModel);
				} catch (Exception e) {
					logger.error("关注，插入好友关系数据到数据库出错！" + e.getMessage());
				}
				
			 }
		}while(bean != null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleUnfollow() {
		String submitJson = null;
		FollowBean bean = null;
		do{
			 submitJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getUnFollowListKey()).rightPop();
			 bean = JSON.parseObject(submitJson, FollowBean.class);
			 if (bean != null){
				 //非名人和不是趣拍（Hoola君才进行推送）
				 if (!redisUserService.isFamous(bean.getFid()) && bean.getFid() != 1){
					 //发布的
					 Set createSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(bean.getFid())).rangeWithScores(0, -1);
					 //转发的
					 Set forwardSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(bean.getFid())).rangeWithScores(0, -1);
					 Set pushSet = new HashSet();
					 pushSet.addAll(createSet);
					 pushSet.addAll(forwardSet);
					 Iterator it = pushSet.iterator();
					 while (it.hasNext()){
						 ZSetOperations.TypedTuple obj = (ZSetOperations.TypedTuple)it.next();
						 //从总体的推送列表中删除数据
						 relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(bean.getUid())).remove((String)obj.getValue());
						 
						 ActionModel action = ActionModel.parse((String)obj.getValue());
						 //创建 从接受创建列表中删除
						 if (action.getAction() == ActionModel.Action.CREATE.getMark()){
							 relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsCreateTimelineKey(bean.getUid())).remove((String)obj.getValue());
						 }
						 //转发和喜欢（从接受转发的列表中删除）
						 if (action.getAction() == ActionModel.Action.LIKE.getMark() || action.getAction() == ActionModel.Action.FORWARD.getMark()){
							 relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsForwardTimelineKey(bean.getUid())).remove((String)obj.getValue());
						 }
						 
						 //转发 和 喜欢（带转发）需要删除去重判断
						 if (action.getAction() == ActionModel.Action.LIKE.getMark() || action.getAction() == ActionModel.Action.FORWARD.getMark()){
							 redisTimelineService.deleteUserRevLikeTime(bean.getUid(), action.getCid(),action.getUid());
						 }
					 }
				 }
				 //删除对方分组内的信息
				 long fid = bean.getFid();
				 List<GroupForm> groups = redisGroupService.loadGroups(fid);
				 if (groups != null && groups.size()>0){
					 //删除组内好友
					 for (GroupForm group : groups){
						 redisGroupService.deleteGroupUser(fid, group.getName(), bean.getUid());
						 try{
							 //删除数据库好友关系
							 clientGroupService.deleteGroupRelation(group.getName(), bean.getFid(), bean.getUid());
						 }catch (Exception e) {
							 logger.error("修正对方分组信息,删除数据库好友关系出错！Message:"+e.getMessage()+",Params="+group);
						}
						
					 }
				 }
				 
				 //删除自己分组内的信息
				 long uid = bean.getUid();
				 List<GroupForm> myGroups = redisGroupService.loadGroups(uid);
				 if (myGroups != null && myGroups.size()>0){
					 //删除组内好友
					 for (GroupForm group : myGroups){
						 redisGroupService.deleteGroupUser(uid, group.getName(), bean.getFid());
						 try{
							 //删除数据库好友关系
							 clientGroupService.deleteGroupRelation(group.getName(), bean.getUid(), bean.getFid());
						 }catch (Exception e) {
							 logger.error("修正对方分组信息，删除数据库好友关系出错！Message:"+e.getMessage()+",Params="+group);
						}
						
					 }
				 }
				 
				 //删除数据库数据
				 try{
					 FriendModel friendModel = new FriendModel();
					 friendModel.setCreateTime(System.currentTimeMillis());
					 friendModel.setFid(bean.getFid());
					 friendModel.setIsFriend(0);
					 friendModel.setIsTrue(1);
					 friendModel.setUid(bean.getUid());
					 clientUserService.deleteFriend(friendModel);
				 }catch (Exception e) {
					logger.error("删除数据库关注关系出错！Message:"+e.getMessage()+",Params="+bean);
				}
				
				 
			 }
		}while(bean != null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleInvite() {
		String submitJson = null;
		InviteBean bean = null;
		do{
			 submitJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getInviteListKey()).rightPop();
			 bean = JSON.parseObject(submitJson, InviteBean.class);
			 if (bean != null){
				 String text = MessageFormat.format(INVITE_TEXT, bean.getName());
				 if (bean.getOpenType() == BindModel.OpenType.SINA.getMark()){
					 BindModel bind = redisUserService.getBindInfo(bean.getUid(), bean.getOpenType());
					 shareService.shareToSina(null, text, bind.getAccessToken());
				 }
				 if (bean.getOpenType() == BindModel.OpenType.TENCENT.getMark()){
					 BindModel bind = redisUserService.getBindInfo(bean.getUid(), bean.getOpenType());
					 shareService.shareToQQWeibo(null, text, bind.getAccessToken(), bind.getOpenUid());
				 }
				 try{
					 InviteModel inviteModel = new InviteModel();
					 inviteModel.setCreateTime(System.currentTimeMillis());
					 inviteModel.setNickName(bean.getName());
					 inviteModel.setType(bean.getOpenType());
					 inviteModel.setUid(bean.getUid());
					 //TODO 插入邀请好友日志
					 clientUserService.insertInvite(inviteModel);
				 }catch(Exception e){
					 logger.error("插入邀请日志失败！e ="+ e);
				 }
			 }
		}while(bean != null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleMobileUp() {
		String json = null;
		MobileMessageBean bean = null;
		do{
			 json = (String)jmsTemplate.boundListOps(JMSKeyManager.getMobilesListkey()).rightPop();
			 bean = JSON.parseObject(json, MobileMessageBean.class);
			 
			 if (bean != null){
				 //插入数据库
				 List<UserMobileModel> mobiles = new ArrayList<UserMobileModel>();
				 if (bean.getMobileStr() != null){
					 String[] records = bean.getMobileStr().split("\\|");
						for (String record : records){
							UserMobileModel mobile = new UserMobileModel();
							String[] res = record.split(":");
							if (res.length == 2){
								mobile.setMobile(res[1]);
								mobile.setNickName(res[0]);
								mobile.setCrateTime(System.currentTimeMillis());
								mobile.setUid(bean.getUid());
								mobiles.add(mobile);
								//创建索引
								try{
									IndexOpenUserModel indexOpenUser = new IndexOpenUserModel();
									indexOpenUser.setOpenNickName(mobile.getNickName());
									indexOpenUser.setOpenType(BindModel.OpenType.MOBILE.getMark());
									indexOpenUser.setOpenUserId(mobile.getMobile());
									indexOpenUser.setUid(mobile.getUid());
									indexBuilder.buildOpenUserIndex(indexOpenUser);
								}catch(Exception e){
									logger.error("创建通讯录索引出错！e="+e);
								}
							}
						}
					 clientUserService.insertMobiles(bean.getUid(),mobiles);
				 }
			 }
		}while(bean != null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleUserEdit() {
		String json = null;
		UserModel user = null;
		do{
			 json = (String)jmsTemplate.boundListOps(JMSKeyManager.getUserEditKey()).rightPop();
			 user = JSON.parseObject(json, UserModel.class);
			if (user != null) {
				//更新数据库
				UserModel newUser = new UserModel();
				if (StringUtils.isNotBlank(user.getAvatarUrl())){
					newUser.setAvatarUrl(user.getAvatarUrl());
				}
				if (StringUtils.isNotBlank(user.getNickName())){
					newUser.setNickName(EmojiUtils.filterEmoji(user.getNickName()));
				}
				if (StringUtils.isNotBlank(user.getSignature())){
					newUser.setSignature(EmojiUtils.filterEmoji(user.getSignature()));
				}
				if (StringUtils.isNotBlank(user.getVideoUrl())){
					newUser.setVideoUrl(user.getVideoUrl());
				}
				if (StringUtils.isNotBlank(user.getVideoFaceUrl())){
					newUser.setVideoFaceUrl(user.getVideoFaceUrl());
				}
				
				newUser.setUid(user.getUid());
				clientUserService.updateUserInfo(newUser);
				//删除用户索引，重新索引
				indexBuilder.deleteUserIndex(user.getUid());
				IndexUserModel indexModel = new IndexUserModel();
				indexModel.setNickName(user.getNickName());
				indexModel.setSignature(user.getSignature());
				indexModel.setTime(user.getCreateTime());
				indexModel.setUid(user.getUid());
				indexBuilder.buildUserIndex(indexModel);
			}
		}while(user != null);
		
	}
	
	
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

	public void setShareService(IShareService shareService) {
		this.shareService = shareService;
	}

	public void setClientUserService(IClientUserService clientUserService) {
		this.clientUserService = clientUserService;
	}

	public void setRedisGroupService(IRedisGroupService redisGroupService) {
		this.redisGroupService = redisGroupService;
	}

	public void setClientGroupService(IClientGroupService clientGroupService) {
		this.clientGroupService = clientGroupService;
	}

	public void setIndexBuilder(IIndexBuilder indexBuilder) {
		this.indexBuilder = indexBuilder;
	}

	public void setRedisMessageService(IRedisMessageService redisMessageService) {
		this.redisMessageService = redisMessageService;
	}

	public void setClientMessageService(IClientMessageService clientMessageService) {
		this.clientMessageService = clientMessageService;
	}

	public void setRedisJMSMessageService(
			IRedisJMSMessageService redisJMSMessageService) {
		this.redisJMSMessageService = redisJMSMessageService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	@Override
	public void run() {
		System.out.println("================"+Thread.currentThread().getName());
		this.handleUserRegister();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
