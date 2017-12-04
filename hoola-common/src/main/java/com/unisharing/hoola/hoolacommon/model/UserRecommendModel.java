package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class UserRecommendModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 661209668165045602L;
	
	private long uid;
	private String reason;
	private int type;//1、编辑推荐好友 2、编辑推荐公共用户
	private long create_time;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
	
	

}
