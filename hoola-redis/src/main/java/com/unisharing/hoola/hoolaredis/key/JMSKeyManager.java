package com.unisharing.hoola.hoolaredis.key;

public class JMSKeyManager {
	
	private static String NEWUSER_MQ_KEY = "l:jms:user:new";//新增用户消息队列
	
	private static String NEWUSER_ERROR_LIST_KEY = "l:jms:user:error";//新增用户消息处理失败消息
	
	private static String NEWCONTENT_MQ_KEY = "l:jms:content:new";//新增内容消息队列
	
	private static String NEWCONTENT_ERROR_LIST_KEY = "l:jms:content:error";//新增内容消息处理失败消息
	
	private static String FOLLOW_MQ_KEY = "l:jms:follows"; //新增关注消息队列
	
	private static String UNFOLLOW_MQ_KEY = "l:jms:unfollows"; //取消关注消息队列
	
	public static String NEW_BINDING_MQ_KEY = "l:jms:binding:new";//新增绑定数据
	
	public static String LIKE_MQ_KEY = "l:jms:like";//用户喜欢内容消息队列
	
	public static String DISLIKE_MQ_KEY = "l:jms:dislike";//用户不喜欢内容消息队列
	
	public static String FORWARD_MQ_KEY = "l:jms:forward";//用户转发内容消息队列
	
	public static String CANCEL_FORWARD_MQ_KEY = "l:jms:cancel:forward";
	
	public static String COMMENT_MQ_KEY  = "l:jms:comment";//用户评论
	
	public static String MESSAGE_MQ_KEY = "l:jms:message";//发送私信
	
	public static String DELETE_DIALOG_MQ_KEY = "l:jms:delete:dialog";//删除对话消息
	
	public static String INVITE_MQ_KEY = "l:jms:invite";//邀请好友
	
	public static String SHARE_MQ_KEY = "l:jms:share";//分享消息队列Key
	
	public static String FEEDBACK_MQ_KEY = "l:jms:feedback";//建议反馈
	
	public static String REPORT_MQ_KEY = "l:jms:report";//举报
	
	public static String AT_MQ_KEY = "l:jms:at";//用户At内容消息队列
	
	public static String GROUP_ADD_MQ_KEY ="l:jms:group:add";//组添加
	
	public static String GROUP_EDIT_MQ_KEY ="l:jms:group:edit";//组编辑
	
	public static String GROUP_DELETE_MQ_KEY ="l:jms:group:delete";//组删除
	
	public static String GROUP_USER_ADD_MQ_KEY = "l:jms:group:users:add";//添加组成员
	
	public static String GROUP_USER_DELETE_MQ_KEY = "l:jms:group:users:delete";//删除组成员
	
	public static String OPEN_FRIEND_SYN_MQ_KEY = "l:jms:openfriend:syn"; //第三方好友同步
	
	public static String MOBILE_UP_MQ_KEY = "l:jms:mobiles";//通讯录上传消息队列
	
	public static String USER_LOGIN_MQ_KEY = "l:jms:login";//用户登录信息
	
	public static String USER_EDIT_MQ_KEY = "l:jms:user:edit";//用户编辑信息
	
	public static String LIKE_FORWARD_MQ_KEY = "l:jms:like:forward";//用户喜欢并转发消息队列
	
	public static String CANCEL_LIKE_FORWARD_MQ_KEY = "l:jms:like:forward:cancel";//用户取消喜欢并关注消息队列
	
	public static String AUTO_FOLLOW_QUPAIAPP = "auto:follow:qupaiapp";//用户是否自动关注HoolaAPP
	
	public static String AUTO_UPLOAD_STATUS = "auto:upload:status";//注册时自动发布一条微博
	
	public static String SYN_WEIBO_MQ_KEY = "syn:weibo";//注册同步微博消息队列
	
	public static String CONVERT_GIF ="l:jms:convert";//格式转化
	
	
	/**
	 * 取得新增用户消息队列Key
	 * @return
	 */
	public static String getNewUserMQKey(){
		return NEWUSER_MQ_KEY;
	}
	
	/**
	 * 取得新增用户失败信息List key
	 * @return
	 */
	public static String getNewUserErrorListKey(){
		return NEWUSER_ERROR_LIST_KEY;
	}
	
