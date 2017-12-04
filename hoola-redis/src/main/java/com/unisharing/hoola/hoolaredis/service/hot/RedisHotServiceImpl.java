package com.unisharing.hoola.hoolaredis.service.hot;


import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.*;
import com.unisharing.hoola.hoolaredis.key.HotContentKeyManager;
import com.unisharing.hoola.hoolaredis.key.SystemKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisHotServiceImpl extends BaseRedisService implements IRedisHotService {

	Log logger = LogFactory.getLog(RedisHotServiceImpl.class);
	
	IRedisRelationshipService redisRelationshipService;
	IRedisUserService redisUserService;
	IRedisContentService redisContentService;
	IRedisTimelineService redisTimelineService;
	IRedisHotService redisHotService;
	
	private final HashMapper<UserRecommendModel, String, String> recommendUserMapper = new DecoratingStringHashMapper<UserRecommendModel>(
			new JacksonHashMapper<UserRecommendModel>(UserRecommendModel.class));
	
	private final HashMapper<SubjectModel, String, String> subjectMapper = new DecoratingStringHashMapper<SubjectModel>(
			new JacksonHashMapper<SubjectModel>(SubjectModel.class));
	
	@SuppressWarnings("unchecked")
	@Override
	public void addBanner(List<BannerInfoModel> banners) {
		long count = jmsTemplate.boundListOps(HotContentKeyManager.getTopAdvKey()).size();
		for (BannerInfoModel banner : banners){
			try{
				String json = JSON.toJSONString(banner);
				if (json != null){
					jmsTemplate.boundListOps(HotContentKeyManager.getTopAdvKey()).rightPush(json);
				}
			}catch(Exception e){
				logger.error("同步广告条出错！"+e.getMessage()+",e="+e);
			}
		}
		long total = jmsTemplate.boundListOps(HotContentKeyManager.getTopAdvKey()).size();
		//删除老数据
		jmsTemplate.boundListOps(HotContentKeyManager.getTopAdvKey()).trim(count, total - 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addHotTag(EditorTagForm tagForm) {
		try{
			String json = JSON.toJSONString(tagForm);
			if (json != null){
				jmsTemplate.boundZSetOps(HotContentKeyManager.getTopTagKey()).add(json, System.currentTimeMillis());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addMainHotTag(EditorTagForm tagForm) {
		try{
			String json = JSON.toJSONString(tagForm);
			if (json != null){
				jmsTemplate.boundZSetOps(HotContentKeyManager.getMainTopTagKey()).add(json, tagForm.getOrderNum());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BannerForm> loadBanners() {
		List<BannerForm> banners = new ArrayList<BannerForm>();
		List list = jmsTemplate.boundListOps(HotContentKeyManager.getTopAdvKey()).range(0, -1);
		if (list != null){
			Iterator it = list.iterator();
			while (it.hasNext()){
				String json = (String)it.next();
				try{
					BannerInfoModel bannerModel = JSON.parseObject(json, BannerInfoModel.class);
					banners.add(bannerModel.asForm());
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return banners;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<EditorTagForm> loadHotTags(int start, int end) {
		List<EditorTagForm> tags = new ArrayList<EditorTagForm>();
		Set set = jmsTemplate.boundZSetOps(HotContentKeyManager.getTopTagKey()).reverseRange(start, end);
		if (set != null){
			Iterator it = set.iterator();
			while (it.hasNext()){
				String json = (String)it.next();
				try{
					EditorTagForm tag = JSON.parseObject(json, EditorTagForm.class);
					tags.add(tag);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return tags;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<EditorTagForm> loadMainHotTags() {
		List<EditorTagForm> tags = new ArrayList<EditorTagForm>();
		Set set = jmsTemplate.boundZSetOps(HotContentKeyManager.getMainTopTagKey()).range(0,-1);
		if (set != null){
			Iterator it = set.iterator();
			while (it.hasNext()){
				String json = (String)it.next();
				try{
					EditorTagForm tag = JSON.parseObject(json, EditorTagForm.class);
					tags.add(tag);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return tags;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> loadTags(int start, int end) {
		List<String> tags = new ArrayList<String>();
		List list = jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).range(start, end);
		if (list != null){
			for (Object obj : list){
				String tag = (String)obj;
				tags.add(tag);
			}
		}
		return tags;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addTag(List<String> tagList) {
		Long count = jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).size();
		if (tagList != null){
			for (String tag:tagList){
				try{
					jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).rightPush(tag);
				}catch (Exception e) {
					logger.error("添加编辑推荐标签出错！Message="+e.getMessage()+",e="+e);
				}
			}
			Long total = jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).size();
			if (total != null){
				jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).trim(count, total - 1);
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addRecommendUser(UserRecommendModel recUser) {
		try{
			if (recUser != null && recUser.getUid()>0){
				if (recUser.getType() == 1){//编辑推荐好友（好友菜单中的推荐好友）
					jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).add(String.valueOf(recUser.getUid()), System.currentTimeMillis());
				}else{ //达人用户
					jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendTalentListKey()).add(String.valueOf(recUser.getUid()), System.currentTimeMillis());
				}
				//添加推荐好友信息
				this.insertRecommendUserData(recUser);
			}
			
		}catch (Exception e) {
			logger.error("添加编辑推荐用户出错！Message="+e.getMessage()+",e="+e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateRecommendReason(UserRecommendModel userRecommendModel) {
		try {
			if(userRecommendModel!=null && userRecommendModel.getUid()>0){
					jmsTemplate.boundHashOps(
							HotContentKeyManager.getRecommendUserDataKey(userRecommendModel.getUid())).putAll(
							recommendUserMapper.toHash(userRecommendModel));
			}
		} catch (Exception e) {
			logger.error("修改推荐理由出错！Message="+e.getMessage()+",e="+e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteRecommendUser(UserRecommendModel recUser){
		try{
			if (recUser != null && recUser.getUid()>0){
				if (recUser.getType() == 1){//编辑推荐好友（好友菜单中的推荐好友）
					jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).remove(String.valueOf(recUser.getUid()));
				}else{
					jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendTalentListKey()).remove(String.valueOf(recUser.getUid()));
				}
				//删除推荐好友信息
				this.deleteRecommendUserData(recUser);
			}
			
		}catch (Exception e) {
			logger.error("删除编辑推荐用户出错！Message="+e.getMessage()+",e="+e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<SimpleUserForm> loadRecommendUsers(long uid) {
		List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
		int count = 0;
		while(users.size()<50){
			Set set = jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendUserKey()).reverseRange(count, count + 50);
			if (set != null && set.size() > 0){
				Iterator it = set.iterator();
				while (it.hasNext()){
					String uidStr = (String)it.next();
					try{
						long userid = Long.parseLong(uidStr);
						if (!redisRelationshipService.isFollowed(uid, userid) && uid != userid){
							UserRecommendModel recUser = redisHotService.getRecommendUser(userid);
							if (recUser != null && recUser.getUid()>0){
								UserModel user = redisUserService.getUser(userid);
								if (user != null && user.getUid()>0 && user.getUid() != uid){
									user.setSignature(recUser.getReason());//用用户的签名信息存储推荐理由
									users.add(user.asSimpleUserForm());
								}
							}
						}
					}catch (Exception e) {
						logger.error("查询编辑推荐用户列表出错！Message="+e.getMessage()+",e="+e);
					}
				}
			}else{
				return users;
			}
			count += 50;
		}
		return users;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertHotUser(List<Long> uids) {
		int count = this.countHotUser();
		for(Long uid:uids){
			jmsTemplate.boundListOps(HotContentKeyManager.getTopUserKey()).rightPush(String.valueOf(uid));
		}
		int total = this.countHotUser();
		if (count > 0){
			jmsTemplate.boundListOps(HotContentKeyManager.getTopUserKey()).trim(count, total -1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertHotContent(List<Long> cids,int type) {
		String key = getHotContentListKey(type);
		if (key != null){
			int count = this.countHotContent(type);//插入前数量
			for(Long cid:cids){
				jmsTemplate.boundListOps(key).rightPush(String.valueOf(cid));
			}
			int total = this.countHotContent(type); //插入后数量
			if (count > 0){
				jmsTemplate.boundListOps(key).trim(count, total - 1);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TalentListForm> loadHotUser(long uid, int start, int end) {
		List<TalentListForm> users = new ArrayList<TalentListForm>();
		List list = jmsTemplate.boundListOps(HotContentKeyManager.getTopUserKey()).range(start, end);
		if (list != null){
			for (Object id: list){
				try{
					Long userId = Long.parseLong((String)id);
					TalentListForm listItem = new TalentListForm();
					UserModel model = redisUserService.getUser(userId);
					TalentForm user = model.asTalentForm();
					int count = redisRelationshipService.countFans(userId);
					user.setFansCount(count);
					if (count / 10000 > 0){
						user.setFansCountShow((count /10000) + "万");
					}else{
						user.setFansCountShow(count+"");
					}
					listItem.setUser(user);
					if (uid != userId){
						listItem.setIsFollow(redisRelationshipService.isFollowed(uid,userId) ? 1 : 0);
					}else{
						listItem.setIsFollow(0);
					}
					users.add(listItem);
				}catch (Exception e) {
					logger.error("查询热门达人出错！Message="+e.getMessage()+",e="+e);
				}
			}
		}
		return users;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TalentForm> loadTalentList(long uid, int start, int end) {
		List<TalentForm> users = new ArrayList<TalentForm>();
		List list = jmsTemplate.boundListOps(HotContentKeyManager.getTopUserKey()).range(start, end);
		if (list != null){
			for (Object id: list){
				try{
					Long userId = Long.parseLong((String)id);
					UserModel model = redisUserService.getUser(userId);
					TalentForm talent = model.asTalentForm();
					int fansCount = redisRelationshipService.countFans(userId);
					talent.setFansCount(fansCount);
					if (fansCount / 10000 > 0){
						talent.setFansCountShow((fansCount /10000) + "万");
					}else{
						talent.setFansCountShow(fansCount+"");
					}
					int contentCount = redisContentService.countUserPublicContents(userId);
					talent.setContentCount(contentCount);
					users.add(talent);
				}catch (Exception e) {
					logger.error("查询热门达人榜单出错！Message="+e.getMessage()+",e="+e);
				}
			}
		}
		return users;
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public List<ActionForm> loadHotContent(long uid, int start, int end,int type) {
		List<ActionForm> actions = new ArrayList<ActionForm>();
		String key = getHotContentListKey(type);
		if (key != null){
			List list = jmsTemplate.boundListOps(key).range(start, end);
			// 循环取出内容详细信息
			if (list != null) {
				for (Object obj :list) {
					ActionForm action = new ActionForm();
					try{
						long cid = Long.parseLong((String)obj);
						ActionModel actionModel = new ActionModel();
						actionModel.setAction(ActionModel.Action.CREATE.getMark());
						actionModel.setCid(cid);
						actionModel.setUid(0);//创建则使用内容作者ID
						action = redisTimelineService.getActionForm(actionModel,uid,0,true);
						actions.add(action);
					}catch(Exception e){
						logger.error("获取最新内容列表失败！,Message:"+e.getMessage());
					}
				}
			}
		}
		return actions;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countHotContent(int type) {
		String key = getHotContentListKey(type);
		if (key != null){
			Long count = jmsTemplate.boundListOps(key).size();
			return count == null?0:count.intValue();
		}else{
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countHotUser() {
		Long count = jmsTemplate.boundListOps(HotContentKeyManager.getTopUserKey()).size();
		return count == null?0:count.intValue();
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertSearchTag(String tagStr) {
		String[] tags = tagStr.split("\\|");
		Long count = jmsTemplate.boundListOps(SystemKeyManager.getSearchTag()).size();
		for (String tag:tags){
			if (tag.trim().length()>0){
				jmsTemplate.boundListOps(SystemKeyManager.getSearchTag()).rightPush(tag);
			}
		}
		Long total = jmsTemplate.boundListOps(SystemKeyManager.getSearchTag()).size();
		//删除老数据
		jmsTemplate.boundListOps(SystemKeyManager.getSearchTag()).trim(count, total - 1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> loadSearchTags() {
		List list =jmsTemplate.boundListOps(SystemKeyManager.getSearchTag()).range(0, -1);
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertPublishTag(String tagStr) {
		jmsTemplate.delete(SystemKeyManager.getPublishTag());
		String[] tags = tagStr.split("\\|");
		for (String tag:tags){
			if (tag.trim().length()>0){
				jmsTemplate.boundListOps(SystemKeyManager.getPublishTag()).leftPush(tag);
			}
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> loadPublishTags() {
		List list =jmsTemplate.boundListOps(SystemKeyManager.getPublishTag()).range(0, -1);
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void insertChannelTags(List<TagHotModel> tags) {
		Long count = jmsTemplate.boundListOps(HotContentKeyManager.getChannelListKey()).size();
		for(TagHotModel tag:tags){
			if (tag != null){
				String json = JSON.toJSONString(tag);
				if (json != null){
					jmsTemplate.boundListOps(HotContentKeyManager.getChannelListKey()).rightPush(json);
				}
			}
		}
		Long total = jmsTemplate.boundListOps(HotContentKeyManager.getChannelListKey()).size();
		//删除老数据
		jmsTemplate.boundListOps(HotContentKeyManager.getChannelListKey()).trim(count, total - 1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TagHotModel> loadChannelTags() {
		List<TagHotModel> tags = new ArrayList<TagHotModel>();
		List list = jmsTemplate.boundListOps(HotContentKeyManager.getChannelListKey()).range(0 , -1);
		if (list != null) {
			for (Object obj :list) {
				try{
					String json = (String)obj;
					TagHotModel tag = JSON.parseObject(json,TagHotModel.class);
					tag.setImageUrl(HoolaConfig.getAliyunSystemImagesDomain()+tag.getImageUrl());
					tags.add(tag);
				}catch(Exception e){
					logger.error("获取最新内容列表失败！,Message:"+e.getMessage());
				}
			}
		}
		return tags;
	}

	@SuppressWarnings("unchecked")
	private void insertRecommendUserData(UserRecommendModel recUser) {
		if (recUser != null && recUser.getUid() > 0) {
			jmsTemplate.boundHashOps(
					HotContentKeyManager.getRecommendUserDataKey(recUser.getUid())).putAll(
					recommendUserMapper.toHash(recUser));
		}
	}
	
	

	@SuppressWarnings("unchecked")
	private void deleteRecommendUserData(UserRecommendModel recUser) {
		if (recUser != null && recUser.getUid() > 0) {
			jmsTemplate.delete(HotContentKeyManager.getRecommendUserDataKey(recUser.getUid()));
		}
	}

	@Override
	public UserRecommendModel getRecommendUser(long uid) {
		return recommendUserMapper.fromHash(getRecommendUserMap(uid));
	}
	
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getRecommendUserMap(long uid) {
		return new DefaultRedisMap<String, String>(HotContentKeyManager.getRecommendUserDataKey(uid), jmsTemplate);
	}

	public void setRedisHotService(IRedisHotService redisHotService) {
		this.redisHotService = redisHotService;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SimpleUserForm> loadRecommendTalents(long uid) {
		Set set = jmsTemplate.boundZSetOps(HotContentKeyManager.getRecommendTalentListKey()).reverseRange(0, -1);
		if (set != null && set.size() > 0){
			List<SimpleUserForm> users = new ArrayList<SimpleUserForm>();
			Iterator it = set.iterator();
			while (it.hasNext()){
				String uidStr = (String)it.next();
				try{
					long userid = Long.parseLong(uidStr);
					UserRecommendModel recUser = redisHotService.getRecommendUser(userid);
					if (recUser != null && recUser.getUid()>0){
						UserModel user = redisUserService.getUser(userid);
						if (user != null && user.getUid()>0 && user.getUid() != uid){
							user.setSignature(recUser.getReason());//用用户的签名信息存储推荐理由
							users.add(user.asSimpleUserForm());
						}
					}
				}catch (Exception e) {
					logger.error("查询编辑推荐用户列表出错！Message="+e.getMessage()+",e="+e);
				}
			}
			return users;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertSubject(SubjectModel subject) {
		if (subject != null && subject.getSid() > 0 ){
			try{
				//插入话题信息
				jmsTemplate.boundHashOps(HotContentKeyManager.getSubjectDateKey(subject.getSid())).putAll(subjectMapper.toHash(subject));
				//删除列表，以免重复插入
				jmsTemplate.boundListOps(HotContentKeyManager.getSubjectListKey()).remove(0, String.valueOf(subject.getSid()));
				//插入话题列表
				jmsTemplate.boundListOps(HotContentKeyManager.getSubjectListKey()).leftPush(String.valueOf(subject.getSid()));
			}catch(Exception e){
				logger.error("删除话题出错！e="+e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertSubjectContent(int sid,long cid){
		SubjectForm subject = this.getSubject(sid);
		if (subject != null && subject.getSid()>0){
			//删除避免重复插入
			jmsTemplate.boundListOps(HotContentKeyManager.getSubjectContentsKey(sid)).remove(0,String.valueOf(cid));
			//插入内容id
			jmsTemplate.boundListOps(HotContentKeyManager.getSubjectContentsKey(sid)).leftPush(String.valueOf(cid));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ActionForm> loadSubjectContents(long uid,int sid,int begin,int end){
		SubjectForm subject = this.getSubject(sid);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		if (subject != null){
			List ids = jmsTemplate.boundListOps(HotContentKeyManager.getSubjectContentsKey(sid)).range(begin, end);
			for (Object obj : ids){
				try{
					long cid = Long.parseLong(String.valueOf(obj));
					ActionModel actionModel = new ActionModel();
					actionModel.setAction(ActionModel.Action.CREATE.getMark());
					actionModel.setCid(cid);
					actionModel.setUid(0);
					ActionForm action = redisTimelineService.getActionForm(actionModel, uid, 0, false);
					if (action != null ){
						actions.add(action);
					}
				}catch(Exception e){
					logger.error("类型转化出错！e="+e);
				}
			}
		}
		return actions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countSubjectContents(long sid){
		Long count = jmsTemplate.boundListOps(HotContentKeyManager.getSubjectContentsKey(sid)).size();
		return count == null ? 0:count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteSubject(int sid) {
		try{
			//删除列表
			jmsTemplate.boundListOps(HotContentKeyManager.getSubjectListKey()).remove(0, String.valueOf(sid));
			//删除话题信息
			jmsTemplate.delete(HotContentKeyManager.getSubjectDateKey(sid));
		}catch(Exception e){
			logger.error("删除话题出错！e="+e);
		}
		
		
		
	}

	@Override
	public SubjectForm getSubject(int sid) {
		return subjectMapper.fromHash(this.getSubjectMap(sid)).asForm();
	}
	
	
	
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getSubjectMap(int sid) {
		return new DefaultRedisMap<String, String>(HotContentKeyManager.getSubjectDateKey(sid), jmsTemplate);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SubjectForm> loadSubjects(long uid, int begin, int end) {
		List sids = jmsTemplate.boundListOps(HotContentKeyManager.getSubjectListKey()).range(begin, end);
		List<SubjectForm> subjects = new ArrayList<SubjectForm>();
		for (Object obj : sids){
			int sid = Integer.parseInt(String.valueOf(obj));
			SubjectForm subject = this.getSubject(sid);
			if (subject != null){
				subjects.add(subject);
			}
		}
		return subjects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countSubject() {
		Long size = jmsTemplate.boundListOps(HotContentKeyManager.getSubjectListKey()).size();
		return size == null ? 0: size.intValue();
	}
	
	private String getHotContentListKey(int type){
		String key = null;
		switch (type) {
		case 0:
			key = HotContentKeyManager.getTopContentKey();
			break;
		case 1:
			key = HotContentKeyManager.getAllTopContentKey();
			break;
		case 2:
			key = HotContentKeyManager.getWeekTopContentKey();
			break;
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countTags() {
		Long count = jmsTemplate.boundListOps(HotContentKeyManager.getSysTopTagKey()).size();
		return count == null ? 0 : count.intValue();
	}

}
