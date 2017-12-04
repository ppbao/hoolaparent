package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class CommentMessageForm implements Serializable{
	
	private static final long serialVersionUID = -8547633855112673360L;
	SimpleUserForm user; //评论用户
	ActionForm action;//对应的内容；
	String commentText;//评论内容
	long commentId;//评论ID
	String memo;//备注（评论我的内容，回复我的评论）
	long time;//评论时间
	int isNew;//是否为新消息
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
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	
}
