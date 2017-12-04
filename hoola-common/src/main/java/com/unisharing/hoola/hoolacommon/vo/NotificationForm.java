package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class NotificationForm implements Serializable{
	
	private static final long serialVersionUID = 2590697399478947565L;
	int timelineNum = 0; //动态新数据
	int hotNum = 0;	//发现新数据
	int newNum = 0;	//最新新数据
	int atMessageNum = 0;	//at消息新数据
	int commentMessageNum = 0;	//评论消息新数据
	int messageNum = 0;	//私信新消息
	int friendNum = 0;	//粉丝新消息
	int recFriendNum = 0;//推荐新消息
  //int editorRecFriendNum;//编辑推荐的好友
	int isCamera = 0;
	public int getTimelineNum() {
		return timelineNum;
	}
	public void setTimelineNum(int timelineNum) {
		this.timelineNum = timelineNum;
	}
	public int getHotNum() {
		return hotNum;
	}
	public void setHotNum(int hotNum) {
		this.hotNum = hotNum;
	}
	public int getNewNum() {
		return newNum;
	}
	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}
	public int getAtMessageNum() {
		return atMessageNum;
	}
	public void setAtMessageNum(int atMessageNum) {
		this.atMessageNum = atMessageNum;
	}
	public int getCommentMessageNum() {
		return commentMessageNum;
	}
	public void setCommentMessageNum(int commentMessageNum) {
		this.commentMessageNum = commentMessageNum;
	}
	public int getMessageNum() {
		return messageNum;
	}
	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}
	public int getFriendNum() {
		return friendNum;
	}
	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}
	public int getIsCamera() {
		return isCamera;
	}
	public void setIsCamera(int isCamera) {
		this.isCamera = isCamera;
	}
	public int getRecFriendNum() {
		return recFriendNum;
	}
	public void setRecFriendNum(int recFriendNum) {
		this.recFriendNum = recFriendNum;
	}
/*	public int getEditorRecFriendNum() {
		return editorRecFriendNum;
	}
	public void setEditorRecFriendNum(int editorRecFriendNum) {
		this.editorRecFriendNum = editorRecFriendNum;
	}*/
	
}
