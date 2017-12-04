package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class SetUserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8670513457586728729L;
	private long uid;
	private int orderNum;
	private long createTime;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
	

}
