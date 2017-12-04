package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class DialogForm implements Serializable {
	
	private static final long serialVersionUID = -7121933172757864671L;
	SimpleUserForm user;//
	int newNum;//新消息数
	String lastMessage;//最后一条消息
	long time;//最后一条消息时间
	public SimpleUserForm getUser() {
		return user;
	}
	public int getNewNum() {
		return newNum;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setUser(SimpleUserForm user) {
		this.user = user;
	}
	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
