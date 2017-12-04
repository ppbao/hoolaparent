package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

public class AtMessageBean implements Serializable{
	private static final long serialVersionUID = -6096810308888007149L;
	private String atDuanquUsers;
	private String atSinaUsers;
	private String atTencentUsers;
	private long cid;
	private long uid;
	public String getAtDuanquUsers() {
		return atDuanquUsers;
	}
	public void setAtDuanquUsers(String atDuanquUsers) {
		this.atDuanquUsers = atDuanquUsers;
	}
	public String getAtSinaUsers() {
		return atSinaUsers;
	}
	public void setAtSinaUsers(String atSinaUsers) {
		this.atSinaUsers = atSinaUsers;
	}
	public String getAtTencentUsers() {
		return atTencentUsers;
	}
	public void setAtTencentUsers(String atTencentUsers) {
		this.atTencentUsers = atTencentUsers;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
