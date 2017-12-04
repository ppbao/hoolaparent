package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class LikeContentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1569844400307720460L;
	
	private int id;
	private long uid;
	private long cid;
	private long createTime;
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
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
	
	

}
