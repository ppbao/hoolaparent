package com.unisharing.hoola.hoolaredis.service.content;


import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.submit.Pager;
import com.unisharing.hoola.hoolacommon.utils.DateUtil;
import com.unisharing.hoola.hoolacommon.utils.HoolaStringUtils;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.ContentForm;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.key.ContentKeyManager;
import com.unisharing.hoola.hoolaredis.key.SearchKeyManager;
import com.unisharing.hoola.hoolaredis.key.TimelineKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Service("redisContentService")
public class RedisContentServiceImpl extends BaseRedisService implements IRedisContentService{
	
	Log logger = LogFactory.getLog(RedisContentServiceImpl.class);
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisTimelineService redisTimelineService;
	
	private final HashMapper<ContentModel, String, String> contentMapper = new DecoratingStringHashMapper<ContentModel>(
			new JacksonHashMapper<ContentModel>(ContentModel.class));
	
	@Override
	public ContentModel getContent(long cid) {
		return contentMapper.fromHash(getContent(String.valueOf(cid)));
	}

	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getContent(String cid) {
		return new DefaultRedisMap<String, String>(ContentKeyManager.getContentInfoKey(cid), contentTemplate);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ContentModel insertContent(ContentModel content) {
		if (content.getCid() == 0){
			RedisAtomicLong contentIdCounter = new RedisAtomicLong(ContentKeyManager.getContentIdKey(),super.contentTemplate.getConnectionFactory());
			content.setCid(contentIdCounter.incrementAndGet());
		}
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(content.getCid()+"")).putAll(contentMapper.toHash(content));
		//插入内容ID 和 key 的对应关系
		contentTemplate.boundValueOps(ContentKeyManager.getContentIdByKey(content.getKey())).set(String.valueOf(content.getCid()));
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentModel> loadContentsByLikeNum(int limit) {
		contentTemplate.boundSetOps("").union("");
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> loadUserContents(long uid, long visitUid, Pager pager) {
		Set<TypedTuple> set = null;
		int start = (pager.getPage() - 1) * pager.getPageSize();
		int end = (pager.getPage()) * pager.getPageSize() - 1;
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
				relationTemplate.getConnectionFactory().getConnection());
		// 查询自己的个人中心
		if (uid == visitUid) {
			if (pager.getPage() == 1) {
				relationTemplate.delete(TimelineKeyManager.getUserContentListKey(uid));
				int[] weights = {1,1,1};
				connection.zUnionStore(TimelineKeyManager.getUserContentListKey(uid),
						Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(uid),// 自身公开数据
						TimelineKeyManager.getUserPrivateList(uid),// 自己私密数据;
						TimelineKeyManager.getUserGroupContentsKey(uid));// 组内分享的
			}
			set = relationTemplate.boundZSetOps(TimelineKeyManager.getUserContentListKey(uid)).reverseRangeWithScores(start, end);
		} else {
			// 交集私密和组内分享的数据
			int[] weights = { 1, 1};
			connection.zInterStore(
					TimelineKeyManager.getOtherUserContentListKey(uid, visitUid),
					Aggregate.MAX, weights,
					TimelineKeyManager.getUserGroupContentsKey(uid),// 组内分享数据;
					TimelineKeyManager.getGroupTimelineKey(visitUid));// 访问者组内分享的
			//合并交集结果 和 公开内容
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(uid)).unionAndStore(TimelineKeyManager.getOtherUserContentListKey(uid, visitUid),
					TimelineKeyManager.getOtherUserContentListKey(uid, visitUid));
			relationTemplate.expire(TimelineKeyManager.getOtherUserContentListKey(uid, visitUid), 30, TimeUnit.SECONDS);
			set = relationTemplate.boundZSetOps(
					TimelineKeyManager.getOtherUserContentListKey(uid, visitUid))
					.reverseRangeWithScores(start, end);
		}
		connection.close();
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (set != null) {
			Iterator it = set.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				ActionModel actionModel = ActionModel.parse(value);
				long time = obj.getScore().longValue();
				ActionForm actionForm = redisTimelineService.getActionForm(actionModel, visitUid, time,false);
				if (actionForm.getContent().getcStatus() == ContentModel.Status.NORMAL.getMark()
						|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED.getMark()
								&& actionForm.getContent().getUser().getUid() == visitUid)){
					actions.add(actionForm);
				}
			}
		}
		return actions;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ActionForm> loadUserLikeContents(long uid, long visitUid,
			Pager pager) {
		int start = (pager.getPage() - 1) * pager.getPageSize();
		int end = (pager.getPage()) * pager.getPageSize() - 1;
		Set<TypedTuple> likeSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).reverseRangeWithScores(start, end);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (likeSet != null) {
			Iterator it = likeSet.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				ActionModel actionModel = ActionModel.parse(value);
				long time = obj.getScore().longValue();
				ActionForm actionForm = redisTimelineService.getActionForm(actionModel, visitUid, time,false);
				if (actionForm.getContent().getcStatus() == ContentModel.Status.NORMAL.getMark()
						|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED.getMark()
								&& actionForm.getContent().getUser().getUid() == visitUid)){
					actions.add(actionForm);
				}
			}
		}
		return actions;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> loadUserForwardContents(long uid, long visitUid,
			Pager pager) {
		int start = (pager.getPage() - 1) * pager.getPageSize();
		int end = (pager.getPage()) * pager.getPageSize() - 1;
		Set<TypedTuple> forwardSet = relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(uid)).reverseRangeWithScores(start, end);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (forwardSet != null) {
			Iterator it = forwardSet.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				ActionModel actionModel = ActionModel.parse(value);
				long time = obj.getScore().longValue();
				ActionForm actionForm = redisTimelineService.getActionForm(actionModel, visitUid, time,false);
				if (actionForm.getContent().getcStatus() == ContentModel.Status.NORMAL.getMark()
						|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED.getMark()
								&& actionForm.getContent().getUser().getUid() == visitUid)){
					actions.add(actionForm);
				}
			}
		}
		return actions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countUserContents(long uid){
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserContentListKey(uid)).size();
		return count == null ?0:count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countUserLikeContents(long uid){
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).size();
		return count == null ?0:count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countUserForwardContents(long uid){
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(uid)).size();
		return count == null ?0:count.intValue();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> loadUserAllContents(long uid, long visitUid,
			Pager pager) {
		Set<TypedTuple> set = null;
		int start = (pager.getPage() - 1) * pager.getPageSize();
		int end = (pager.getPage()) * pager.getPageSize() - 1;
		
		//if (pager.getPage() == 1) {
			DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
					relationTemplate.getConnectionFactory().getConnection());
			//删除列表数据
			relationTemplate.delete(TimelineKeyManager.getUserAllContentListKey(uid));
			// 查询自己的所有
			if (uid == visitUid) {
				// 合并 我公开的，私密的，喜欢的，分享的，@的，转发的
				int[] weights = { 1, 1, 1, 1, 1, 1 };
				connection.zUnionStore(
						TimelineKeyManager.getUserAllContentListKey(uid),
						Aggregate.MAX, weights,
						TimelineKeyManager.getUserPublicList(uid),// 自身公开数据
						TimelineKeyManager.getUserPrivateList(uid),// 自己私密数据;
						TimelineKeyManager.getUserLikeList(uid),// 自己喜欢数据;
						TimelineKeyManager.getUserShareListKey(uid),// 自己分享的；
						TimelineKeyManager.getUserForwardList(uid),// 自己转发的；
						TimelineKeyManager.getUserAtListKey(uid)// 自己At的；
						);
				set = relationTemplate.boundZSetOps(
						TimelineKeyManager.getUserAllContentListKey(uid))
						.reverseRangeWithScores(start, end);
				relationTemplate.expire(TimelineKeyManager.getUserAllContentListKey(uid), 60, TimeUnit.SECONDS);
			} else {
				int[] weights1 = { 1, 1};
				//交集 私密 和 组内分享数据
				connection.zInterStore(
						TimelineKeyManager.getOtherUserAllContentListKey(uid, visitUid),
						Aggregate.MAX, weights1,
						//TimelineKeyManager.getUserPrivateList(uid),//自己私有的
						TimelineKeyManager.getUserLikeList(uid),//喜欢的内容
						TimelineKeyManager.getGroupTimelineKey(visitUid));//组内分享的；
				int[] weights2 = { 1, 1};
				//合并其他数据
				connection.zUnionStore(
						TimelineKeyManager.getOtherUserAllContentListKey(uid, visitUid),
						Aggregate.MAX, weights2,
						//TimelineKeyManager.getUserPublicList(uid),// 自身公开数据
						//TimelineKeyManager.getUserLikeList(uid),// 自己喜欢数据;
						TimelineKeyManager.getUserForwardList(uid),//自己转发的
						TimelineKeyManager.getOtherUserAllContentListKey(uid, visitUid)//组内分享的；
						);
				set = relationTemplate.boundZSetOps(
						TimelineKeyManager.getOtherUserAllContentListKey(uid, visitUid))
						.reverseRangeWithScores(start, end);
				relationTemplate.expire(TimelineKeyManager.getOtherUserAllContentListKey(uid, visitUid), 60, TimeUnit.SECONDS);
			}
			connection.close();
		//}
		
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (set != null) {
			Iterator it = set.iterator();
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				ActionModel actionModel = ActionModel.parse(value);
				long time = obj.getScore().longValue();
				ActionForm actionForm = redisTimelineService.getActionForm(actionModel, visitUid, time,false);
				if (actionForm.getContent().getcStatus() == ContentModel.Status.NORMAL.getMark()
						|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED.getMark()
								&& actionForm.getContent().getUser().getUid() == visitUid)){
					actions.add(actionForm);
				}
			}
		}
		return actions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countUserAllContents(long uid){
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserAllContentListKey(uid)).size();
		return count == null ?0:count.intValue();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public List<FriendForm> loadLikeUsers(long cid, int start, int end) {
		List<FriendForm> friends = new ArrayList<FriendForm>();
		Set set = relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).range(start, end);
		Iterator it = set.iterator();
		while (it.hasNext()){
			String obj = (String)it.next();
			String[] values = obj.split(":");
			try{
				long uid = Long.parseLong(values[0]);
				UserModel model = redisUserService.getUser(uid);
				SimpleUserForm form = model.asSimpleUserForm();
				FriendForm friend = new FriendForm();
				friend.setUser(form);
				friends.add(friend);
			}catch (Exception e) {
				logger.error("loadLikeUsers Error;"+e.getMessage());
			}
			
		}
		return friends;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countLikeUsers(long cid) {
		Long count = relationTemplate.boundZSetOps(
				ContentKeyManager.getContentLikeUserkey(cid)).size();
		return count == null ? 0 : count.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateCommentNum(long cid, int count) {
		if (count <= 0) {
			count = 0;
		}
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("commentNum", String.valueOf(count));
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateLikeNum(long cid, int count) {
		if (count <= 0) {
			count = 0;
		}
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("likeNum", String.valueOf(count));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateForwardNum(long cid, int count) {
		if (count <= 0) {
			count = 0;
		}
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("forwardNum", String.valueOf(count));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updatePlayNum(long cid) {
		ContentModel content = this.getContent(cid);
		if (content != null && content.getCid() >0){
			Long num = contentTemplate.boundValueOps(ContentKeyManager.getContentPlayNumKey(cid)).increment(1);
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(cid+"")).put("showTimes", String.valueOf(num));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isLiked(long cid, long uid) {
		String obj = uid+":"+ ActionModel.Action.LIKE.getMark();//uid:2
		Long index = relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).rank(String.valueOf(obj));
		return index != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void likeContent(long cid, long uid) {
		//插入喜欢列表
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.LIKE.getMark());
		model.setCid(cid);
		model.setUid(uid);
		boolean  bool = relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).add(model.toString(), System.currentTimeMillis());
		if (bool){
			//插入内容喜欢用户列表
			String obj = uid + ":" + ActionModel.Action.LIKE.getMark();
			bool = relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).add(obj, System.currentTimeMillis());
			Long count = contentTemplate.boundValueOps(ContentKeyManager.getContentLikeNumKey(cid)).increment(1);
			this.updateLikeNum(cid, count.intValue());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void disLikeContent(long cid, long uid) {
		//插入喜欢列表
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.LIKE.getMark());
		model.setCid(cid);
		model.setUid(uid);
		Long  result = relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).remove(model.toString());
		if (result>0){
			//插入内容喜欢用户列表
			String obj = uid + ":" + ActionModel.Action.LIKE.getMark();
			result = relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).remove(obj);
			Long count = contentTemplate.boundValueOps(ContentKeyManager.getContentLikeNumKey(cid)).increment(-1);
			this.updateLikeNum(cid, count.intValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addCommentUser(long cid, long uid) {
		//插入内容评论用户列表
		String obj = uid + ":" + ActionModel.Action.COMMENT.getMark();
		relationTemplate.boundZSetOps(ContentKeyManager.getContentCommentUserKey(cid)).add(obj, System.currentTimeMillis());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateContentStatus(long cid, int status) {
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("cStatus", String.valueOf(status));
	}
	
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	@Override
	public void deleteContentFromList(long uid, long cid) {
		ActionModel action = new ActionModel();
		action.setAction(ActionModel.Action.CREATE.getMark());
		action.setCid(cid);
		action.setUid(uid);
		redisTimelineService.deleteUserContentList(action);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> listContents(long uid,int start,int end) {
		List list = contentTemplate.boundListOps(ContentKeyManager.getContentListKey()).range(start, end);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (list != null) {
			for (Object obj :list) {
				try{
					long cid = Long.parseLong((String)obj);
					ActionModel actionModel = new ActionModel();
					actionModel.setAction(ActionModel.Action.CREATE.getMark());
					actionModel.setCid(cid);
					actionModel.setUid(uid);
					ActionForm actionForm = redisTimelineService.getActionForm(actionModel, uid, 0,false);
					ContentForm form = actionForm.getContent();
					if (form.getcStatus() == 0
							|| (form.getcStatus() == ContentModel.Status.SHIELDED
									.getMark() && form.getUser().getUid() == uid)) {
						actions.add(actionForm);
					}
				}catch(Exception e){
					logger.error("获取最新内容列表失败！,Message:"+e.getMessage());
				}
			}
		}
		return actions;
	}
	@SuppressWarnings("unchecked")
	public void buildTagIndex(long cid, Set<String> tags) {
		for (String tag : tags) {
			contentTemplate.boundSetOps(SearchKeyManager.getContentIndexKey(tag)).add(String.valueOf(cid));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void delTagIndex(String tag,long cid){
		contentTemplate.boundSetOps(SearchKeyManager.getContentIndexKey(tag)).remove(String.valueOf(cid));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertContentList(long cid) {
		contentTemplate.boundListOps(ContentKeyManager.getContentListKey()).leftPush(String.valueOf(cid));
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertContentFindList(long cid,int recommend) {
		contentTemplate.boundListOps(ContentKeyManager.getContentFindListKey()).remove(0, String.valueOf(cid));
		contentTemplate.boundListOps(ContentKeyManager.getContentFindListKey()).leftPush(String.valueOf(cid));
		if (recommend == 1){
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("recommend", "1");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> loadFindContentList(long uid, int start, int end) {
		List list = contentTemplate.boundListOps(ContentKeyManager.getContentFindListKey()).range(start, end);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		// 循环取出内容详细信息
		if (list != null) {
			for (Object obj :list) {
				try{
					long cid = Long.parseLong((String)obj);
					ActionModel actionModel = new ActionModel();
					actionModel.setAction(ActionModel.Action.CREATE.getMark());
					actionModel.setCid(cid);
					actionModel.setUid(0);
					ActionForm actionForm = redisTimelineService.getActionForm(actionModel, uid, 0, false);
					//过滤删除处理
					if (actionForm.getContent().getcStatus() == ContentModel.Status.NORMAL.getMark()
							|| (actionForm.getContent().getcStatus() == ContentModel.Status.SHIELDED.getMark()
									&& actionForm.getContent().getUser().getUid() == uid)){
						actions.add(actionForm);
					}
					
				}catch(Exception e){
					logger.error("获取最新内容列表失败！,Message:"+e.getMessage());
				}
			}
		}
		return actions;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countContentList() {
		Long count = contentTemplate.boundListOps(
				ContentKeyManager.getContentListKey()).size();
		return count == null ? 0 : count.intValue();
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countFindContent() {
		Long count = contentTemplate.boundListOps(
				ContentKeyManager.getContentFindListKey()).size();
		return count == null ? 0 : count.intValue();
	}
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public List<Long> loadAllContentId() {
		List<Long> contentIds = new ArrayList<Long>();
		Set keys = contentTemplate.keys("hm:content:14304:data");
		Iterator it = keys.iterator();
		while(it.hasNext()){
			ContentModel model = contentMapper.fromHash(new DefaultRedisMap<String, String>((String)it.next(), contentTemplate));
			if (model.getCid() > 0){
				contentIds.add(model.getCid());
			}
		}
		return contentIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isForwarded(long cid, long uid) {
		String obj = uid+":"+ ActionModel.Action.FORWARD.getMark();//uid:2
		Long index = relationTemplate.boundZSetOps(ContentKeyManager.getContentForwardUserkey(cid)).rank(String.valueOf(obj));
		return index != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void forwardContent(long cid, long uid) {
		// 插入我的转发列表
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.FORWARD.getMark());
		model.setCid(cid);
		model.setUid(uid);
		boolean bool = relationTemplate.boundZSetOps(
				TimelineKeyManager.getUserForwardList(uid)).add(model.toString(),
				System.currentTimeMillis());
		if (bool) {
			// 插入内容转发用户列表
			String obj = uid + ":" + ActionModel.Action.FORWARD.getMark();
			bool = relationTemplate.boundZSetOps(
					ContentKeyManager.getContentForwardUserkey(cid)).add(obj,
					System.currentTimeMillis());
			//更新转发数
			Long count = contentTemplate.boundValueOps(
					ContentKeyManager.getContentForwardNumKey(cid)).increment(1);
			this.updateForwardNum(cid, count.intValue());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void cancelForwardContent(long cid, long uid) {
		//删除用户转发列表
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.FORWARD.getMark());
		model.setCid(cid);
		model.setUid(uid);
		Long result  = relationTemplate.boundZSetOps(
				TimelineKeyManager.getUserForwardList(uid)).remove(model.toString());
		if (result>0) {
			// 删除内容转发用户列表
			String obj = uid + ":" + ActionModel.Action.FORWARD.getMark();
			result = relationTemplate.boundZSetOps(
					ContentKeyManager.getContentForwardUserkey(cid)).remove(obj);
			//更新转发数
			Long count = contentTemplate.boundValueOps(
					ContentKeyManager.getContentForwardNumKey(cid)).increment(-1);
			this.updateForwardNum(cid, count.intValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertShareContentList(long uid, long cid) {
		ActionModel action = new ActionModel();
		action.setAction(ActionModel.Action.SHARE.getMark());
		action.setCid(cid);
		action.setUid(uid);
		relationTemplate.boundZSetOps(TimelineKeyManager.getUserShareListKey(uid)).add(action.toString(), System.currentTimeMillis());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<FriendForm> loadForwardUsers(long cid, int start, int end) {
		List<FriendForm> friends = new ArrayList<FriendForm>();
		Set set = relationTemplate.boundZSetOps(ContentKeyManager.getContentForwardUserkey(cid)).range(start, end);
		Iterator it = set.iterator();
		while (it.hasNext()){
			String obj = (String)it.next();
			String[] values = obj.split(":");
			try{
				long uid = Long.parseLong(values[0]);
				UserModel model = redisUserService.getUser(uid);
				SimpleUserForm form = model.asSimpleUserForm();
				FriendForm friend = new FriendForm();
				friend.setUser(form);
				friends.add(friend);
			}catch (Exception e) {
				logger.error("loadLikeUsers Error;"+e.getMessage());
			}
			
		}
		return friends;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countForwardUsers(long cid) {
		Long count = relationTemplate.boundZSetOps(
				ContentKeyManager.getContentForwardUserkey(cid)).size();
		return count == null ? 0 : count.intValue();
	}

	@Override
	public long getContentId() {
		RedisAtomicLong contentIdCounter = new RedisAtomicLong(
				ContentKeyManager.getContentIdKey(),
				super.contentTemplate.getConnectionFactory());
		return contentIdCounter.incrementAndGet();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteContentFindList(long cid) {
		contentTemplate.boundListOps(ContentKeyManager.getContentFindListKey()).remove(0, String.valueOf(cid));
		//取消“荐”标识
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("recommend","0");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertEditorRecommend(long cid) {
		
		ContentModel content = this.getContent(cid);
		if (content != null && content.getCid() > 0){
			ActionModel action = new ActionModel();
			action.setAction(ActionModel.Action.RECOMMEND.getMark());
			action.setCid(cid);
			action.setUid(content.getUid());
			relationTemplate.boundZSetOps(
					TimelineKeyManager.getEditorRecommendList()).add(
					action.toString(), System.currentTimeMillis());
			//添加“荐”
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("recommend","1");
			refleshRecommend();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteEditorRecommend(long cid) {
		ContentModel content = this.getContent(cid);
		if (content != null && content.getCid() > 0){
			ActionModel action = new ActionModel();
			action.setAction(ActionModel.Action.RECOMMEND.getMark());
			action.setCid(cid);
			action.setUid(content.getUid());
			relationTemplate.boundZSetOps(
					TimelineKeyManager.getEditorRecommendList()).remove(action.toString());
			//取消“荐”
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("recommend","0");
			
			refleshRecommend();
		}
	}
	
	/**
	 * 刷新推荐内容区-- //最新的20条显示给用户
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void refleshRecommend(){
		relationTemplate.delete(TimelineKeyManager.getEditorRecommendTopKey());
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

	@SuppressWarnings("unchecked")
	@Override
	public long getCid(String key) {
		String id = (String)contentTemplate.boundValueOps(ContentKeyManager.getContentIdByKey(key)).get();
		try{
			long cid = Long.parseLong(id);
			return cid;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public ContentForm getContent(long cid, long uid, boolean isFromCache) {
		//long beginTime = System.currentTimeMillis();
		ContentModel model = this.getContent(cid);
		ContentForm form = model.asContentFormForClient();
		boolean isLiked = redisContentService.isLiked(form.getCid(), uid);
		boolean isForward = redisContentService.isForwarded(form.getCid(), uid);
		if (uid == form.getUser().getUid()){
			form.setLike(false);
		}else{
			form.setLike(isLiked);
		}
		if (uid == form.getUser().getUid()){
			form.setForward(false);
		}else{
			form.setForward(isForward);
		}
		
		if (uid == form.getUser().getUid()){
			form.setLikeAndForward(false);
		}else{
			form.setLikeAndForward(isLiked || isForward);
		}
		//全部从缓存取 - 在改变时更新
		form.setOptUsers(redisTimelineService.loadOptUsersFromCache(form.getCid(),0, 17));
		/*if (isFromCache){
			form.setOptUsers(redisTimelineService.loadOptUsersFromCache(form.getCid(),0, 17));
		}else{
			form.setOptUsers(redisTimelineService.loadOptUsers(form.getCid(),0, 17));
		}*/
		SimpleUserForm author = redisUserService.getUser(form.getUser().getUid()).asSimpleUserForm();
		
		form.setAtUsers(this.loadAtUserFromCache(model));
		
		//long endTime = System.currentTimeMillis();
		//System.out.println("======================getContent"+(endTime - beginTime));
		form.setUser(author);
		return form;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateContentMD5(long cid, String md5) {
		contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("md5", md5);
	}
	@SuppressWarnings("unchecked")
	@Override
	public long getCidFromQueue() {
		Object obj = contentTemplate.boundListOps(ContentKeyManager.getContentIdQueueKey()).leftPop();
		try{
			long cid = Long.parseLong(String.valueOf(obj));
			return cid;
		}catch (Exception e) {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertCidIntoQueue(long cid){
		contentTemplate.boundListOps(ContentKeyManager.getContentIdQueueKey()).rightPush(String.valueOf(cid));
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateDescription(long cid, String description) {
		ContentModel content = this.getContent(cid);
		if (content != null && content.getCid()>0){
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("description", description);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set loadContentLikes(long cid) {
		return relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).reverseRangeWithScores(0, -1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set loadContentForwards(long cid) {
		return relationTemplate.boundZSetOps(ContentKeyManager.getContentForwardUserkey(cid)).reverseRangeWithScores(0, -1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateFriendsShareNum(long cid,int count) {
		if (count > 0){
			Long num = contentTemplate.boundValueOps(ContentKeyManager.getFriendsShareNumKey(cid)).increment(count);
			if (num > 0){
				contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("friendsShareNum", String.valueOf(num));
			}
			return num.intValue();
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int updateSinaShareNum(long cid,int count) {
		if (count > 0){
			Long num = contentTemplate.boundValueOps(ContentKeyManager.getSinaShareNumKey(cid)).increment(count);
			if (num > 0){
				contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(String.valueOf(cid))).put("sinaShareNum", String.valueOf(num));
			}
			return num.intValue();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void cancelLikeAndForwardContent(long uid, long cid) {
		//注意：插入用户转发列表，但是Action.Type = Like （主要是因为动态页面合并了转发列表，而小图标需要显示喜欢图标）
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.LIKE.getMark());
		model.setCid(cid);
		model.setUid(uid);
		Long  result = relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(uid)).remove(model.toString());
		if (result>0){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).remove(model.toString());
			//删除内容喜欢用户列表
			String obj = uid + ":" + ActionModel.Action.LIKE.getMark();
			result = relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).remove(obj);
			Long count = contentTemplate.boundValueOps(ContentKeyManager.getContentLikeNumKey(cid)).increment(-1);
			this.updateLikeNum(cid, count.intValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void likeAndForwardContent(long uid, long cid) {
		//注意：插入用户转发列表，但是Action.Type = Like （主要是因为动态页面合并了转发列表，而小图标需要显示喜欢图标）
		ActionModel model = new ActionModel();
		model.setAction(ActionModel.Action.LIKE.getMark());
		model.setCid(cid);
		model.setUid(uid);
		ContentModel content = this.getContent(cid);
		if (content.getIsPrivate() == 0){
			relationTemplate.boundZSetOps(TimelineKeyManager.getUserForwardList(uid)).add(model.toString(), System.currentTimeMillis());
		}
		relationTemplate.boundZSetOps(TimelineKeyManager.getUserLikeList(uid)).add(model.toString(), System.currentTimeMillis());
		//插入内容喜欢用户列表
		String obj = uid + ":" + ActionModel.Action.LIKE.getMark();
		relationTemplate.boundZSetOps(ContentKeyManager.getContentLikeUserkey(cid)).add(obj, System.currentTimeMillis());
		Long count = contentTemplate.boundValueOps(ContentKeyManager.getContentLikeNumKey(cid)).increment(1);
		this.updateLikeNum(cid, count.intValue());
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countUserPrivateContents(long uid) {
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPrivateList(uid)).size();
		if (count != null){
			return count.intValue();
		}else{
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countUserPublicContents(long uid) {
		Long count = relationTemplate.boundZSetOps(TimelineKeyManager.getUserPublicList(uid)).size();
		if (count != null){
			return count.intValue();
		}else{
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<SimpleUserForm> loadAtUserFromCache(ContentModel model){
		Object obj = contentTemplate.boundValueOps(ContentKeyManager.getAtUserCacheKey(model.getCid())).get();
		List<SimpleUserForm> atUsers = new ArrayList<SimpleUserForm>();
		if (obj != null){
			try{
				atUsers = JSON.parseArray((String)obj, SimpleUserForm.class);
			}catch(Exception e){
				logger.error("loadAtUserFromCache 数据格式转化错误！ String to list"+e);
			}
		}else{
			atUsers = redisTimelineService.loadAtUsers(HoolaStringUtils.getNickNames(model.getDescription()));
			if (atUsers != null){
				try{
					String json = JSON.toJSONString(atUsers);
					contentTemplate.boundValueOps(ContentKeyManager.getAtUserCacheKey(model.getCid())).set(json);
				}catch(Exception e){
					logger.error("loadAtUserFromCache 数据格式转化错误！ List to String"+e);
				}
			}
		}
		return atUsers;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<TypedTuple> loadUser24HoursContents(long uid) {
		Set<TypedTuple> actions = relationTemplate.boundZSetOps(TimelineKeyManager.getPublicTimelineKey(uid)).reverseRangeByScoreWithScores(DateUtil.getBefor24HoursTime(), Long.MAX_VALUE);
		return actions;
	}

	public int saveTmpVideo(String key ,String data){
		return jmsTemplate.boundValueOps(key).append(data);
	}
	
	public String getTmpVideo(String key){
		return (String)jmsTemplate.boundValueOps(key).get();
	}
}