	/**
	 * 取得新建内容MQ key
	 * @return
	 */
	public static String getNewContentMQKey(){
		return NEWCONTENT_MQ_KEY;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getNewContentErrorListKey(){
		return NEWCONTENT_ERROR_LIST_KEY;
	}
	
	/**
	 * 新增Follow消息队列key
	 * @return
	 */
	public static String getNewFollowListKey(){
		return FOLLOW_MQ_KEY;
	}
	
	/**
	 * 取消Follow消息队列key
	 * @return
	 */
	public static String getUnFollowListKey(){
		return UNFOLLOW_MQ_KEY;
	}
	
	/**
	 * 新增绑定消息队列Key
	 * @return
	 */
	public static String getBindingListKey(){
		return NEW_BINDING_MQ_KEY;
	}
	
	/**
	 * 用户新增喜欢内容消息队列
	 * @return
	 */
	public static String getLikeListKey(){
		return LIKE_MQ_KEY;
	}
	
	/**
	 * 用户新增转发内容消息队列
	 * @return
	 */
	public static String getForwardListKey(){
		return FORWARD_MQ_KEY;
	}
	
	/**
	 * 取消转发内容消息队列Key
	 * @return
	 */
	public static String getCancelForwardListKey(){
		return CANCEL_FORWARD_MQ_KEY;
	}
	
	/**
	 * 用户不喜欢内容消息队列
	 * @return
	 */
	public static String getDislikeListKey(){
		return DISLIKE_MQ_KEY;
	}
	
	/**
	 * 用户评论消息队列
	 * @return
	 */
	public static String getCommentListKey(){
		return COMMENT_MQ_KEY;
	}

	/**
	 * 发送私信消息队列
	 * @return
	 */
	public static String getMessageListKey(){
		return MESSAGE_MQ_KEY;
	}
	
	/**
	 * 取得删除对话的消息队列key
	 * @return
	 */
	public static String getDeleteDialogListKey(){
		return DELETE_DIALOG_MQ_KEY;
	}
	
	/**
	 * 邀请好友消息队列Key
	 * @return
	 */
	public static String getInviteListKey(){
		return INVITE_MQ_KEY;
	}
	
	/**
	 * 分享
	 * @return
	 */
	public static String getShareListKey(){
		return SHARE_MQ_KEY;
	}
	
	/**
	 * 意见反馈
	 * @return
	 */
	public static String getFeedBackListKey(){
		return FEEDBACK_MQ_KEY;
	}
	
	/**
	 * 举报
	 * @return
	 */
	public static String getReportListKey(){
		return REPORT_MQ_KEY;
	}
	
	/**
	 * At消息队列
	 * @return
	 */
	public static String getAtListKey(){
		return AT_MQ_KEY;
	}
	
	/**
	 * 组添加消息队列
	 * @return
	 */
	public static String getGroupAddListKey(){
		return GROUP_ADD_MQ_KEY;
	}
	
	/**
	 * 组删除消息队列
	 * @return
	 */
	public static String getGroupDeleteListKey(){
		return GROUP_DELETE_MQ_KEY;
	}
	
	/**
	 * 组编辑消息队列
	 * @return
	 */
	public static String getGroupEditListKey(){
		return GROUP_EDIT_MQ_KEY;
	}
	
	/**
	 * 组成员添加消息队列
	 * @return
	 */
	public static String getGroupUsersAddListKey(){
		return GROUP_USER_ADD_MQ_KEY;
	}
	
	/**
	 * 组成员删除消息队列
	 * @return
	 */
	public static String getGroupUsersDeleteListKey(){
		return GROUP_USER_DELETE_MQ_KEY;
	}
	
	/**
	 * 取得同步好友信息key
	 * @return
	 */
	public static String getOpenFriendSynListKey(){
		return OPEN_FRIEND_SYN_MQ_KEY;
	}
	
	/**
	 * 通讯录上传
	 * @return
	 */
	public static String getMobilesListkey(){
		return MOBILE_UP_MQ_KEY;
	}
	
	/**
	 * 登录消息队列Key
	 * @return
	 */
	public static String getLoginListKey(){
		return USER_LOGIN_MQ_KEY;
	}
	
	/**
	 * 用户修改信息消息队列
	 * @return
	 */
	public static String getUserEditKey(){
		return USER_EDIT_MQ_KEY;
	}
	
	/**
	 * 取得自动关注趣拍APP开关Key
	 * @return
	 */
	public static String getAutoFollowFlag(){
		return AUTO_FOLLOW_QUPAIAPP;
	}
	
	/**
	 * 取得注册时自动发布一条微博开关Key
	 * @return
	 */
	public static String getAutoUploadStatusFlag(){
		return AUTO_UPLOAD_STATUS;
	}
	
	/**
	 * 取得喜欢并转发消息队列
	 * @return
	 */
	public static String getLikeForwardMqKey(){
		return LIKE_FORWARD_MQ_KEY;
	}
	
	/**
	 * 取得取消喜欢并转发消息队列
	 * @return
	 */
	public static String getCancelLikeForwardMqKey(){
		return CANCEL_LIKE_FORWARD_MQ_KEY;
	}
	
	/**
	 * 注册同步微博消息队列
	 * @return
	 */
	public static String getSynWeiboMqKey(){
		return SYN_WEIBO_MQ_KEY;
	}
	
	/**
	 * 取得格式转化队列名
	 * @return
	 */
	public static String getConvertMqKey(){
		return CONVERT_GIF;
	}
}
