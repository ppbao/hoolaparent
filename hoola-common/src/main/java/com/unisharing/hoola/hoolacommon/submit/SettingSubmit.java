package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;

public class SettingSubmit implements Serializable {

	private static final long serialVersionUID = 6362465262052071181L;
	int recommend  = 1;//编辑推荐
	int share = 1;//喜欢时自动同步
	int atMessage = 1;//At消息
	int likeMessage = 0;//喜欢消息
	int commentMessage = 1;//评论消息
	int message = 1;//私信消息
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getAtMessage() {
		return atMessage;
	}
	public void setAtMessage(int atMessage) {
		this.atMessage = atMessage;
	}
	public int getLikeMessage() {
		return likeMessage;
	}
	public void setLikeMessage(int likeMessage) {
		this.likeMessage = likeMessage;
	}
	public int getCommentMessage() {
		return commentMessage;
	}
	public void setCommentMessage(int commentMessage) {
		this.commentMessage = commentMessage;
	}
	public int getMessage() {
		return message;
	}
	public void setMessage(int message) {
		this.message = message;
	}
}
