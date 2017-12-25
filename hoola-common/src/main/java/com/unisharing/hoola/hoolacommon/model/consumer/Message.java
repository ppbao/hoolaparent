package com.unisharing.hoola.hoolacommon.model.consumer;

public class Message {
	
	enum MessageType{
		USER_FOLLOW,//关注用户
		USER_UNFOLLOE,//取消关注用户
		
		CONTENT_CREATE,//发布内容
		CONTENT_DELETE,//删除内容
		CONTENT_LIKE,//喜欢内容
		CONTENT_SHARE,//转发内容（分享到Hoola）
		CONTENT_AT,//@内容
		CONTENT_SAVE,//保存本地
		CONTENT_SHARE_OTHER,//分享内容到第三方
		
		COMMENT_ADD,//评论
		COMMENT_REPLY,//回复
		
		SEND_MESSAGE,//发私信
		MESSAGE_REPLY//回复私信
	}

}
