package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;
import java.util.List;

public class CommentForm implements Serializable {
	
	private static final long serialVersionUID = -4842853392558105217L;
	private long id;//评论ID	
	private String commentText;// 评论内容
	private long createTime;//发表时间
	
	
	private SimpleUserForm user;//发表者
	private SimpleUserForm replyUser;//被回复者 非回复评论该属性为null
	private List<CommentForm> replyComments; //该评论的子评论列表

	public long getId() {
		return id;
	}

	public String getCommentText() {
		return commentText;
	}

	public long getCreateTime() {
		return createTime;
	}

	public List<CommentForm> getReplyComments() {
		return replyComments;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public SimpleUserForm getUser() {
		return user;
	}

	public SimpleUserForm getReplyUser() {
		return replyUser;
	}

	public void setUser(SimpleUserForm user) {
		this.user = user;
	}

	public void setReplyUser(SimpleUserForm replyUser) {
		this.replyUser = replyUser;
	}

	public void setReplyComments(List<CommentForm> replyComments) {
		this.replyComments = replyComments;
	}

}
