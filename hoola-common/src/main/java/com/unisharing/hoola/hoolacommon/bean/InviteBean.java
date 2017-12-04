package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

public class InviteBean implements Serializable{
	private static final long serialVersionUID = 2791810923355872094L;
	long uid;
	String name;
	int openType;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	
}
