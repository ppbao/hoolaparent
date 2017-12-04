package com.unisharing.hoola.hoolacommon;

import java.io.Serializable;

public class NoticeMessage implements Serializable{

	public NoticeMessage() {
		super();
	}

	public NoticeMessage(String sender, String receiver, MessageType messageType) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.messageType = messageType;
	}

	public NoticeMessage(String sender, String receiver,
			MessageType messageType, long objId) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.messageType = messageType;
		this.objId = objId;
	}

	private String sender;// 多台服务器-发起者
	private String receiver;// 接收者-可以指定接收消息处理者，一般为谁发起谁处理
	//private String objId;//对象ID

	private MessageType messageType;
	private long objId;//消息ID

	public enum MessageType {
		CONTENT_ADD, // 内容发布
		CONTENT_DELETE, // 内容删除
		REPORT,//举报

		USER_ADD, // 用户注册
		USER_EDIT, // 用户修改信息
		LOGIN, // 用户登录
		BINDING,//第三方平台绑定
		BIND_MOBILE,//绑定手机号码
		UPLOAD_CONTACTS,//上传通讯录
		INVITE,//邀请好友
		MOBILE_UP,//通讯录上传

		FOLLOW, // 关注
		UNFOLLOW, // 取消关注

		COMMENT, // 评论内容
		DELETE_COMMENT,//删除评论
		FORWARD, // 转发
		CANCEL_FORWARD,//取消转发
		LIKE, // 喜欢内容
		DISLIKE,//不喜欢
		SHARE, // 分享
		SAVE, // 保存本地
		LIKE_FORWARD,//喜欢并转发
		CANCEL_LIKE_FORWARD,//取消喜欢并转发

		SEND_MESSAGE, // 发私信
		DELETE_MESSAGE,//删除私信
		DELETE_DIALOG,//删除对话
		AT_MESSAGE, // 发@信息
		DELETE_ATMESSAGE,//删除@消息
		
		GROUP_ADD,//添加组
		GROUP_DELETE,//删除组
		GROUP_EDIT,//编辑组
		GROUP_USER_ADD,//添加组成员
		GROUP_USER_DELETE,//删除组成员
		
		SETTING,// 设置
		TEST,//测试
		COMPRESS,//压缩
		FEEDBACK,//意见反馈
		
		OPEN_FRIEND_SYN,//好友同步
		SYN_WEIBO,//注册同步微博
		CONVERT2GIF//生成GIF
	}

	public String getSender() {
		return sender;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Override
	public String toString() {
		return "NoticeMessage [messageType=" + messageType + ", receiver="
				+ receiver + ", sender=" + sender + "]";
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

}
