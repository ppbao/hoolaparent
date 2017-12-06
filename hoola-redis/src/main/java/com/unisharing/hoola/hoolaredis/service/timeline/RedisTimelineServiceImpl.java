package com.unisharing.hoola.hoolaredis.service.timeline;


import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.SettingModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.submit.Pager;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.ContentForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.key.*;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.content.RedisContentServiceImpl;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.DefaultStringTuple;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.util.*;
@Service("redisTimelineService")
public class RedisTimelineServiceImpl extends BaseRedisService implements IRedisTimelineService{
	Log logger = LogFactory.getLog(RedisContentServiceImpl.class);
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserContentList(long uid, long cid, int isPrivate) {
		ActionModel member = new ActionModel(uid, ActionModel.Action.CREATE.getMark(),cid);
		if (isPrivate == 1){//私密
			return relationTemplate.boundZSetOps(TimelineKeyManager.getUserPrivateList(uid)).add(member.toString(),System.currentTimeMillis());
		}else if(isPrivate == 2){ //组内分享
			return relationTemplate.boundZSetOps(TimelineKeyManager.getUserGroupContentsKey(uid)).add(member.toString(), System.currentTimeMillis());
		}else{
			return relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(uid)).add(member.toString(),System.currentTimeMillis());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserLikeList(long uid, long cid) {
		ActionModel action = new ActionModel(uid, ActionModel.Action.LIKE.getMark(),cid);
		return relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).add(action.toString(),System.currentTimeMillis());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteUserLikeList(long uid, long cid) {
		ActionModel action = new ActionModel(uid, ActionModel.Action.LIKE.getMark(),cid);
		Long result = relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).remove(action.toString());
		return (result > 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserTimeLine(long uid,ActionModel action) {
		//TODO 创建和转发分开存储
		/*if (action.getAction() == ActionModel.Action.CREATE.getMark()){
			return relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsCreateTimelineKey(uid)).add(action.toString(),System.currentTimeMillis());
		}
		if (action.getAction() == ActionModel.Action.FORWARD.getMark()){
			return relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsForwardTimelineKey(uid)).add(action.toString(),System.currentTimeMillis());
		}*/
		return relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).add(action.toString(),System.currentTimeMillis());
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserGroupTimeLine(long uid, ActionModel action) {
		return relationTemplate.boundZSetOps(TimelineKeyManager.getGroupTimelineKey(uid)).add(action.toString(),System.currentTimeMillis());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteUserTimeLine(long uid,ActionModel action) {
		//TODO 创建和转发分开存储
		if (action.getAction() == ActionModel.Action.CREATE.getMark()){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsCreateTimelineKey(uid)).remove(action.toString());
		}
		
		if (action.getAction() == ActionModel.Action.FORWARD.getMark()){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserFollowsForwardTimelineKey(uid)).remove(action.toString());
		}
		
		Long result = relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).remove(action.toString());
	    return (result >0);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ActionForm> loadUserTimeline(long uid, Pager pager, String type) {
		int start = (pager.getPage() - 1) * pager.getPageSize();
		int end = (pager.getPage()) * pager.getPageSize() - 1;
		if (start == 0){
			//删除原来的拉去的数据
			relationTemplate.delete(TimelineKeyManager.getUserTimeLineKey(uid));
		}
		Set actionSet = new HashSet();
		List<ActionForm> actions = new ArrayList<ActionForm>();
		String topAction = null;
		if (type.equals("all")){
			actionSet = this.loadAllTimelineActions(uid, start, end,0);//获取关注用户所有内容列表（创建+转发）
			Set topActionSet = this.getTopActions(uid); //制定内容
			//取出置顶内容
			if (topActionSet != null){
				Iterator it = topActionSet.iterator();
				while (it.hasNext()) {
					TypedTuple obj = (TypedTuple) it.next();
					topAction = (String)obj.getValue();
					long time = obj.getScore().longValue();
					ActionModel actionModel = ActionModel.parse(topAction);
					ActionForm actionForm = this.getActionForm(actionModel, uid, time,false);
					if (actionForm.getContent().getcStatus() == 0){
						actionForm.setTop(1);//设置置顶标识
						actions.add(actionForm);
					}
				}
			}
		}
		
		if (type.equals("create")){
			actionSet = this.loadCreateTimelineActions(uid, start, end);//获取关注用户创建的内容列表
		}
		
		if (type.equals("forward")){
			actionSet = this.loadForwardTimelineActions(uid, start, end);//获取关注用户创建的内容列表
		}
		
		if ("friend".equals(type)){//好友
			actionSet = this.loadFriendTimelineActions(uid, start, end);//获取好友的创建发送列表
		}
		
		if ("private".equals(type)){//仅自己
			actionSet = this.loadUserPrivateContents(uid, start, end);//获取仅自己的内容列表
			
		}
		// 循环取出内容详细信息
		if (actionSet != null) {
			Iterator it = actionSet.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				if (topAction == null || !value.equals(topAction)){ //去掉重复的
					long time = obj.getScore().longValue();
					ActionModel actionModel = ActionModel.parse(value);
					ActionForm actionForm = this.getActionForm(actionModel, uid, time,false);
					//显示正常的内容和自己被编辑屏蔽的内容
					if (actionForm.getContent().getcStatus() == 0
							|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED
									.getMark() && actionForm.getContent()
									.getUser().getUid() == uid)) {
						actions.add(actionForm);
					}
				}
			}
		}
		return actions;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ActionForm> loadUserNewestTime(long uid){
		Object objTime = relationTemplate.boundValueOps(
				TimelineKeyManager.getUserLastVisitTimelineTimeKey(uid)).get();
		long lastTime = 0;
		if (objTime != null){
			lastTime = Long.parseLong(String.valueOf(objTime));
		}
		List<ActionForm> actions = new ArrayList<ActionForm>();
		String topAction = null;
		Set actionSet = this.loadAllTimelineActions(uid, 0, 50,lastTime);//获取关注用户所有内容列表（创建+转发）
		Set topActionSet = this.getTopActions(uid); //制定内容
		//取出置顶内容
		if (topActionSet != null){
			Iterator it = topActionSet.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				topAction = (String)obj.getValue();
				long time = obj.getScore().longValue();
				ActionModel actionModel = ActionModel.parse(topAction);
				ActionForm actionForm = this.getActionForm(actionModel, uid, time,false);
				if (actionForm.getContent().getcStatus() == 0){
					actionForm.setTop(1);//设置置顶标识
					actions.add(actionForm);
				}
			}
		}
		// 循环取出内容详细信息
		if (actionSet != null) {
			Iterator it = actionSet.iterator();
			while (it.hasNext() && actions.size() < 50) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String) obj.getValue();
				if (topAction == null || !value.equals(topAction)) { // 去掉重复的
					long time = obj.getScore().longValue();
					ActionModel actionModel = ActionModel.parse(value);
					ActionForm actionForm = this.getActionForm(actionModel,
							uid, time, false);
					actions.add(actionForm);
				}
			}
		}
		return actions;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int countUserTimeline(long uid) {
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).size();
		return count.intValue();
	}
	
	/**
	 * 获取关注用户所有的内容（转发+创建）
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set loadAllTimelineActions(long uid,int start,int end,long time){
		UserModel user = redisUserService.getUser(uid);
		//初始化最新的20条推荐
		this.initTopEditorRecommendList();
		if (start == 0){
			DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
			//拉取名人好友数据
			pullFamousContent(connection,uid,"all");
			//拉取短趣君的数据
			if (redisRelationshipService.isFollowed(uid, 1)){//判断是否关注短趣君； 短趣君 id = 1
				Set temp = connection.zRangeByScoreWithScores(TimelineKeyManager.getUserPublicList(1), user.getCreateTime(), Long.MAX_VALUE);
				if (temp != null){
					Iterator it = temp.iterator();
					while (it.hasNext()) {
						DefaultStringTuple obj = (DefaultStringTuple) it.next();
						String value = obj.getValueAsString();
						long score = obj.getScore().longValue();
						connection.zAdd(TimelineKeyManager.getUserQupaiList(uid), score, value);
					}
				}
				int[] weights = {1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights,
						TimelineKeyManager.getUserQupaiList(uid),//  短趣君在用户注册以后发部的内容 id = 1
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
			
			//合并推的数据+自己的数据+编辑推荐的数据
			SettingModel setting = redisUserService.getUserSetting(uid);
			if (setting != null && setting.getRecommend() == 0){
				int[] weights = {1,1,1,1,1,1};
				connection.zUnionStore(TimelineKeyManager.getUserTimeLineKey(uid),
						Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(uid),// 自身公开数据
						TimelineKeyManager.getUserPrivateList(uid),// 自己私密数据;
						TimelineKeyManager.getUserGroupContentsKey(uid),//组内分享数据
						//TimelineKeyManager.getUserForwardList(uid),// 自己喜欢数据; 自己喜欢的不要显示
						TimelineKeyManager.getGroupTimelineKey(uid),// 好友组内分享推送数据
						TimelineKeyManager.getPublicTimelineKey(uid),// 公开推送的数据;
						TimelineKeyManager.getUserTimeLineKey(uid));// 拉取的数据;
			}else{
				int[] weights = {1,1,1,1,1,1,1};
				connection.zUnionStore(TimelineKeyManager.getUserTimeLineKey(uid),
						Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(uid),// 自身公开数据
						TimelineKeyManager.getUserPrivateList(uid),// 自己私密数据;
						TimelineKeyManager.getUserGroupContentsKey(uid),//组内分享数据
						//TimelineKeyManager.getUserForwardList(uid),// 自己转发数据;自己喜欢的不要显示
						TimelineKeyManager.getGroupTimelineKey(uid),// 好友组内分享推送数据
						TimelineKeyManager.getPublicTimelineKey(uid),// 公开推送的数据;
						TimelineKeyManager.getUserTimeLineKey(uid),// 拉取的数据;
						TimelineKeyManager.getEditorRecommendTopKey());//编辑推荐数据
			}
			connection.del(TimelineKeyManager.getUserQupaiList(uid));
			connection.close();
			relationTemplate.boundValueOps(TimelineKeyManager.getUserLastVisitTimelineTimeKey(uid)).set(String.valueOf(System.currentTimeMillis()));
		}
		Set actionSet = null;
		if (time > 0){
			actionSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).reverseRangeByScoreWithScores(time,Long.MAX_VALUE);
		}else{
			actionSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).reverseRangeWithScores(start, end);
		}
		
		return actionSet;
	}
	
	
	/**
	 * 获取关注用户创建的内容列表
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set loadCreateTimelineActions(long uid,int start,int end){
		UserModel user = redisUserService.getUser(uid);
		if (start == 0){
			DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
			//拉去名人关注数据
			pullFamousContent(connection,uid,"create");
			//拉取短趣君的数据
			if (redisRelationshipService.isFollowed(uid, 1)){//判断是否关注短趣君； 短趣君 id = 1
				Set temp = connection.zRangeByScoreWithScores(TimelineKeyManager.getUserPublicList(1), user.getCreateTime(), Long.MAX_VALUE);
				if (temp != null){
					Iterator it = temp.iterator();
					while (it.hasNext()) {
						DefaultStringTuple obj = (DefaultStringTuple) it.next();
						String value = obj.getValueAsString();
						long score = obj.getScore().longValue();
						connection.zAdd(TimelineKeyManager.getUserQupaiList(uid), score, value);
					}
				}
				int[] weights = {1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights,
						TimelineKeyManager.getUserQupaiList(uid),//  短趣君在用户注册以后发部的内容 id = 1
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
			
			//合并推的数据+自己的数据+编辑推荐的数据
			int[] weights = {1,1,1};
			connection.zUnionStore(TimelineKeyManager.getUserTimeLineKey(uid),
					Aggregate.MAX, weights,
					TimelineKeyManager.getGroupTimelineKey(uid),// 组内分享数据
					TimelineKeyManager.getUserFollowsCreateTimelineKey(uid),//公开推送（发布内容）
					TimelineKeyManager.getUserTimeLineKey(uid));// 拉取的数据;
			
			connection.del(TimelineKeyManager.getUserQupaiList(uid));
			connection.close();
		}
		Set actionSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).reverseRangeWithScores(start, end);
		return actionSet;
	}
	
	
	/**
	 * 获取好友的内容列表，采用全拉去的方式进行
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set loadFriendTimelineActions(long uid,int start,int end){
		if (start == 0){
			DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
			//获取好友用户
			userRelationTemplate.delete(FriendShipKeyManager.getUserFriendKey(uid));
			
			userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid))
					.intersectAndStore(FriendShipKeyManager.getFansKey(uid),
							FriendShipKeyManager.getUserFriendKey(uid));
			Set friendSet = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFriendKey(uid)).reverseRangeWithScores(start, end);
			Iterator it = friendSet.iterator();
			//拉去好友发布和转发数据
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				//获取组内分享数据
				int[] weights1 = { 1, 1};
				connection.zInterStore(
						TimelineKeyManager.getFriendGroupContents(uid),
						Aggregate.MAX, weights1,
						TimelineKeyManager.getUserGroupContentsKey(Long.parseLong(value)),// 好友组内分享数据;
						TimelineKeyManager.getGroupTimelineKey(uid));// 访问者接受组内分享推送的数据
				//公开+转发的数据
				int[] weights2 = {1,1,1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights2,
						TimelineKeyManager.getUserPublicList(Long.parseLong(value)),//公开发表的
						TimelineKeyManager.getUserForwardList(Long.parseLong(value)),//转发的
						TimelineKeyManager.getFriendGroupContents(uid),//好友组内分享
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
			connection.del(TimelineKeyManager.getFriendGroupContents(uid));//删除组内分享临时数据
			connection.close();
		}
		Set actionSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).reverseRangeWithScores(start, end);
		return actionSet;
	}
	
	/**
	 * 获取关注用户创建的内容列表
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set loadForwardTimelineActions(long uid,int start,int end){
		if (start == 0){
			DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
			//拉去名人好友数据
			pullFamousContent(connection,uid,"forward");
			//合并自己转发和收到的转发数据
			int[] weights = {1,1};
			connection.zUnionStore(TimelineKeyManager.getUserTimeLineKey(uid),
					Aggregate.MAX, weights,
					TimelineKeyManager.getUserFollowsForwardTimelineKey(uid),//公开推送（发布内容）
					TimelineKeyManager.getUserTimeLineKey(uid));// 拉取的数据;
			connection.del(TimelineKeyManager.getUserQupaiList(uid));
			connection.close();
		}
		Set actionSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserTimeLineKey(uid)).reverseRangeWithScores(start, end);
		return actionSet;
	}
	
	/**
	 * 拉去名人数据
	 * @param connection //redis链接
	 * @param uid //用户ID
	 * @param type	//需要拉去的列表类型（全部:all,好友：friend，转发：forward，创建：create）
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void pullFamousContent(DefaultStringRedisConnection connection, long uid, String type){
		
		//删除关注中的名人数据（以便更新）
		userRelationTemplate.delete(FriendShipKeyManager.getUserFollowFamousKey(uid));
		//1、取得关注列表中属于名人的用户列表
		userRelationTemplate.boundZSetOps(FriendShipKeyManager.getFollowKey(uid))
				.intersectAndStore(FriendShipKeyManager.getFamousUserKey(),
						FriendShipKeyManager.getUserFollowFamousKey(uid));
		//2、取出关注列表中所有名人
		Set followFamous = userRelationTemplate.boundZSetOps(FriendShipKeyManager.getUserFollowFamousKey(uid)).range(0, -1);
		//循环拉取名人内容
		if (followFamous != null && (type.equals("all") || type.equals("friend"))) {
			Iterator it = followFamous.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				int[] weights = {1,1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(Long.parseLong(value)),//公开发表的
						TimelineKeyManager.getUserForwardList(Long.parseLong(value)),//转发的
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
		}
		
		if (followFamous != null && type.equals("create")){
			Iterator it = followFamous.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				int[] weights = {1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(Long.parseLong(value)),//公开发表的
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
		}
		
		if (followFamous != null && type.equals("forward")){
			Iterator it = followFamous.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				int[] weights = {1,1};
				connection.zUnionStore(TimelineKeyManager
						.getUserTimeLineKey(uid), Aggregate.MAX, weights,
						TimelineKeyManager.getUserForwardList(Long.parseLong(value)),//公开发表的
						TimelineKeyManager.getUserTimeLineKey(uid));
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initTopEditorRecommendList(){
		Long size = relationTemplate.boundZSetOps(TimelineKeyManager.getEditorRecommendTopKey()).size();
		if (size == null || size == 0){
			Set set = relationTemplate.boundZSetOps(TimelineKeyManager.getEditorRecommendList()).reverseRangeWithScores(0, 19);
			if (set != null){
				Iterator it = set.iterator();
				while (it.hasNext()) {
					TypedTuple obj = (TypedTuple) it.next();
					String value = (String)obj.getValue();
					long score = obj.getScore().longValue();
					relationTemplate.boundZSetOps(TimelineKeyManager.getEditorRecommendTopKey()).add(value, score);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set getTopActions(long uid){
		//取出置顶内容，
		if (relationTemplate.boundZSetOps(HotContentKeyManager.getQupaiTopContentKey()).size()>0){
			Set topActionSet = relationTemplate.boundZSetOps(HotContentKeyManager.getQupaiTopContentKey()).reverseRangeWithScores(0, -1);
			
			if (topActionSet != null && topActionSet.size() > 0){
				//判断是否已经访问过，访问过直接把置顶内容插入 USER_PUBLIC_TIMELINE，所以只要判断改内容是否在USER_PUBLIC_TIMELINE存在
				TypedTuple obj = null;
				Iterator it = topActionSet.iterator();
				while(it.hasNext()){
					obj = (TypedTuple) it.next();
				}
				//判断是否已经访问过
				String value = (String)obj.getValue();
				Long index = relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).rank(value);
				//没访问过返回
				if (index == null ){
					//合并到 USER_PUBLIC_TIMELINE
					relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).add(value , obj.getScore());
					return topActionSet;
				}else{
					return null;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadOptUsers(long cid, int start, int end){
		relationTemplate.delete(ContentKeyManager.getContentOptUserKey(cid));
		Set keySet = new HashSet();
		keySet.add(CommentKeyManager.getContentCommentUsersKey(cid));
		keySet.add(ContentKeyManager.getContentForwardUserkey(cid));
		relationTemplate.boundZSetOps(
				ContentKeyManager.getContentLikeUserkey(cid)).unionAndStore(
				keySet, ContentKeyManager.getContentOptUserKey(cid));
		Set set = relationTemplate.boundZSetOps(ContentKeyManager.getContentOptUserKey(cid)).reverseRange(start, end);
		Iterator it = set.iterator();
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		while (it.hasNext()){
			String value = (String)it.next();
			String[] vals = value.split(":");//"uid:action" action = 0 喜欢 action = 1 comment
			try{
				long uid = Long.parseLong(vals[0]);
				UserModel model = redisUserService.getUser(uid);
				SimpleUserForm user = model.asSimpleUserForm();
				user.setActionType(Integer.parseInt(vals[1]));
				users.add(user);
			}catch(Exception e){
				logger.error("[Params:cid="+cid+",start="+start+",end="+end+"],Message="+e.getMessage());
			}
		}
		return users;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadOptUsersFromCache(long cid,int start,int end){
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		Object obj = relationTemplate.boundValueOps(ContentKeyManager.getOptUserCacheKey(cid)).get();
		if (obj != null){
			try{
				String json = (String)obj;
				users = JSON.parseArray(json, SimpleUserForm.class);
			}catch (Exception e) {
				logger.error("获取内容操作用户出错！"+e.getMessage()+"。e,"+e);
			}
		}else{
			Set keySet = new HashSet();
			keySet.add(CommentKeyManager.getContentCommentUsersKey(cid));
			keySet.add(ContentKeyManager.getContentForwardUserkey(cid));
			relationTemplate.boundZSetOps(
					ContentKeyManager.getContentLikeUserkey(cid)).unionAndStore(
					keySet, ContentKeyManager.getContentOptUserKey(cid));
			Set set = relationTemplate.boundZSetOps(ContentKeyManager.getContentOptUserKey(cid)).reverseRange(start, end);
			Iterator it = set.iterator();
			users = new ArrayList<SimpleUserForm>();
			while (it.hasNext()){
				String value = (String)it.next();
				String[] vals = value.split(":");//"uid:action" action = 0 喜欢 action = 1 comment
				try{
					long uid = Long.parseLong(vals[0]);
					UserModel model = redisUserService.getUser(uid);
					SimpleUserForm user = model.asSimpleUserForm();
					user.setActionType(Integer.parseInt(vals[1]));
					users.add(user);
				}catch(Exception e){
					logger.error("[Params:cid="+cid+",start="+start+",end="+end+"],Message="+e.getMessage());
				}
			}
			//加入缓存
			if (users.size() > 0){
				String json = JSON.toJSONString(users);
				relationTemplate.boundValueOps(ContentKeyManager.getOptUserCacheKey(cid)).set(json);
				//relationTemplate.expire(ContentKeyManager.getOptUserCacheKey(cid), 10, TimeUnit.MINUTES);
			}
		}
		return users;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshOptUsersCache(long cid){
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		Set keySet = new HashSet();
		keySet.add(CommentKeyManager.getContentCommentUsersKey(cid));
		keySet.add(ContentKeyManager.getContentForwardUserkey(cid));
		relationTemplate.boundZSetOps(
				ContentKeyManager.getContentLikeUserkey(cid)).unionAndStore(
				keySet, ContentKeyManager.getContentOptUserKey(cid));
		Set set = relationTemplate.boundZSetOps(ContentKeyManager.getContentOptUserKey(cid)).reverseRange(0, 17);
		Iterator it = set.iterator();
		users = new ArrayList<SimpleUserForm>();
		while (it.hasNext()){
			String value = (String)it.next();
			String[] vals = value.split(":");//"uid:action" action = 0 喜欢 action = 1 comment
			try{
				long uid = Long.parseLong(vals[0]);
				UserModel model = redisUserService.getUser(uid);
				SimpleUserForm user = model.asSimpleUserForm();
				user.setActionType(Integer.parseInt(vals[1]));
				users.add(user);
			}catch(Exception e){
				logger.error("[Params:cid="+cid+"],Message="+e.getMessage()+",e"+e);
			}
		}
		
		if (users.size() > 0){
			String json = JSON.toJSONString(users);
			relationTemplate.boundValueOps(ContentKeyManager.getOptUserCacheKey(cid)).set(json);
		}else{
			relationTemplate.delete(ContentKeyManager.getOptUserCacheKey(cid));
		}
	}
	
	public IRedisUserService getRedisUserService() {
		return redisUserService;
	}
	public IRedisContentService getRedisContentService() {
		return redisContentService;
	}
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}
	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteUserContentList(ActionModel action) {
		Long result = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(action.getUid())).remove(action.toString());
		if (result<=0){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserPrivateList(action.getUid())).remove(action.toString());
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserAtList(long uid, long cid) {
		ActionModel action = new ActionModel(uid, ActionModel.Action.AT.getMark(),cid);
		return relationTemplate.boundZSetOps(TimelineKeyManager.getUserAtListKey(uid)).add(action.toString(),System.currentTimeMillis());
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserForwardList(long uid, long cid) {
		ActionModel action = new ActionModel(uid, ActionModel.Action.FORWARD.getMark(),cid);
		return relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(uid)).add(action.toString(),System.currentTimeMillis());
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean insertUserShareList(long uid, long cid) {
		ActionModel action = new ActionModel(uid, ActionModel.Action.SHARE.getMark(),cid);
		return relationTemplate.boundZSetOps(TimelineKeyManager.getUserShareListKey(uid)).add(action.toString(),System.currentTimeMillis());
	}
	@Override
	public ActionForm getActionForm(ActionModel actionModel ,long uid,long time,boolean isFromCache) {
		ActionForm actionForm = new ActionForm();
		ContentForm contentForm = redisContentService.getContent(actionModel.getCid(), uid,isFromCache);
		if (actionModel.getAction() == ActionModel.Action.RECOMMEND.getMark() || contentForm.getRecommend() == 1){
			contentForm.setRecommend(1);
		}else{
			contentForm.setRecommend(0);
		}
		actionForm.setContent(contentForm);
		UserModel actionUser = null;
		if (actionModel.getAction() == ActionModel.Action.CREATE.getMark()){
			actionForm.setActionUser(contentForm.getUser());
		}else{
			actionUser = redisUserService.getUser(actionModel.getUid());
			actionForm.setActionUser(actionUser.asSimpleUserForm());
		}
		actionForm.setActionType(actionModel.getAction());
		actionForm.setTime(time == 0 ? contentForm.getUploadTime() : time);
		return actionForm;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public int countNewTimeline(long uid) {
		int count = 0;
		Object obj = relationTemplate.boundValueOps(
				TimelineKeyManager.getUserLastVisitTimelineTimeKey(uid)).get();
		if (obj != null){
			long time = Long.parseLong(String.valueOf(obj));
			// 公开推送过来的信息（创建的）
			int count1 = relationTemplate
					.boundZSetOps(TimelineKeyManager.getUserFollowsCreateTimelineKey(uid))
					.rangeByScore(time, Long.MAX_VALUE).size();
			// 公开推送过来的信息（转发的）
			int count3 = relationTemplate
					.boundZSetOps(TimelineKeyManager.getUserFollowsForwardTimelineKey(uid))
					.rangeByScore(time, Long.MAX_VALUE).size();
			
			// 组内分享过来的信息
			int count2 = relationTemplate
					.boundZSetOps(TimelineKeyManager.getGroupTimelineKey(uid))
					.rangeByScore(time, Long.MAX_VALUE).size();
			count = count1 + count2 + count3;
		}
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertTimelineTopAction(ActionModel action) {
		//置顶内容只有一条
		//1、合并置顶内容和公开内容列表
		DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
		int[] weights = {1,1};
		//短趣君 id = 1
		connection.zUnionStore(TimelineKeyManager.getUserPublicList(1),
				Aggregate.MAX, weights, HotContentKeyManager
						.getQupaiTopContentKey(), TimelineKeyManager
						.getUserPublicList(1));
		connection.close();
		//2、移除老内容；
		relationTemplate.delete(HotContentKeyManager.getQupaiTopContentKey());
		//3、插入新数据
		relationTemplate.boundZSetOps(HotContentKeyManager.getQupaiTopContentKey()).add(action.toString(),System.currentTimeMillis());
	}
	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}
	@Override
	public List<SimpleUserForm> loadAtUsers(Set<String> nickNames) {
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		for (String nickName :nickNames){
			UserModel user = redisUserService.getUserByNickName(nickName);
			if (user != null){
				users.add(user.asSimpleUserForm());
			}
		}
		return users;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteUserRevLikeTime(long uid, long cid,long actionUserId) {
		Double score = relationTemplate.boundZSetOps(TimelineKeyManager.getUserRevLikeTimeline(uid)).score(String.valueOf(cid));
		if (score != null && score.longValue() == actionUserId){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserRevLikeTimeline(uid)).remove(String.valueOf(cid));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertUserRevLikeTime(long uid, long cid,long actionUserId) {
		relationTemplate.boundZSetOps(TimelineKeyManager.getUserRevLikeTimeline(uid)).add(String.valueOf(cid),actionUserId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean isRevLikeTime(long uid, long cid) {
		Long index =  relationTemplate.boundZSetOps(TimelineKeyManager.getUserRevLikeTimeline(uid)).rank(String.valueOf(cid));
		return index != null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set loadUserPrivateContents(long uid, int start, int end) {
		Set privateSet = relationTemplate.boundZSetOps(
				TimelineKeyManager.getUserPrivateList(uid))
				.reverseRangeWithScores(start, end);
		return privateSet;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countUserPrivateContents(long uid) {
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPrivateList(uid)).size();
		return count == null?0:count.intValue();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void pushOneContentsToNewUser(long uid, int count) {
		Set createSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(1)).reverseRangeWithScores(0, 0);
		Iterator it = createSet.iterator();
		while (it.hasNext()) {
			TypedTuple obj = (TypedTuple) it.next();
			relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).add((String) obj.getValue(), obj.getScore().longValue());
		}

	}
	
}
