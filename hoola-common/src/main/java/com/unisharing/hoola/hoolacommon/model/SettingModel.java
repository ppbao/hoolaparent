package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class SettingModel implements Serializable {
	
	private static final long serialVersionUID = 6951303955595422242L;
	int recommend  = 1;//编辑推荐
	int share = 1;//喜欢时自动同步
	int atMessage = 1;//At消息
	int likeMessage = 0;//喜欢消息
	int commentMessage = 1;//评论消息
	int message = 1;//私信消息
	int forwardMessage = 0;//转发消息
	int isCamera = 0;//直接进入相机 0:首页；1：相机；2:热门
	public int getRecommend() {
		return recommend;
	}
	public int getShare() {
		return share;
	}
	public int getAtMessage() {
		return atMessage;
	}
	public int getLikeMessage() {
		return likeMessage;
	}
	public int getCommentMessage() {
		return commentMessage;
	}
	public int getMessage() {
		return message;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public void setAtMessage(int atMessage) {
		this.atMessage = atMessage;
	}
	public void setLikeMessage(int likeMessage) {
		this.likeMessage = likeMessage;
	}
	public void setCommentMessage(int commentMessage) {
		this.commentMessage = commentMessage;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	public int getForwardMessage() {
		return forwardMessage;
	}
	public void setForwardMessage(int forwardMessage) {
		this.forwardMessage = forwardMessage;
	}
	public int getIsCamera() {
		return isCamera;
	}
	public void setIsCamera(int isCamera) {
		this.isCamera = isCamera;
	}
}
