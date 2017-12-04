package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class ContentTagModel implements Serializable {
	
	private static final long serialVersionUID = 1253581778444717689L;
	
	private long id;	//自增加ID
	private long cid;	//内容ID
	private long tid;	//标签ID
	public long getId() {
		return id;
	}
	public long getCid() {
		return cid;
	}
	public long getTid() {
		return tid;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	@Override
	public String toString() {
		return "ContentTagModel [cid=" + cid + ", id=" + id + ", tid=" + tid
				+ "]";
	}

}
