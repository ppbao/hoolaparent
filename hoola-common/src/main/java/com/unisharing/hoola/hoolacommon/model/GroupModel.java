package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7122722602545369789L;
	
	private int id;//db自增
	private long uid;//用户id
	private String groupName;//组名
	private long createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	

	

}
