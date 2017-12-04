package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class AtMessageForm implements Serializable {
	private static final long serialVersionUID = -642710239542992904L;
	private SimpleUserForm user;//发送At信息用户信息
	private ActionForm action;//内容
	private String memo = "";//备注信息;
	private int actionType;
	private long time;
	private int isNew;
	public SimpleUserForm getUser() {
		return user;
	}
	public void setUser(SimpleUserForm user) {
		this.user = user;
	}
	public ActionForm getAction() {
		return action;
	}
	public void setAction(ActionForm action) {
		this.action = action;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
}
