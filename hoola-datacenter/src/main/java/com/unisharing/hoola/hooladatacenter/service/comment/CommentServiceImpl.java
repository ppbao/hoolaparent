package com.unisharing.hoola.hooladatacenter.service.comment;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.comment.IClientCommentService;
import com.unisharing.hoola.hoolacommon.model.CommentModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.SettingModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.utils.PushSender;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.comment.IRedisCommentService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class CommentServiceImpl extends BaseRedisService implements ICommentService {
	
	Log logger = LogFactory.getLog(CommentServiceImpl.class);
	@Autowired
	IRedisCommentService redisCommentService;
	@Autowired
	IClientCommentService clientCommentService;
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisMessageService redisMessageService;
	@Autowired
	IRedisTimelineService redisTimelineService;

	@SuppressWarnings("unchecked")
	@Override
	public void handleCommentAdd() {
		String json = null;
		CommentModel comment = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCommentListKey()).rightPop();
		while(json != null){
			comment = JSON.parseObject(json, CommentModel.class);
			
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(comment.getCid());
			
			//推送消息到评论消息列表
			redisCommentService.insertUserCommentMessage(comment.getId());
			String msg = "";
			
			//推送消息到客户端
			long revUid = 0;
			if (comment.getParentId() == 0){
				//直接评论
				ContentModel content = redisContentService.getContent(comment.getCid());
				msg = "评论了我的内容";
				revUid = content.getUid();
			}else{
				revUid = comment.getReplyUid();
				msg = "回复了我的评论";
			}
			
			if (revUid != 0) {
				UserModel revUser = redisUserService.getUser(revUid);
				if (revUser != null && revUser.getDeviceToken() != null
						&& revUser.getDeviceToken().trim().length() >= 64) {
					int count = redisMessageService.countTotalNewMessage(revUid);
					Set<String> tokens = redisUserService.getUserDeviceToken(revUser.getUid());
					SettingModel setting = redisUserService.getUserSetting(revUid);
					if (setting.getCommentMessage() == 1) {
						UserModel senderUser = redisUserService.getUser(comment.getUid());
						if (revUser.getDeviceToken() != null && revUser.getDeviceToken().trim().length() > 0) {
							for (String token : tokens) {
								if (token.trim().length() == 64) {
									PushSender.send(token, senderUser.getNickName()	+ msg, count, "hoola://message/comments");
								}
							}
						}
					}else{
						if (revUser.getDeviceToken() != null && revUser.getDeviceToken().trim().length() > 0) {
							for (String token : tokens) {
								if (token.trim().length() == 64) {
									PushSender.send(token, null, count, "hoola://message/comments");//只推送角标数字
								}
							}
						}
					}
				}
			}
			try{
				//插入数据库
				clientCommentService.insertContentComment(comment);
			}catch(Exception e){
				logger.error("评论入库失败：Message="+e.getMessage()+";Params="+comment);
			}
			
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getCommentListKey()).rightPop();
		}
	}
	
	@Override
	public void handelCommentDelete(long id) {
		CommentModel commentModel = redisCommentService.getComment(id);
		if (commentModel!= null && commentModel.getId()>0){
			//删除评论
			int count = redisCommentService.deleteComment(commentModel);
			//更新内容操作用户
			redisTimelineService.refreshOptUsersCache(commentModel.getCid());
		}else{
			commentModel = new CommentModel();
			commentModel.setId(id);
		}
		try{
			clientCommentService.deleteContentComment(commentModel);
		}catch(Exception e){
			logger.error("删除评论出错！"+e);
		}
		
	}

	public void setRedisCommentService(IRedisCommentService redisCommentService) {
		this.redisCommentService = redisCommentService;
	}

	public void setClientCommentService(IClientCommentService clientCommentService) {
		this.clientCommentService = clientCommentService;
	}

	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisMessageService(IRedisMessageService redisMessageService) {
		this.redisMessageService = redisMessageService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}
}
