package com.unisharing.hoola.hoolacommon.model;

import io.searchbox.annotations.JestId;

public class IndexOpenUserModel {
	@JestId
	private String id;
	
	private String openUserId;	//用户
	private String openNickName;//用户昵称
	private long uid;//该第三方好友所属用户
	private int openType;//平台类型
	
	public String getOpenUserId() {
		return openUserId;
	}
	public void setOpenUserId(String openUserId) {
		this.openUserId = openUserId;
	}
	public String getOpenNickName() {
		return openNickName;
	}
	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "IndexOpenUserModel [id=" + id + ", openUserId=" + openUserId
				+ ", openNickName=" + openNickName + ", uid=" + uid
				+ ", openType=" + openType + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
