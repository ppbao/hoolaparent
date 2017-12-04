package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

public class GroupBean implements Serializable {
	
	private static final long serialVersionUID = -2743455602757691178L;
	long uid;
	String groupName;
	String oldGroupName;
	String uids;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getOldGroupName() {
		return oldGroupName;
	}
	public void setOldGroupName(String oldGroupName) {
		this.oldGroupName = oldGroupName;
	}
	public String getUids() {
		return uids;
	}
	public void setUids(String uids) {
		this.uids = uids;
	}
}
