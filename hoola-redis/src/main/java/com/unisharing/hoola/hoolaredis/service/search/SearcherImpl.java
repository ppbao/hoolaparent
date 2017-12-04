package com.unisharing.hoola.hoolaredis.service.search;


import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.key.SearchKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;



import java.util.*;

public class SearcherImpl extends BaseRedisService implements ISearcher {
	
	IRedisContentService redisContentService;
	
	IRedisUserService redisUserService;
	
	IRedisTimelineService redisTimelineService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> searchSuggest(String key) {
		Set<String> suggests = new HashSet<String>();
		if(key!=null&&(!"".equals(key)))

		{
			Long index = contentTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).rank(key+SearchKeyManager.getSymbole());
			if (index == null){
				index = contentTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).rank(key);
				if (index != null){
					Set<String> keys = contentTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).range(index, index + 100);
					for (String a :keys){
						if (a.endsWith(SearchKeyManager.getSymbole())){
							suggests.add(a.substring(0,a.length()-1));
						}
					}
				}
			}
			suggests.add(key);
		}
		return suggests;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> searchTagContentsOrderByUploadTime(long uid, String key, int start, int end) {
		if (start == 0){
			Set<String> tags = searchSuggest(key);//通过
			List<String> keys = new ArrayList<String>();
			for (String tag : tags){
				keys.add(SearchKeyManager.getContentIndexKey(tag));
			}
			contentTemplate.boundSetOps(SearchKeyManager.getContentIndexKey("")).unionAndStore(keys, SearchKeyManager.getResultKey(key));
		}
		
		SortQuery query = SortQueryBuilder.sort(SearchKeyManager.getResultKey(key)).by("hm:content:*:data->uploadTime").limit(start, end-start).order(Order.DESC).build();
		List<String> ids = contentTemplate.sort(query);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		for (String id : ids){
			ActionForm action = new ActionForm();
			ContentModel model = redisContentService.getContent(Long.parseLong(id));
			if ((model != null && model.getcStatus()== ContentModel.Status.NORMAL.getMark())
					|| (model.getcStatus() == ContentModel.Status.SHIELDED.getMark() && model.getUid() == uid)){
				ActionModel actionModel = new ActionModel();
				actionModel.setAction(ActionModel.Action.CREATE.getMark());
				actionModel.setCid(model.getCid());
				actionModel.setUid(uid);
				action = redisTimelineService.getActionForm(actionModel, uid, 0, false);
				actions.add(action);
			}
		}
		return actions;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> searchUserSuggest(String key) {
		Set<String> suggests = new HashSet<String>();
        if(key!=null&&(!"".equals(key))){
			Long index = userTemplate.boundZSetOps(SearchKeyManager.getUserSuggestKey()).rank(key+SearchKeyManager.getSymbole());
			if (index == null){
				index = userTemplate.boundZSetOps(SearchKeyManager.getUserSuggestKey()).rank(key);
				if (index != null){
					Set<String> keys = userTemplate.boundZSetOps(SearchKeyManager.getUserSuggestKey()).range(index, index + 100);
					for (String a :keys){
						if (a.endsWith(SearchKeyManager.getSymbole())){
							suggests.add(a.substring(0,a.length()-1));
						}
					}
				}
			}
			suggests.add(key);
		}
		return suggests;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> searchUser(String key, int start, int end) {
		if (start == 0){
			Set<String> nickNames = searchUserSuggest(key);
			List<String> keys = new ArrayList<String>();
			for (String nickName : nickNames){
				keys.add(SearchKeyManager.getUserNicknameKey(nickName));
			}
			userTemplate.boundSetOps(SearchKeyManager.getUserResultKey("")).unionAndStore(keys, SearchKeyManager.getUserResultKey(key));
		}
		
		SortQuery query = SortQueryBuilder.sort(SearchKeyManager.getUserResultKey(key)).by("hm:user:*:data->createtime").limit(start, end-start).order(Order.DESC).build();
		List<String> ids = userTemplate.sort(query);
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		for (String uid : ids) {
			users.add(redisUserService.getUser(Long.parseLong(uid)).asSimpleUserForm());
		}
		return users;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countContent(String key) {
		return contentTemplate.boundSetOps(SearchKeyManager.getResultKey(key)).size().intValue();
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> searchTagContentsOrderByLikeNum(long uid, String key,
			int start, int end) {
		if (start == 0){
			Set<String> tags = searchSuggest(key);//通过
			List<String> keys = new ArrayList<String>();
			for (String tag : tags){
				keys.add(SearchKeyManager.getContentIndexKey(tag));
			}
			contentTemplate.boundSetOps(SearchKeyManager.getContentIndexKey("")).unionAndStore(keys, SearchKeyManager.getResultKey(key));
		}
		
		SortQuery query = SortQueryBuilder.sort(SearchKeyManager.getResultKey(key)).by("hm:content:*:data->likeNum").limit(start, end-start).order(Order.DESC).build();
		List<String> ids = contentTemplate.sort(query);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		for (String id : ids){
			ActionForm action = new ActionForm();
			ContentModel model = redisContentService.getContent(Long.parseLong(id));
			if ((model != null && model.getcStatus()== ContentModel.Status.NORMAL.getMark())
					|| (model.getcStatus() == ContentModel.Status.SHIELDED.getMark() && model.getUid() == uid)){
				ActionModel actionModel = new ActionModel();
				actionModel.setAction(ActionModel.Action.CREATE.getMark());
				actionModel.setCid(model.getCid());
				actionModel.setUid(uid);
				action = redisTimelineService.getActionForm(actionModel, uid, 0, false);
				actions.add(action);
			}
		}
		Collections.sort(actions);
		return actions;
	}

}
