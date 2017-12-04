package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class TalentForm implements Serializable {
	private static final long serialVersionUID = 8963873689066585494L;
	private long uid;//id
	private String nickName;//昵称
	private String avatar;//头像
	private int fansCount;//粉丝数（26万）
	private String fansCountShow;//粉丝数
	private int contentCount;//内容数
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public String getFansCountShow() {
		return fansCountShow;
	}
	public void setFansCountShow(String fansCountShow) {
		this.fansCountShow = fansCountShow;
	}
	public int getContentCount() {
		return contentCount;
	}
	public void setContentCount(int contentCount) {
		this.contentCount = contentCount;
	}
	
}
