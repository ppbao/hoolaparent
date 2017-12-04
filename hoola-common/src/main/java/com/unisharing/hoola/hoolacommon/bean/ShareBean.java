package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

public class ShareBean implements Serializable{
	private static final long serialVersionUID = 6495895551460993311L;
	long uid;
	long cid;
	int openType;
	String memo;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
