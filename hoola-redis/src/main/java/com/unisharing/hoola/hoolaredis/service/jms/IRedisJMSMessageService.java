package com.unisharing.hoola.hoolaredis.service.jms;

import com.unisharing.hoola.hoolacommon.bean.*;
import com.unisharing.hoola.hoolacommon.model.*;

public interface IRedisJMSMessageService {
	
	/**
	 * 插入新增内容数据到消息队列
	 * @param bean //JSON.toJsonString(ContentSubmit)
	 */
	public long insertAddContentMessageQueue(ContentBean bean);
	
	/**
	 * 插入待转化视频
	 * @param bean
	 * @return
	 */
	public long insertConvertMessageQueue(ContentBean bean);
	
	/**
	 * 插入新增关注数据到消息队列
	 * @param bean
	 * @return
	 */
	public long insertFollowMessageQueue(FollowBean bean);
	
	/**
	 * 插入取消关注数据到消息队列
	 * @param bean
	 * @return
	 */
	public long insertUnFollowMessageQueue(FollowBean bean);
	
	/**
	 * 插入新增用户数据到消息队列
	 * @param model
	 * @return
	 */
	public long insertNewUserMessageQueue(UserModel model);
	
	/**
	 * 插入绑定帐号消息队列
	 * @param bind
	 * @return
	 */
	public long insertBindingMessageQueue(BindModel bind);
	
	/**
	 * 插入喜欢内容消息队列
	 * @param action
	 * @return
	 */
	public long insertLikeMessageQueue(ActionModel action);
	
	/**
	 * 插入转发内容消息队列
	 * @param action
	 * @return
	 */
	public long insertForwardMessageQueue(ActionModel action);
	
	/**
	 * 插入取消转发内容消息队列
	 * @param action
	 * @return
	 */
	public long insertCancelForwardMessageQueue(ActionModel action);
	
	/**
	 * 删除转发内容消息队列;用户反复点击可以不用处理
	 */
	
	public long deleteForwardMessageQueue(ActionModel action);
	
	/**
	 * 删除取消转发内容消息队列，用户反复点击可以减少处理次数
	 * @param action
	 * @return
	 */
	public long deleteCancelForwardMessageQueue(ActionModel action);
	
	/**
	 * 删除喜欢内容消息队列;用户反复点击可以不用处理
	 */
	
	public long deleteLikeMessageQueue(ActionModel action);
	
	/**
	 * 插入不喜欢内容消息队列
	 * @param action
	 * @return
	 */
	public long insertDislikeMessageQueue(ActionModel action);
	
	/**
	 * 删除不喜欢内容消息队列，用户反复点击可以减少处理次数
	 * @param action
	 * @return
	 */
	public long deleteDislikeMessageQueue(ActionModel action);
	
	/**
	 * 插入评论消息队列
	 * @param action
	 * @return
	 */
	public long insertCommentMessageQueue(CommentModel action);
	
	/**
	 * 发送私信插入消息队列
	 * @param message
	 * @return
	 */
	public long insertSendMessageQueue(MessageModel message);
	
	/**
	 * 插入删除对话消息队列
	 * @param dialog
	 * @return
	 */
	public long insertDeleteDialogQueue(DialogBean dialog);
	
	//手机验证码相关
	/**
	 * 插入手验证码
	 * @param mobile
	 * @param code
	 */
	public void addMobileCode(String mobile, String code);
	
	/**
	 * 获取手机验证码
	 * @param mobile
	 */
	public String getMobileCode(String mobile);
	
	/**
	 * 删除手机验证码
	 * @param mobile
	 */
	public void deleteMobileCode(String mobile);
	
	/**
	 * 插入邀请消息队列
	 * @param invite
	 */
	public void insertInviteQueue(InviteBean invite);
	
	/**
	 * 插入分享消息队列
	 * @param bean
	 */
	public void insertShareQueue(ShareBean bean);
	
	/**
	 * 插入At消息队列
	 * @param bean
	 */
	public void insertAtQueue(AtMessageBean bean);
	
	
	/**
	 * 意见反馈消息队列
	 * @param model
	 */
	public void insertFeedBackQueue(FeedBackModel model);
	
	/**
	 * 举报消息队列
	 * @param report
	 */
	public void insertReportQueue(ReportModel report);
	
	/**
	 * 添加分组消息队列
	 * @param group
	 */
	public void insertGroupAddQueue(GroupBean group);
	
	/**
	 * 编辑组消息队列
	 * @param group
	 */
	public void insertGroupEditQueue(GroupBean group);
	
	/**
	 * 删除组消息队列
	 * @param group
	 */
	public void insertGroupDeleteQueue(GroupBean group);
	
	/**
	 * 删除组成员消息队列
	 * @param group
	 */
	public void insertGroupUsersDeleteQueue(GroupBean group);
	
	/**
	 * 添加组成员消息队列
	 * @param group
	 */
	public void insertGroupUsersAddQueue(GroupBean group);
	
	/**
	 * 添加数据同步消息From  DB TO REDIS
	 * 
	 */
	
	public void insertOpenFrienDataSynFromDB(OpenFriend openFriend);
	
	/**
	 * 通讯录上传消息队列
	 * @param mobiles
	 */
	public void insertContactsUpQueue(MobileMessageBean mobiles);
	
	/**
	 * 用户登录信息
	 */
	public void insertUserLoginQueue(BindModel bind);
	
	/**
	 * 编辑用户信息
	 */
	public void insertUserEditQueue(UserModel user);
	
	/**
	 * 取得自动关注趣拍APP开关
	 * @return
	 */
	public int getAutoFollowFlag();
	
	/**
	 * 取得注册自动发布新浪微博开关
	 * @return
	 */
	public int getAutoUploadStatusFlag();
	
	/**
	 * 插入喜欢转发消息
	 */
	public void insertLikeForwardQueue(ActionModel action);
	
	/**
	 * 插入取消喜欢转发消息
	 */
	public void insertCancelLikeForwardQueue(ActionModel action);
	
	/**
	 * 删除喜欢转发消息队列
	 * @param action
	 */
	public long deleteLikeForwardQueue(ActionModel action);
	
	/**
	 * 删除取消喜欢转发消息队列
	 * @param action
	 */
	public long deleteCancelLikeForwardQueue(ActionModel action);
	
	/**
	 * 插入注册同步新浪微博消息队列
	 * @param uid
	 */
	public void insertSynWeiboQueue(long uid);
}
