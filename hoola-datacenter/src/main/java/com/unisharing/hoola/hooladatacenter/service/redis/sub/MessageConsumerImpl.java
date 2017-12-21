package com.unisharing.hoola.hooladatacenter.service.redis.sub;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hooladatacenter.service.comment.ICommentService;
import com.unisharing.hoola.hooladatacenter.service.content.IContentService;
import com.unisharing.hoola.hooladatacenter.service.group.IGroupService;
import com.unisharing.hoola.hooladatacenter.service.message.IMessageService;
import com.unisharing.hoola.hooladatacenter.service.system.ISystemService;
import com.unisharing.hoola.hooladatacenter.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumerImpl implements MessageListener {
	@Autowired
    IContentService contentService;
	@Autowired
    IUserService userService;
	@Autowired
	ICommentService commentService;
	@Autowired
    IMessageService messageService;
	@Autowired
    ISystemService systemService;
	@Autowired
	IGroupService groupService;
	@Autowired
	TaskExecutor taskExecutor;

	@Override
	public void onMessage(Message message, byte[] pattern) {

		NoticeMessage notice = JSON.parseObject(message.getBody(),
				NoticeMessage.class);
		// if (notice.getReceiver().equals(HoolaUtils.getIp())) {
		// 内容上传
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.CONTENT_ADD)) {
//			taskExecutor.execute(this.contentService);
			contentService.handleContentAdd(notice.getObjId());
			return;
		}
		
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.CONTENT_DELETE)) {
			contentService.handleContentDelete(notice.getObjId());
			return;
		}

		// }

		// 用户注册
		if (notice.getMessageType().equals(NoticeMessage.MessageType.USER_ADD)) {
			//TODO
//			taskExecutor.execute(this.userService);
//			
//			ThreadPoolTaskExecutor exe = (ThreadPoolTaskExecutor)taskExecutor;
//			System.out.println("-------------"+exe.getThreadPoolExecutor().getTaskCount());
			
			userService.handleUserRegister();
			return;
		}
		
		// 用户编辑
		if (notice.getMessageType().equals(NoticeMessage.MessageType.USER_EDIT)) {
			userService.handleUserEdit();
			return;
		}
		
		//用户登录
		if (notice.getMessageType().equals(NoticeMessage.MessageType.LOGIN)){
			userService.handleLogin();
			return;
		}

		// 用户绑定
		if (notice.getMessageType().equals(NoticeMessage.MessageType.BINDING)) {
			userService.handleUserBinding(notice.getObjId());
			return;
		}

		// 用户喜欢
		if (notice.getMessageType().equals(NoticeMessage.MessageType.LIKE)) {
			contentService.handelContentLike();
			return;
		}

		// 转发
		if (notice.getMessageType().equals(NoticeMessage.MessageType.FORWARD)) {
			contentService.handleForwardContent();
			return;
		}

		// 取消转发
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.CANCEL_FORWARD)) {
			contentService.handleCancelForwardContent();
			return;
		}

		// 用户不喜欢
		if (notice.getMessageType().equals(NoticeMessage.MessageType.DISLIKE)) {
			contentService.handelContentDislike();
			return;
		}

		// 关注
		if (notice.getMessageType().equals(NoticeMessage.MessageType.FOLLOW)) {
			userService.handleFollow();
			return;
		}

		// 取消关注
		// 关注
		if (notice.getMessageType().equals(NoticeMessage.MessageType.UNFOLLOW)) {
			userService.handleUnfollow();
			return;
		}

		// 发表评论
		if (notice.getMessageType().equals(NoticeMessage.MessageType.COMMENT)) {
			commentService.handleCommentAdd();
			return;
		}

		// 删除评论
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.DELETE_COMMENT)) {
			commentService.handelCommentDelete(notice.getObjId());
		}

		// 发送私信
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.SEND_MESSAGE)) {
			messageService.handleSendMessage();
			return;
		}
		// 删除私信
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.DELETE_MESSAGE)) {
			messageService.handleDeleteMessage(notice.getObjId());
			return;
		}
		// 删除对话
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.DELETE_DIALOG)) {
			messageService.handleDeleteDialog();
			return;
		}

		// 分享
		if (notice.getMessageType().equals(NoticeMessage.MessageType.SHARE)) {
			contentService.handleShareContent();
			return;
		}
		// at
		if (notice.getMessageType()
				.equals(NoticeMessage.MessageType.AT_MESSAGE)) {
			contentService.handleAtContent();
			return;
		}
		// 邀请好友
		if (notice.getMessageType().equals(NoticeMessage.MessageType.INVITE)) {
			userService.handleInvite();
			return;
		}
		// 反馈
		if (notice.getMessageType().equals(NoticeMessage.MessageType.FEEDBACK)) {
			systemService.handleFeedback();
			return;
		}
		// 举报
		if (notice.getMessageType().equals(NoticeMessage.MessageType.REPORT)) {
			systemService.handleReport();
			return;
		}

		// 添加组
		if (notice.getMessageType().equals(NoticeMessage.MessageType.GROUP_ADD)) {
			groupService.handleGroupAdd();
			return;
		}

		// 删除组
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.GROUP_DELETE)) {
			groupService.handleGroupDelete();
			return;
		}

		// 编辑组
		if (notice.getMessageType()
				.equals(NoticeMessage.MessageType.GROUP_EDIT)) {
			groupService.handleGroupEdit();
			return;
		}

		// 组成员添加
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.GROUP_USER_ADD)) {
			groupService.handleGroupUsersAdd();
			return;
		}

		// 组成员删除
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.GROUP_USER_DELETE)) {
			groupService.handleGroupEdit();
			return;
		}

		// 同步第三方好友
		if (notice.getMessageType().equals(
				NoticeMessage.MessageType.OPEN_FRIEND_SYN)) {
			systemService.handleOpenFriendSyn();
			return;
		}

		// 保存手机通讯录
		if (notice.getMessageType().equals(NoticeMessage.MessageType.MOBILE_UP)) {
			userService.handleMobileUp();
			return;
		}
		
		// 喜欢关注
		if (notice.getMessageType().equals(NoticeMessage.MessageType.LIKE_FORWARD)){
			contentService.handleLikeAndForwardContent();
			return;
		}
		
		// 取消喜欢关注
		if (notice.getMessageType().equals(NoticeMessage.MessageType.CANCEL_LIKE_FORWARD)){
			contentService.handleCancelLikeAndForwardContent();
			return;
		}
		
		//同步微博
		if(notice.getMessageType().equals(NoticeMessage.MessageType.SYN_WEIBO)){
			userService.handleSynWeibo();
			return ;
		}

	}

	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public void setSystemService(ISystemService systemService) {
		this.systemService = systemService;
	}

	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
