package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class SetContentModel implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6406338343001861386L;
	private long cid;
	private long uid;
	private int orderNum;
	private long createTime;
	private int type;//1、老版本 2、新版本
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
