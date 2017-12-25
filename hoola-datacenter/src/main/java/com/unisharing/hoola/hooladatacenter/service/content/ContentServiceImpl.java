package com.unisharing.hoola.hooladatacenter.service.content;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.forwardcontent.IClientForwardContentService;
import com.unisharing.hoola.hoolaclient.service.info.IClientContentService;
import com.unisharing.hoola.hoolaclient.service.likecontent.IClientLikeContentService;
import com.unisharing.hoola.hoolacommon.bean.AtMessageBean;
import com.unisharing.hoola.hoolacommon.bean.ContentBean;
import com.unisharing.hoola.hoolacommon.bean.ShareBean;
import com.unisharing.hoola.hoolacommon.indexService.IIndexBuilder;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.service.IShareService;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.utils.HoolaStringUtils;
import com.unisharing.hoola.hoolacommon.utils.PushSender;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.key.ContentKeyManager;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.key.TimelineKeyManager;
import com.unisharing.hoola.hoolaredis.pubsub.IRedisPublisher;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.group.IRedisGroupService;
import com.unisharing.hoola.hoolaredis.service.hot.IRedisHotService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.report.IRedisReportService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ContentServiceImpl extends BaseRedisService implements IContentService {
	Log logger = LogFactory.getLog(ContentServiceImpl.class);
	@Autowired
	IRedisTimelineService redisTimelineService;
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	
//	IESIndexBuilder esIndexBuilder;
    @Autowired
	IShareService shareService;
    @Autowired
	IRedisUserService redisUserService;
    @Autowired
	IClientContentService clientContentService;
    @Autowired
	IRedisPublisher compressPublisher;
    @Autowired
	IRedisMessageService redisMessageService;
    @Autowired
	IRedisGroupService redisGroupService;
    @Autowired
	IClientLikeContentService clientLikeContentService;
    @Autowired
	IClientForwardContentService clientForwardContentService;
    @Autowired
	IRedisReportService redisReportService;
    @Autowired
	IIndexBuilder indexBuilder;
    @Autowired
	IRedisHotService redisHotService;
	
	private final static String DEFAULT_SHARE_TEXT = "这个视频碉堡啦！赶紧来看呀！>>>";//分享别人的默认分享语
	private final static String SELF_DEFAULT_SHARE_TEXT = "趣拍视频来一发，咩哈哈哈哈！戳这里，戳这里";//分享自己的默认分享语
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void handleContentAdd(long cid) {
		String submitJson = null;
		ContentBean bean = null;
		do {
			//从redis数据库队列中取数据
			submitJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getNewContentMQKey()).rightPop();
			bean = JSON.parseObject(submitJson, ContentBean.class);
			//因为Redis的消息不可靠，为了防止数据丢失则需要对丢失的数据进行补偿
			if (bean != null) {
				//插入话题和内容的对应关系
				try{
					redisHotService.insertSubjectContent(bean.getActiveId(), bean.getCid());
				}catch(Exception e){
					logger.error("插入话题内容对应关系出错！e="+e);
				}
				//插入Redis用户公开内容列表：set:user:[id]:public
				redisTimelineService.insertUserContentList(bean.getUid(), bean.getCid(), bean.getIsPrivate());
				
				ActionModel action = new ActionModel(bean.getUid(), ActionModel.Action.CREATE.getMark(),bean.getCid());
				//TODO 发送压缩通知 暂缓
				//compressPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.COMPRESS));
				
				//公开内容推送给到粉丝动态列表-内容分发
				if (bean.getIsPrivate() == 0 && !StringUtils.hasText(bean.getGroupNames())){
					//公开内容插入最新列表
					redisContentService.insertContentList(bean.getCid());
					//TODO 非名人进行推送
					//pushAction(bean.getUid(),action);
					pushAction(bean.getUid(),action);//pipeline 推送
					//发送At消息给Hoola用户
					this.atDuanqu(bean.getAtUserDuanqu(),bean.getDescription(), bean.getUid(), bean.getCid());
					
					//缓存At用户列表
					List<SimpleUserForm> atUsers = redisTimelineService.loadAtUsers(HoolaStringUtils.getNickNames(bean.getDescription()));
					if (atUsers != null){
						try{
							String json = JSON.toJSONString(atUsers);
							contentTemplate.boundValueOps(ContentKeyManager.getAtUserCacheKey(bean.getCid())).set(json);
						}catch(Exception e){
							logger.error("loadAtUserFromCache 数据格式转化错误！ List to String"+e);
						}
					}
					
					BindModel sinaBindModel = redisUserService.getBindInfo(bean.getUid(), BindModel.OpenType.SINA.getMark());
					if (sinaBindModel.getUid()>0){
						//发送At消息到新浪微波
						if (StringUtils.hasText(bean.getAtUserSina())) {
							/*String description = EmojiUtils.decodeEmoji(bean.getDescription());
							String url = HoolaConfig.getWapHost() + bean.getKey() + ".htm?from=sina";
							String content = cutAtContent(description, bean.getAtUserSina());
							content += url + HoolaConfig.getComeFrom();
							try {
								shareService.shareToSina(bean.getThumbnailsUrl(), content, sinaBindModel.getAccessToken());
							} catch (Exception e) {
								logger.error("分享新浪微博出错！" + e.getMessage());
							}*/
							
							this.shareToWeibo(bean.getUid(),
									bean.getDescription(), bean.getKey(),
									bean.getThumbnailsUrl(), bean.getAtUserSina());
						}else{
							//同步内容到新浪微博，如果有At消息则不同步
							if (bean.getShareSina() == 1){
								/*String description = EmojiUtils.decodeEmoji(bean.getDescription());
								String content = cutShareContent(description);
								String url = HoolaConfig.getWapHost() + bean.getKey() + ".htm?from=sina";
								content += url + HoolaConfig.getComeFrom();
								try{
									shareService.shareToSina(bean.getThumbnailsUrl(), content, sinaBindModel.getAccessToken());
								}catch (Exception e){
									logger.error("分享新浪微出错！"+e.getMessage());
								}*/
								this.shareToWeibo(bean.getUid(),
										bean.getDescription(), bean.getKey(),
										bean.getThumbnailsUrl(), null);
							}
						}
					}
					BindModel qqBindModel = redisUserService.getBindInfo(bean.getUid(), BindModel.OpenType.TENCENT.getMark());
					if (qqBindModel.getUid()>0 && qqBindModel.getAccessToken().length() > 0){
						//发送At消息到腾讯微博
						if (StringUtils.hasText(bean.getAtUserTencent())) {
							/*String description = EmojiUtils.decodeEmoji(bean.getDescription());
							String url = HoolaConfig.getWapHost() + bean.getKey() + ".htm";
							String content = cutAtContent(description, bean.getAtUserTencent());
							content += url + HoolaConfig.getComeFrom();
							content = content.replaceAll("@趣拍APP", "@qupaiapp");
							try {
								shareService.shareToQQWeibo(bean.getThumbnailsUrl(), content, 
										qqBindModel.getAccessToken(),	qqBindModel.getOpenUid());
							} catch (Exception e) {
								logger.error("AT腾讯微博出错！" + e.getMessage());
							}*/
							this.shareToTencent(bean.getUid(),
									bean.getDescription(), bean.getKey(),
									bean.getThumbnailsUrl(),
									bean.getAtUserTencent());

						}else{
							if (bean.getShareTencentWeiBo() == 1){
								//TODO 分享到腾讯微博
								/*String description = EmojiUtils.decodeEmoji(bean.getDescription());
								String url = HoolaConfig.getWapHost() + bean.getKey() + ".htm?from=qqweibo";
								String content = cutShareContent(description);
								content += url + HoolaConfig.getComeFrom();
								content = content.replaceAll("@趣拍APP", "@qupaiapp");
								try {
									shareService.shareToQQWeibo(bean.getThumbnailsUrl(), content, 
											qqBindModel.getAccessToken(),	qqBindModel.getOpenUid());
								} catch (Exception e) {
									logger.error("分享腾讯微博出错！" + e.getMessage());
								}*/
								this.shareToTencent(bean.getUid(),
										bean.getDescription(), bean.getKey(),
										bean.getThumbnailsUrl(), null);
							}
						}
						// 同步信息到QQ空间
						if (bean.getShareTencent() == 1) {
							/*try{
								String description = EmojiUtils.decodeEmoji(bean.getDescription());
								String url = HoolaConfig.getWapHost() + bean.getKey() + ".htm";
								shareService.shareToQZone("趣拍视频来一发，咩哈哈哈哈！戳这里，戳这里>>>",description, url,
										HoolaConfig.getAliyunHDVideoDomain()
												+ bean.getVideoUrlHD(),
										HoolaConfig.getAliyunThumbnailDomain()
												+ bean.getThumbnailsUrl(),
										qqBindModel.getAccessToken(),
										qqBindModel.getOpenUid());
							}catch (Exception e) {
								logger.error("分享QQ空间失败！"+e.getMessage()+"，bean="+bean+"，qqBindModel="+qqBindModel);
							}*/
							shareToQzone(bean.getUid(), bean.getDescription(),
									bean.getKey(), bean.getThumbnailsUrl(),
									bean.getVideoUrl());
						}
					}
					//插入我at的列表
					if (StringUtils.hasText(bean.getAtUserDuanqu())
							|| StringUtils.hasText(bean.getAtUserSina())
							|| StringUtils.hasText(bean.getAtUserTencent())){
						redisTimelineService.insertUserAtList(bean.getUid(), bean.getCid());
					}
					// 我所有里面不现实分享自己的内容。
					/*if (bean.getShareSina() == 1 || bean.getShareTencent() == 1){
						redisTimelineService.insertUserShareList(bean.getUid(), bean.getCid());
					}*/
					//创建标签和内容的对应关系
					Set<String> tags = HoolaStringUtils.getTags(bean.getDescription());
					redisContentService.buildTagIndex(bean.getCid(), tags);
					try{
						if (StringUtils.hasText(bean.getDescription())){
							//创建查询索引
							IndexContentModel model = new IndexContentModel();
							model.setCid(bean.getCid());
							model.setDescription(bean.getDescription());
							model.setTags(tags.toString());
							model.setTime(bean.getUploadTime());
							indexBuilder.buildConentIndex(model);
						}
					}catch(Exception e){
						logger.error("创建内容索引出错！"+e.getMessage());
					}
				} else {
					//组内分享
					if (bean.getGroupNames() != null && bean.getGroupNames().trim().length()>0){
						String groupNames = bean.getGroupNames();
						Set<Long> userIds = this.loadGroupUsers(bean.getUid(), groupNames);
						for (Long userId : userIds){
							redisTimelineService.insertUserGroupTimeLine(userId, action);
						}
					}
				}
				try{
					//插入数据库
					ContentModel contentModel = bean.asContentModel();
					contentModel.setIsShow(1);
					contentModel.setcStatus(0);
					clientContentService.insertContentInfo(contentModel);
				}catch (Exception e) {
					logger.error("插入内容数据库出错！Message="+e.getMessage()+",Params="+bean);
				}
				//统计帖子使用帖子，滤镜，音乐使用频率
				//贴纸
				 if(StringUtils.hasText(bean.getTiezhiNo())){
					 String[] tiezis = bean.getTiezhiNo().split(",");
					for (String tiezi : tiezis) {
						redisReportService.addTiezhiReport(bean.getCid(), tiezi);
					}
				 }
				 //滤镜
				 if(StringUtils.hasText(bean.getFilterNo())){
					 String[] filters = bean.getFilterNo().split(",");
					for (String filter : filters) {
						redisReportService.addFilterReport(bean.getCid(), filter);
					}
				 }
				 //表情
				 if(StringUtils.hasText(bean.getBiaoqingNo())){
					 String[] biaoqings = bean.getBiaoqingNo().split(",");
					for (String biaoqing : biaoqings) {
						redisReportService.addBiaoqingReport(bean.getCid(), biaoqing);
					}
				 }
				 //音乐
				 if(StringUtils.hasText(bean.getMusicNo())){
					 String[] musics = bean.getMusicNo().split(",");
					for (String music : musics) {
						redisReportService.addMusicReport(bean.getCid(), music);
					}
				 }
			}
		}while(bean != null && bean.getCid() != cid);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handelContentLike() {
		String likeJson = null;
		ActionModel action = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getLikeListKey()).rightPop();
		while(likeJson != null){
			action = JSON.parseObject(likeJson, ActionModel.class);
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			//发送At消息
			ContentModel content = redisContentService.getContent(action.getCid());
			if (content != null && content.getUid() > 0){
				redisMessageService.sendAtMessage(content.getUid(), action);
			}
			
			SettingModel setting = redisUserService.getUserSetting(action.getUid());
			if (setting == null || setting.getShare() == 1){
				int count = redisUserService.countLikeSynNumOneDay(action.getUid());
				if (count < 3){
					// 同步到新浪
					BindModel bind = redisUserService.getBindInfo(action.getUid(), BindModel.OpenType.SINA.getMark());
					if (bind != null && bind.getUid() > 0
							&& content.getIsPrivate() == 0) {
						UserModel user = redisUserService.getUser(content.getUid());
						shareService.shareToSina(content.getThumbnailsUrl(),DEFAULT_SHARE_TEXT + HoolaConfig.getWapHost()
										+ content.getKey() + ".htm?from=sina" + " 导演：@"	+ user.getNickName() + "（来自@趣拍APP）", 
										bind.getAccessToken());
					}

					// 同步到腾讯微博
					BindModel qqBind = redisUserService.getBindInfo(
							action.getUid(), BindModel.OpenType.TENCENT.getMark());
					if (qqBind != null && qqBind.getUid() > 0 && content.getIsPrivate() == 0) {
						UserModel user = redisUserService.getUser(content.getUid());
						shareService.shareToQQWeibo(content.getThumbnailsUrl(),
								DEFAULT_SHARE_TEXT + HoolaConfig.getWapHost()
										+ content.getKey() + ".htm" + " 导演：@"
										+ user.getNickName() + "（来自@qupaiapp）",
										qqBind.getAccessToken(),qqBind.getOpenUid());
					}
					redisUserService.addLikeSynMumOneDay(action.getUid());
				}
			}
			
			
			try{
				//同步数据库
				LikeContentModel likeContentModel = new LikeContentModel();
				likeContentModel.setCid(action.getCid());
				likeContentModel.setUid(action.getUid());
				likeContentModel.setCreateTime(System.currentTimeMillis());
				clientLikeContentService.insertLikeContentModel(likeContentModel);
			}catch (Exception e) {
				logger.error("同步喜欢信息到数据库出错！Message="+e.getMessage()+",Params="+action);
			}
			
			// 推送喜欢消息到客户端
			UserModel revUser = redisUserService.getUser(content.getUid());
			if (revUser != null && revUser.getDeviceToken() != null
					&& revUser.getDeviceToken().trim().length() >= 64) {
				SettingModel revSetting = redisUserService.getUserSetting(revUser.getUid());
				UserModel senderUser = redisUserService.getUser(action.getUid());
				int count = redisMessageService.countTotalNewMessage(revUser
						.getUid());
				Set<String> tokens = redisUserService.getUserDeviceToken(revUser.getUid());
				if (revSetting.getLikeMessage() == 1){
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, senderUser.getNickName()
									+ "喜欢了我的内容", count, "qupai://message/at");
							// PushSender.send(token, null, count, "message/at");
						}
					}
				} else {
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, null, count, "qupai://message/at");
						}
					}
				}
			}
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getLikeListKey()).rightPop();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handelContentDislike() {
		String likeJson = null;
		ActionModel action = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getDislikeListKey()).rightPop();
		while(likeJson != null){
			action = JSON.parseObject(likeJson, ActionModel.class);
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			//不喜欢更新数据库
			LikeContentModel likeContentModel = new LikeContentModel();
			likeContentModel.setCid(action.getCid());
			likeContentModel.setUid(action.getUid());
			clientLikeContentService.deleteLikeContentModel(likeContentModel);
			
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getDislikeListKey()).rightPop();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleForwardContent() {
		String json = null;
		ActionModel action = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getForwardListKey()).rightPop();
		while(json != null){
			action = JSON.parseObject(json, ActionModel.class);
			//发送At消息
			ContentModel content = redisContentService.getContent(action.getCid());
			if (content != null && content.getUid() > 0){
				redisMessageService.sendAtMessage(content.getUid(), action);
			}
			//推送Timeline
			if (!redisUserService.isFamous(action.getUid())) {
				Set fans = redisRelationshipService.loadFansUid(action.getUid(), 0, 0);
				if (fans != null) {
					for (Object obj : fans) {
						try {
							long uid = Long.parseLong((String) obj);
							//过滤重复转发
							if (!redisTimelineService.isRevLikeTime(uid, action.getCid())){
								//TODO 发布和转发的分开存储 暂时不执行
								redisTimelineService.insertUserTimeLine(uid, action);
								//插入接受的喜欢推送列表
								redisTimelineService.insertUserRevLikeTime(uid, action.getCid(),action.getUid());
							}
							//TODO 转发和上传分开存储
							//redisTimelineService.insertUserTimeLine(uid, action);
						} catch (Exception e) {
							logger.error("推送转发信息时错误！" + e.getMessage());
						}
					}
				}
			}
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			try{
				//转发同步数据库
				ForwardContentModel forwardContentModel = new ForwardContentModel();
				forwardContentModel.setCid(action.getCid());
				forwardContentModel.setUid(action.getUid());
				forwardContentModel.setCreateTime(System.currentTimeMillis());
				clientForwardContentService.insertForwardContentModel(forwardContentModel);
			}catch(Exception e){
				logger.error("转发同步数据库出错！Message="+e.getMessage()+",Params:"+action);
			}
			//推送私信消息到客户端
			SettingModel setting = redisUserService.getUserSetting(content.getUid());
			UserModel revUser = redisUserService.getUser(content.getUid());
			if (revUser != null && revUser.getDeviceToken() != null
					&& revUser.getDeviceToken().trim().length() >= 64) {
				UserModel senderUser = redisUserService
						.getUser(action.getUid());
				int count = redisMessageService.countTotalNewMessage(revUser
						.getUid());
				Set<String> tokens = redisUserService.getUserDeviceToken(revUser.getUid());
				if (setting.getForwardMessage() == 1) {
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, senderUser.getNickName()	+ "转发了我的内容", count, "qupai://message/at");
						}
					}
				} else {
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, null, count, "qupai://message/at");
						}
					}
				}
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getForwardListKey()).rightPop();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleCancelForwardContent() {
		String json = null;
		ActionModel action = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCancelForwardListKey()).rightPop();
		while(json != null){
			action = JSON.parseObject(json, ActionModel.class);
			//删除推送的timeline
			if (!redisUserService.isFamous(action.getUid())) {
				Set fans = redisRelationshipService.loadFansUid(action.getUid(), 0, 0);
				if (fans != null) {
					for (Object obj : fans) {
						try {
							long uid = Long.parseLong((String) obj);
							//TODO 转发和发布分开存储
							redisTimelineService.deleteUserTimeLine(uid, action);
						} catch (Exception e) {
							logger.error("取消转发时删除推送消息出错！" + e.getMessage());
						}
					}
				}
			}
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			try{
				//取消转发同步数据库
				ForwardContentModel forwardContentModel = new ForwardContentModel();
				forwardContentModel.setCid(action.getCid());
				forwardContentModel.setUid(action.getUid());
				clientForwardContentService.deleteForwardContentModel(forwardContentModel);
			}catch(Exception e){
				logger.error("取消转发同步数据库出错!"+e.getMessage()+",e="+e);
			}
			
			
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCancelForwardListKey()).rightPop();
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void handleContentDelete(long cid) {
		ContentModel content = redisContentService.getContent(cid);
		if (content != null && content.getCid() > 0 && content.getIsPrivate() == 0){
			//删除内容索引
			indexBuilder.deleteContentIndex(cid);
			//删除内容更新数据库
			clientContentService.updateContentStatusToDelete(cid);
			//删除标签下内容
			Set<String> tags = HoolaStringUtils.getTags(content.getDescription());
			for (String tag : tags){
				redisContentService.delTagIndex(tag, cid);
			}
			//删除粉丝推送
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
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void handleShareContent() {
		String json = null;
		ShareBean share = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getShareListKey()).rightPop();
		while(json != null){
			share = JSON.parseObject(json, ShareBean.class);
			ContentModel content = redisContentService.getContent(share.getCid());
			int openType = 0;
			if (share.getOpenType() == 1){
				openType = BindModel.OpenType.SINA.getMark();
			}else{
				openType = BindModel.OpenType.TENCENT.getMark();
			}
			BindModel bind = redisUserService.getBindInfo(share.getUid(), openType);
			
			LogShareModel shareLog = new LogShareModel();
			
			//同步到新浪
			if (bind!= null && bind.getUid()>0 && share.getOpenType() == BindModel.OpenType.SINA.getMark()){
				//UserModel user = redisUserService.getUser(share.getUid());
				if (share.getMemo() == null){
					if (content != null && content.getCid()>0){
						UserModel author = redisUserService.getUser(content.getUid());
						
						boolean success = shareService.shareToSina(content.getThumbnailsUrl(), DEFAULT_SHARE_TEXT +HoolaConfig.getWapHost()+content.getKey()+".htm?from=sina"
								+" 导演：@"+author.getNickName()+"（来自@Hoola APP）", bind.getAccessToken());
						
						if (success){
							//更新新浪分享次数
							redisContentService.updateSinaShareNum(share.getCid(), 1);
							//更新数据库
							try{
								clientContentService.updateContentSinaShareNum(share.getCid(), 1);
							}catch(Exception e){
								logger.error("更新新浪分享次数出错！"+e.getMessage());
							}
							shareLog.setFlag(1);
						}else{
							shareLog.setFlag(0);
						}
					}
				}
			}
			if (bind!= null && bind.getUid()>0 && share.getOpenType() == BindModel.OpenType.TENCENT.getMark()){
				String description = null;
				if (StringUtils.hasText(share.getMemo())){
					description = share.getMemo();
				}else{
					 description = EmojiUtils.decodeEmoji(content.getDescription());
				}
				String title = null;
				if (content.getUid() == share.getUid()){
					title = SELF_DEFAULT_SHARE_TEXT;
				}else{
					title = DEFAULT_SHARE_TEXT;
				}
				String url = HoolaConfig.getWapHost() + content.getKey() + ".htm";
				shareService.shareToQZone(title,description, url,
						HoolaConfig.getAliyunHDVideoDomain()
								+ content.getVideoUrlHD(),
						HoolaConfig.getAliyunThumbnailDomain()
								+ content.getThumbnailsUrl(),
								bind.getAccessToken(),
								bind.getOpenUid());
				shareLog.setFlag(1);
			}
			
			if (bind != null && share.getOpenType() == BindModel.OpenType.TENCENTWEIBO.getMark()){
				UserModel author = redisUserService.getUser(content.getUid());
				//TODO 分享信息到腾讯微博
				String description = null;
				if (StringUtils.hasText(share.getMemo())){
					description = share.getMemo()+ ">>>"+HoolaConfig.getWapHost()+content.getKey()+".htm?from=qqweibo"
							+" 导演：@"+author.getNickName()+"（来自@qupaiapp）";
				}else{
					description = DEFAULT_SHARE_TEXT +HoolaConfig.getWapHost()+content.getKey()+".htm?from=qqweibo"
							+" 导演：@"+author.getNickName()+"（来自@qupaiapp）";
				}
				
				try{
					shareService.shareToQQWeibo(content.getThumbnailsUrl(), description, bind.getAccessToken(), bind.getOpenUid());
					shareLog.setFlag(1);
				}catch(Exception e){
					logger.error("分享腾讯微博出错！" + e.getMessage()+",e="+e);
					shareLog.setFlag(0);
				}
				
				
			}
			
			if (share.getOpenType() == BindModel.OpenType.FRIENDS.getMark()){//朋友圈
				//更新朋友圈分享次数
				redisContentService.updateFriendsShareNum(share.getCid(), 1);
				//更新数据库
				try{
					clientContentService.updateContentQuanShareNum(share.getCid(), 1);
					shareLog.setFlag(1);
				}catch(Exception e){
					logger.error("更新朋友圈分享次数出错！"+e.getMessage());
					shareLog.setFlag(0);
				}
				
			}
			
			//插入我分享列表 过滤分享自己的
			if (content != null && share.getUid() != content.getUid()){
				redisTimelineService.insertUserShareList(share.getUid(), share.getCid());
			}
			
			try{
				shareLog.setCid(share.getCid());
				shareLog.setCreateTime(System.currentTimeMillis());
				shareLog.setType(share.getOpenType());
				shareLog.setUid(share.getUid());
				clientContentService.insertShareContent(shareLog);
			}catch(Exception e){
				logger.error("插入分享日志出错，e="+ e);
			}
			
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getShareListKey()).rightPop();
		}
	}
	
	/**
	 * 对At内容的长度控制
	 * @param description
	 * @param atUser
	 * @return
	 */
	private String cutAtContent(String description,String atUser){
		int urlLength = 10;
		int comeFromLength = HoolaConfig.getComeFrom().length();
		int messageLength = HoolaConfig.getAtMessage().length() - 6;
		int message2Length = HoolaConfig.getAtMessage2().length() - 3;
		String content = "";
		atUser = getAtUserString(atUser);
		if (StringUtils.hasText(description)){
			int laveLength = 140 - urlLength - comeFromLength - messageLength;
			int atUsetLength = atUser.length();
			if ( atUsetLength > laveLength ){//去掉所有描述长度还超长，则去掉所有描述采用没有描述的格式
				laveLength = 140 - urlLength - comeFromLength - message2Length;
				if (atUsetLength > laveLength){
					atUser = atUser.substring(0, laveLength - 1);
				}
				content = MessageFormat.format(HoolaConfig.getAtMessage2(),atUser);
			} else {//截取描述 
				int descriptionLength = description.length();
				if (descriptionLength > laveLength){
					description = description.substring(0, (laveLength - atUsetLength -2)) + "…";
				}
				content = MessageFormat.format(HoolaConfig.getAtMessage(),description,atUser);
			}
		}else{
			int laveLength = 140 - urlLength - comeFromLength - message2Length;
			int atUsetLength = atUser.length();
			if (atUsetLength > laveLength){
				atUser = atUser.substring(0, laveLength -1);
			}
			content = MessageFormat.format(HoolaConfig.getAtMessage2(),atUser);
		}
		return content;
	}
	
	/**
	 * 添加@符号
	 * @param atUser
	 * @return
	 */
	private String getAtUserString(String atUser){
		if (StringUtils.hasText(atUser)){
			StringBuffer atUserString = new StringBuffer();
			String[] users = atUser.split(" ");
			for (String user : users){
				atUserString.append("@").append(user).append(" ");
			}
			return atUserString.toString();
		}
		return "";
	}
	
	/**
	 * 对分享内容的长度控制
	 * @param description
	 * @return
	 */
	private String cutShareContent(String description){
		int urlLength = 10;
		int comeFromLength = HoolaConfig.getComeFrom().length();
		int messageLength = HoolaConfig.getShareMessage().length() - 3;
		String content = "";
		if (StringUtils.hasText(description)){
			int laveLength = 140 - urlLength - comeFromLength - messageLength;
			int descriptionLength = description.length();
			if ( descriptionLength > laveLength ){//去掉所有描述长度还超长，则去掉所有描述采用没有描述的格式
				description = description.substring(0, laveLength - 2) + "…";
			} 
			content = MessageFormat.format(HoolaConfig.getShareMessage(),description);
		}else{
			content = HoolaConfig.getShareMessage2();
		}
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleAtContent() {
		String json = null;
		AtMessageBean bean = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getAtListKey()).rightPop();
		while(json != null){
			bean = JSON.parseObject(json, AtMessageBean.class);
			//分享Hoola
			this.atDuanqu(bean.getAtDuanquUsers(),null,bean.getUid(), bean.getCid());
			
			//分享新浪
			if (StringUtils.hasText(bean.getAtSinaUsers())) {
				BindModel sinaBindModel = redisUserService.getBindInfo(bean.getUid(), BindModel.OpenType.SINA.getMark());
				if (sinaBindModel.getUid() > 0) {
					ContentModel contentModel = redisContentService.getContent(bean.getCid());
					// 发送At消息到新浪微波
					String description = EmojiUtils.decodeEmoji(contentModel.getDescription());
					String url = HoolaConfig.getWapHost() + contentModel.getKey() + ".htm?from=sina";
					String content = cutAtContent(description,	bean.getAtSinaUsers());
					content += url + HoolaConfig.getComeFrom();
					try {
						shareService.shareToSina(contentModel.getThumbnailsUrl(),
								content, sinaBindModel.getAccessToken());
					} catch (Exception e) {
						logger.error("分享新浪微博出错！" + e.getMessage());
					}
				}
			}
			// 分享腾讯
			if (StringUtils.hasText(bean.getAtTencentUsers())) {
				BindModel qqBindModel = redisUserService.getBindInfo(bean.getUid(), BindModel.OpenType.TENCENT.getMark());
				if (qqBindModel.getUid() > 0
						&& qqBindModel.getAccessToken().length() > 0) {
					ContentModel contentModel = redisContentService.getContent(bean.getCid());
					String description = EmojiUtils.decodeEmoji(contentModel.getDescription());
					String url = HoolaConfig.getWapHost() + contentModel.getKey() + ".htm";
					String content = cutAtContent(description,bean.getAtTencentUsers());
					content.replaceAll("@HoolaAPP", "@Hoolaapp");
					content += url + HoolaConfig.getComeFrom();
					try {
						shareService.shareToQQWeibo(contentModel.getThumbnailsUrl(),
								content, qqBindModel.getAccessToken(),
								qqBindModel.getOpenUid());
					} catch (Exception e) {
						logger.error("分享腾讯微博出错！" + e.getMessage());
					}

				}
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getAtListKey()).rightPop();
		}
		
	}
	
	/**
	 * 发送HoolaAt消息
	 * @param description //从描述中解析
	 * @param uid //发送用户ID
	 * @param cid	//发送的内容
	 */
	private void atDuanqu(String atDuanquUsers,String description, long uid, long cid) {
		UserModel sendUser = redisUserService.getUser(uid);
		Set<Long> uids = new HashSet<Long>();
		if (StringUtils.hasText(description)) {
			Set<String> nickNames = HoolaStringUtils.getNickNames(description);
			//解析描述信息
			for (String nickName : nickNames) {
				UserModel user = redisUserService.getUserByNickName(nickName);
				if (user!= null && user.getUid()>0){
					uids.add(user.getUid());
				}
			}
		}
		
		if(StringUtils.hasText(atDuanquUsers)){
			String[] ids = atDuanquUsers.split(" ");
			for (String uidStr : ids) {
				try {
					long userId = Long.parseLong(uidStr);
					if (userId > 0){
						uids.add(userId);
					}
				}catch (Exception e) {
					logger.error("数据转化出错！"+e.getMessage());
				}
			}
		}
		
		if (uids.size() > 0) {
			for (long atUid : uids){
				try {
					UserModel user = redisUserService.getUser(atUid);
					boolean isBlack = redisRelationshipService.isBlackUser(atUid, uid);
					if(user!= null && user.getUid()>0 && (!isBlack)){
						ActionModel atMessage = new ActionModel();
						atMessage.setAction(ActionModel.Action.AT.getMark());
						atMessage.setCid(cid);
						atMessage.setUid(uid);
						redisMessageService.sendAtMessage(user.getUid(), atMessage);//发送At消息
						if (user != null && user.getDeviceToken() != null
								&& user.getDeviceToken().trim().length() >= 64) {
							SettingModel setting = redisUserService.getUserSetting(user.getUid());
							Set<String> tokens = redisUserService.getUserDeviceToken(user.getUid());
							int count = redisMessageService.countTotalNewMessage(user.getUid());
							if (setting.getAtMessage() == 1){
								// 发送推送消息到手机终端
								for (String token : tokens) {
									PushSender.send(token, sendUser.getNickName() + "@了我",	count, "qupai://message/at");
								}
							}else{
								for (String token : tokens) {
									//只推送角标数字
									PushSender.send(token, null, count,
											"qupai://message/at");
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("Send Duanqu At Message Error,description=" + description
							+ ",Error Message=" + e.getMessage()+",e="+e);
				}
			}
		}
	}


	private Set<Long> loadGroupUsers(long uid,String groupNames){
		Set<Long> userIds = new HashSet<Long>();
		String[] groups = groupNames.split(",");
		for (String group : groups){
			if(group.trim().length()>0){
				List<SimpleUserForm> users = redisGroupService.loadUsers(uid, group);
				for (SimpleUserForm user: users){
					userIds.add(user.getUid());
				}
			}
		}
		return userIds;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleCancelLikeAndForwardContent() {
		String json = null;
		ActionModel action = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCancelLikeForwardMqKey()).rightPop();
		while(json != null){
			action = JSON.parseObject(json, ActionModel.class);
			
			//删除推送的timeline
			if (!redisUserService.isFamous(action.getUid())) {
				Set fans = redisRelationshipService.loadFansUid(action.getUid(), 0, 0);
				if (fans != null) {
					for (Object obj : fans) {
						try {
							long uid = Long.parseLong((String) obj);
							redisTimelineService.deleteUserTimeLine(uid, action);
							//删除用户已经接受的喜欢推送 -- 用户喜欢排重
							redisTimelineService.deleteUserRevLikeTime(uid, action.getCid(),action.getUid());
						} catch (Exception e) {
							logger.error("取消转发时删除推送消息出错！" + e.getMessage());
						}
					}
				}
			}
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			try{
				//取消转发同步数据库
				ForwardContentModel forwardContentModel = new ForwardContentModel();
				forwardContentModel.setCid(action.getCid());
				forwardContentModel.setUid(action.getUid());
				clientForwardContentService.deleteForwardContentModel(forwardContentModel);
			}catch(Exception e){
				logger.error("取消转发同步数据库出错!"+e.getMessage()+",e="+e);
			}
			
			try{
				//不喜欢更新数据库
				LikeContentModel likeContentModel = new LikeContentModel();
				likeContentModel.setCid(action.getCid());
				likeContentModel.setUid(action.getUid());
				clientLikeContentService.deleteLikeContentModel(likeContentModel);
			}catch(Exception e){
				logger.error("取消喜欢同步数据库出错!"+e.getMessage()+",e="+e);
			}
			
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCancelLikeForwardMqKey()).rightPop();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleLikeAndForwardContent() {
		String likeJson = null;
		ActionModel action = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getLikeForwardMqKey()).rightPop();
		while(likeJson != null){
			action = JSON.parseObject(likeJson, ActionModel.class);
			//发送At消息
			ContentModel content = redisContentService.getContent(action.getCid());
			if (content != null && content.getUid() > 0){
				redisMessageService.sendAtMessage(content.getUid(), action);//喜欢标志
			}
			//-----------------------属于at动作需要做的事情
			if (content.getIsPrivate() == 0){//公开内容 才进行转发 + 同步新浪微博
				//推送Timeline
				if (!redisUserService.isFamous(action.getUid())) {
					Set fans = redisRelationshipService.loadFansUid(action.getUid(), 0, 0);
					if (fans != null) {
						for (Object obj : fans) {
							try {
								long uid = Long.parseLong((String) obj);
								if (!redisTimelineService.isRevLikeTime(uid, action.getCid())){
									//TODO 发布和转发的分开存储 暂时不执行
									redisTimelineService.insertUserTimeLine(uid, action);//注意标识为喜欢标识
									//插入接受的喜欢推送列表
									redisTimelineService.insertUserRevLikeTime(uid, action.getCid(),action.getUid());
								}
							} catch (Exception e) {
								logger.error("推送转发信息时错误！" + e.getMessage());
							}
						}
					}
				}
				try{
					//转发同步数据库
					ForwardContentModel forwardContentModel = new ForwardContentModel();
					forwardContentModel.setCid(action.getCid());
					forwardContentModel.setUid(action.getUid());
					forwardContentModel.setCreateTime(System.currentTimeMillis());
					clientForwardContentService.insertForwardContentModel(forwardContentModel);
				}catch(Exception e){
					logger.error("转发同步数据库出错！Message="+e.getMessage()+",Params:"+action);
				}
				
				SettingModel setting = redisUserService.getUserSetting(action.getUid());
				if (setting == null || setting.getShare() == 1){
					int count = redisUserService.countLikeSynNumOneDay(action.getUid());
					if (count < 3){
						// 同步到新浪
						BindModel bind = redisUserService.getBindInfo(action.getUid(), BindModel.OpenType.SINA.getMark());
						if (bind != null && bind.getUid() > 0
								&& content.getIsPrivate() == 0) {
							UserModel user = redisUserService.getUser(content.getUid());
							shareService.shareToSina(content.getThumbnailsUrl(),DEFAULT_SHARE_TEXT + HoolaConfig.getWapHost()
											+ content.getKey() + ".htm?from=sina" + " 导演：@"	+ user.getNickName() + "（来自@趣拍APP）", 
											bind.getAccessToken());
						}

						// 同步到腾讯微博
						BindModel qqBind = redisUserService.getBindInfo(
								action.getUid(), BindModel.OpenType.TENCENT.getMark());
						if (qqBind != null && qqBind.getUid() > 0 && content.getIsPrivate() == 0) {
							UserModel user = redisUserService.getUser(content.getUid());
							shareService.shareToQQWeibo(content.getThumbnailsUrl(),
									DEFAULT_SHARE_TEXT + HoolaConfig.getWapHost()
											+ content.getKey() + ".htm" + " 导演：@"
											+ user.getNickName() + "（来自@qupaiapp）",
											qqBind.getAccessToken(),qqBind.getOpenUid());
						}
						redisUserService.addLikeSynMumOneDay(action.getUid());
					}
				}
			}
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(action.getCid());
			try{
				//同步数据库
				LikeContentModel likeContentModel = new LikeContentModel();
				likeContentModel.setCid(action.getCid());
				likeContentModel.setUid(action.getUid());
				likeContentModel.setCreateTime(System.currentTimeMillis());
				clientLikeContentService.insertLikeContentModel(likeContentModel);
			}catch (Exception e) {
				logger.error("同步喜欢信息到数据库出错！Message="+e.getMessage()+",Params="+action);
			}
			
			// 推送喜欢消息到客户端
			UserModel revUser = redisUserService.getUser(content.getUid());
			if (revUser != null && revUser.getDeviceToken() != null
					&& revUser.getDeviceToken().trim().length() >= 64) {
				SettingModel revSetting = redisUserService.getUserSetting(revUser.getUid());
				UserModel senderUser = redisUserService.getUser(action.getUid());
				int count = redisMessageService.countTotalNewMessage(revUser
						.getUid());
				Set<String> tokens = redisUserService.getUserDeviceToken(revUser.getUid());
				if (revSetting.getLikeMessage() == 1){
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, senderUser.getNickName()
									+ "喜欢了我的内容", count, "qupai://message/at");
							// PushSender.send(token, null, count, "message/at");
						}
					}
				} else {
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, null, count, "qupai://message/at");
						}
					}
				}
			}
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getLikeForwardMqKey()).rightPop();
		}
	}
	
	/**
	 * 分享腾讯微博
	 * @param uid
	 * @param description
	 * @param urlKey
	 * @param imageUrl
	 * @param atUserStr
	 */
	private void shareToTencent(long uid,String description,String urlKey,String imageUrl,String atUserStr){
		BindModel bind = redisUserService.getBindInfo(uid, BindModel.OpenType.TENCENT.getMark());
		String desc = EmojiUtils.decodeEmoji(description);
		String url = HoolaConfig.getWapHost() + urlKey + ".htm?from=qqweibo";
		String content = "";
		if (atUserStr != null){
			content = cutAtContent(desc, atUserStr);
		}else{
			content = cutShareContent(desc);
		}
		content += url + HoolaConfig.getComeFrom();
		content = content.replaceAll("@趣拍APP", "@qupaiapp");
		try {
			shareService.shareToQQWeibo(imageUrl, content,
					bind.getAccessToken(), bind.getOpenUid());
		} catch (Exception e) {
			logger.error("分享腾讯微博出错！" + e.getMessage());
		}
	}
	
	/**
	 * 分享新浪微薄
	 * @param uid	用户ID
	 * @param description	描述
	 * @param urlKey	内容访问链接Key
	 * @param imageUrl	封面图片链接
	 * @param atUserStr	如果是At则 不为空，否则为null
	 */
	private void shareToWeibo(long uid,String description,String urlKey,String imageUrl,String atUserStr){
		BindModel bind = redisUserService.getBindInfo(uid, BindModel.OpenType.SINA.getMark());
		String desc = EmojiUtils.decodeEmoji(description);
		String url = HoolaConfig.getWapHost() + urlKey + ".htm?from=sina";
		String content = "";
		if (atUserStr != null){
			content = cutAtContent(desc, atUserStr);
		}
		content += url + HoolaConfig.getComeFrom();
		try {
			shareService.shareToSina(imageUrl, content, bind.getAccessToken());
		} catch (Exception e) {
			logger.error("分享新浪微博出错！" + e.getMessage());
		}
	}
	/**
	 * 分享到QQ空间
	 * @param uid
	 * @param description
	 * @param urlKey
	 * @param imageUrl
	 * @param videoUrl
	 */
	private void shareToQzone(long uid,String description,String urlKey,String imageUrl,String videoUrl){
		BindModel bind = redisUserService.getBindInfo(uid, BindModel.OpenType.TENCENT.getMark());
		try{
			String desc = EmojiUtils.decodeEmoji(description);
			String url = HoolaConfig.getWapHost() + urlKey + ".htm";
			shareService.shareToQZone(SELF_DEFAULT_SHARE_TEXT, desc, url,
					HoolaConfig.getAliyunHDVideoDomain() + videoUrl,
					HoolaConfig.getAliyunThumbnailDomain() + imageUrl,
					bind.getAccessToken(), bind.getOpenUid());
		}catch (Exception e) {
			logger.error("分享QQ空间失败！"+e.getMessage()+"，qqBindModel="+bind);
		}
	}
	
	

	/**
	 * 推送动态到粉丝列表
	 * @param uid
	 * @param action
	 */
	@SuppressWarnings({ "rawtypes" })
	private void pushAction(long uid,ActionModel action){
		long beginTime = System.currentTimeMillis();
		//非名人进行推送
		Set fans = null;
		if (!redisUserService.isFamous(uid)){
			fans = redisRelationshipService.loadFansUid(uid,0,0);
			if (fans != null){
				for (Object obj : fans){
					try{
						long fansUid = Long.parseLong((String)obj);
						//TODO 发布和转发分开存储 暂时不执行
						redisTimelineService.insertUserTimeLine(fansUid, action);
					}catch(Exception e){
						logger.error("推送动态！"+e.getMessage());
					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		if (fans != null){
			System.out.println("------------推送了："+fans.size()+"条信息，总共耗时："+(endTime - beginTime)+"ms");
		}
	}
	
	/**
	 * 消息推送 pipeline 
	 * @param uid
	 * @param action
	 */
	@SuppressWarnings("rawtypes")
	private void pushAction2(long uid,ActionModel action){
		long beginTime = System.currentTimeMillis();
		final Object[] objs = new Object[2];
		//非名人进行推送
		Set fans = null;
		if (!redisUserService.isFamous(uid)){
			fans = redisRelationshipService.loadFansUid(uid,0,0);
			if (fans != null){
				objs[0] = fans;
				objs[1] = action;
				RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {  
		            @SuppressWarnings("unchecked")
					@Override  
		            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException { 
		                connection.openPipeline();
		                connection.multi();
		                Set fans = (Set)objs[0];
		                ActionModel action = (ActionModel)objs[1];
		                for (Object obj : fans){
							try{
								long fansUid = Long.parseLong((String)obj);
								String key = TimelineKeyManager.getPublicTimelineKey(fansUid);
								connection.zAdd(relationTemplate.getKeySerializer().serialize(key),
										System.currentTimeMillis(),
										relationTemplate.getValueSerializer().serialize(action.toString()));
							}catch(Exception e){
								logger.error("推送动态！"+e.getMessage());
							}
						}
		                return connection.closePipeline();  
		            }  
		              
		        };  
		        List<Object> results = (List<Object>)relationTemplate.execute(pipelineCallback);
			}
		}
		long endTime = System.currentTimeMillis();
		if (fans != null){
			System.out.println("============推送了："+fans.size()+"条信息，总共耗时："+(endTime - beginTime)+"ms");
		}
	}
	@Override
	public void run() {
		logger.error("+++++++++++"+Thread.currentThread().getName());
		this.handleContentAdd(0);
		
	}

	
	public void setClientLikeContentService(
			IClientLikeContentService clientLikeContentService) {
		this.clientLikeContentService = clientLikeContentService;
	}

	public void setClientForwardContentService(
			IClientForwardContentService clientForwardContentService) {
		this.clientForwardContentService = clientForwardContentService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}


	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

	public void setShareService(IShareService shareService) {
		this.shareService = shareService;
	}

	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setClientContentService(IClientContentService clientContentService) {
		this.clientContentService = clientContentService;
	}

	public void setCompressPublisher(IRedisPublisher compressPublisher) {
		this.compressPublisher = compressPublisher;
	}

	public void setRedisMessageService(IRedisMessageService redisMessageService) {
		this.redisMessageService = redisMessageService;
	}

	public void setRedisGroupService(IRedisGroupService redisGroupService) {
		this.redisGroupService = redisGroupService;
	}

	public void setRedisReportService(IRedisReportService redisReportService) {
		this.redisReportService = redisReportService;
	}

	public void setIndexBuilder(IIndexBuilder indexBuilder) {
		this.indexBuilder = indexBuilder;
	}
	
	public void setRedisHotService(IRedisHotService redisHotService) {
		this.redisHotService = redisHotService;
	}
	
	
	
}
