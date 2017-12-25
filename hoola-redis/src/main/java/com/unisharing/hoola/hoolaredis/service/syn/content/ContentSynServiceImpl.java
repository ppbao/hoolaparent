package com.unisharing.hoola.hoolaredis.service.syn.content;

import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.indexService.IIndexBuilder;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.IndexContentModel;
import com.unisharing.hoola.hoolacommon.utils.HoolaStringUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolaredis.key.ContentKeyManager;
import com.unisharing.hoola.hoolaredis.pubsub.IRedisPublisher;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.jms.IRedisJMSMessageService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service("contentSynService")
public class ContentSynServiceImpl extends BaseRedisService implements IContentSynService {

	Log logger = LogFactory.getLog(ContentSynServiceImpl.class);
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisTimelineService redisTimelineService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
//todo	@Autowired
//	IIndexBuilder indexBuilderService;
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisMessageService redisMessageService;
	@Autowired
	IRedisPublisher hoolaPublisher;
	@Autowired
	IRedisJMSMessageService redisJMSMessageService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean synContentAdd(ContentModel model) {
		
		if (model != null){
			try{
				if (model.getLocation() == null){
					model.setLocation("");
				}
				//插入主内容数据
				redisContentService.insertContent(model);
				
				//插入Redis用户公开内容列表：set:user:[id]:public
				redisTimelineService.insertUserContentList(model.getUid(), model.getCid(), model.getIsPrivate());
				
				ActionModel action = new ActionModel(model.getUid(), ActionModel.Action.CREATE.getMark(),model.getCid());
				//公开内容推送给到粉丝动态列表
				if (model.getIsPrivate() == 0){
					//Hoola君（趣拍）插入置顶数据
					if (model.getUid() == 1 && model.getTop() == 1){//趣拍的置顶内容
						redisTimelineService.insertTimelineTopAction(action);
					}
					//公开内容插入最新列表
					redisContentService.insertContentList(model.getCid());
					//非名人进行推送和不是Hoola君进行推送
					if (!redisUserService.isFamous(model.getUid()) && model.getUid() != 1){
						//List<SimpleUserForm> fans = redisRelationshipService.loadFans(model.getUid(),0,0);
						Set fansUid = redisRelationshipService.loadFansUid(model.getUid(),0,0);
						if (fansUid != null){
							for (Object obj : fansUid){
								try{
									long uid = Long.parseLong((String)obj);
									redisTimelineService.insertUserTimeLine(uid, action);
								}catch(Exception e){
									logger.error("同步缓存出错！"+e.getMessage());
								}
							}
						}
					}
					//创建标签和内容的对应关系
					Set<String> tags = HoolaStringUtils.getTags(model.getDescription());
					redisContentService.buildTagIndex(model.getCid(), tags);
					//创建查询索引
					try{
						IndexContentModel indexModel = new IndexContentModel();
						indexModel.setCid(model.getCid());
						indexModel.setDescription(model.getDescription());
						indexModel.setTags(tags.toString());
						indexModel.setTime(model.getUploadTime());
                   //todo     indexBuilderService.buildConentIndex(indexModel);
					}catch (Exception e) {
						logger.error("创建内容索引失败："+e.getMessage());
					}
				} 
				return true;
			}catch (Exception e){
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean synContentDelete(long cid,int status) {
		ContentModel model = redisContentService.getContent(cid);
		if (model != null && model.getCid() > 0){
			//更新内容状态(编辑删除)
			redisContentService.updateContentStatus(cid, status);
			if (status == ContentModel.Status.EDITOR_DELETE.getMark()){
				//删除我创作的内容
				redisContentService.deleteContentFromList(model.getUid(), cid);
				ContentModel content = redisContentService.getContent(cid);
				ActionModel action = new ActionModel();
				action.setAction(ActionModel.Action.CREATE.getMark());
				action.setCid(cid);
				action.setUid(content.getUid());
				if (content != null && content.getCid() > 0){
					Set fans = redisRelationshipService.loadFansUid(content.getUid(),0,0);
					if (fans != null){
						for (Object obj : fans){
							try{
								long uid = Long.parseLong((String)obj);
								redisTimelineService.deleteUserTimeLine(uid, action);
							}catch(Exception e){
								logger.error("同步缓存出错！"+e.getMessage());
							}
						}
					}
					//删除标签列表
					Set<String> tags = HoolaStringUtils.getTags(content.getDescription());
					for (String tag : tags){
						redisContentService.delTagIndex(tag, cid);
					}
				}
				//删除索引
           //todo     indexBuilderService.deleteContentIndex(cid);
			}
		}
		return true;
	}
	
	@Override
	public boolean synContentLike(long uid ,long cid) {
		ActionModel action = new ActionModel();
		action.setCid(cid);
		action.setUid(uid);
		action.setAction(ActionModel.Action.LIKE.getMark());
		//添加Like用户列表
		boolean isLiked = redisContentService.isLiked(cid, uid);
		if (!isLiked){
			redisContentService.likeContent(cid, uid);
		}
		//发送At消息
		ContentModel content = redisContentService.getContent(cid);
		if (content != null && content.getUid() > 0){
			redisMessageService.sendAtMessage(content.getUid(), action);
		}
		//消息推送暂时不推
		
		return true;
	}
	
	
	
	
	
	@Override
	public String synContentForward(long uid, long cid) {
		String message="";
		ActionModel action=new ActionModel();
		action.setCid(cid);
		action.setUid(uid);
		action.setAction(ActionModel.Action.FORWARD.getMark());
		ContentModel content = redisContentService.getContent(cid);
		if(content==null || content.getCid()==0){
			message="该内容不存在";
		}else{
			if(content.getcStatus()!=0){
				message="该内容被删除";
			}else{
				if(content.getUid()==uid){
					message="自己不能转发自己的内容";
				}else{
					boolean isForward = redisContentService.isForwarded(cid, uid);
					if(!isForward){
						redisContentService.forwardContent(cid, uid);
						try{
							//尝试删除本地消息队列的数据防止重复操作
							redisJMSMessageService.deleteCancelForwardMessageQueue(action);
							redisJMSMessageService.insertForwardMessageQueue(action);
							hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.FORWARD));
							message="转发成功";
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						message="不能重复转发";
					}
				}
			}
		}
		return message;
	}


	@Override
	public boolean synContentEdit(long cid, String description) {
		
		ContentModel oldContent = redisContentService.getContent(cid);
		if (oldContent!= null && oldContent.getCid()> 0){
			//修改Tag内容对应关系
			//1、删除原Tag对应关系
			if (StringUtils.isNotBlank(oldContent.getDescription())){
				Set<String> tags = HoolaStringUtils.getTags(oldContent.getDescription());
				for(String tag:tags){
					redisContentService.delTagIndex(tag, cid);
				}
			}
			//2、添加新的标签内容对应关系
			if (StringUtils.isNotBlank(description)){
				Set<String> tags = HoolaStringUtils.getTags(description);
				redisContentService.buildTagIndex(cid,tags);
			}
			//3、修改内容
			redisContentService.updateDescription(cid, description);
			//4、修改索引
       //todo     indexBuilderService.deleteContentIndex(cid);
			//5、重新创建索引
			IndexContentModel index = new IndexContentModel();
			index.setCid(cid);
			index.setDescription(description);
			index.setTags(HoolaStringUtils.getTagString(description));
			index.setTime(oldContent.getUploadTime());
      //todo      indexBuilderService.buildConentIndex(index);
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean synContentShowTimes(long cid, int times) {
		ContentModel content = redisContentService.getContent(cid);
		if (content != null && content.getCid()>0){
			Long num = contentTemplate.boundValueOps(ContentKeyManager.getContentPlayNumKey(cid)).increment(times);
			contentTemplate.boundHashOps(ContentKeyManager.getContentInfoKey(cid+"")).put("showTimes", String.valueOf(num));
			return true;
		}
		return false;
	}

	

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}
//
//	 public void setindexBuilder(IIndexBuilder indexBuilderService) {
//		this.indexBuilderService = indexBuilderService;
//	}

	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisMessageService(IRedisMessageService redisMessageService) {
		this.redisMessageService = redisMessageService;
	}

	public void setHoolaPublisher(IRedisPublisher hoolaPublisher) {
		this.hoolaPublisher = hoolaPublisher;
	}


	public void setRedisJMSMessageService(
			IRedisJMSMessageService redisJMSMessageService) {
		this.redisJMSMessageService = redisJMSMessageService;
	}


	@Override
	public boolean synContentFriendsShareNum(long cid, int count) {
		try{
			this.redisContentService.updateFriendsShareNum(cid, count);
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}


	@Override
	public boolean synContentSinaShareNum(long cid, int count) {
		try{
			this.redisContentService.updateSinaShareNum(cid, count);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
}
