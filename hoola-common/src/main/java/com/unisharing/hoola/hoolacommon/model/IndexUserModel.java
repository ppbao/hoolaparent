package com.unisharing.hoola.hoolacommon.model;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

public class IndexUserModel implements Serializable {
	private static final long serialVersionUID = -8940839764681174111L;
	@JestId
	long uid;
	String nickName;
	String signature;
	long time;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}