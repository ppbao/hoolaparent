package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class MessageForm implements Serializable {
	
	private static final long serialVersionUID = 9009885255795125082L;
	
	private long id;//私信ID
	private String messageText;//私信内容
	private long createTime;//发送时间
	private SimpleUserForm user;//发送者信息 查询我发送的私信列表则该属性为null
	//private UserForm replyUser;//接收者信息 查询我收到的私信则该属性为null
	public long getId() {
		return id;
	}
	public String getMessageText() {
		return messageText;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public SimpleUserForm getUser() {
		return user;
	}
	public void setUser(SimpleUserForm user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "MessageForm [createTime=" + createTime + ", id=" + id
				+ ", messageText=" + messageText + ", user=" + user + "]";
	}

}
