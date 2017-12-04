package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class RecommendModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612259459555936843L;
	
	private long cid;
	private int type;//0 首页推存 1 发现推存
	private int isShow;
	private long createTime;
	private int recommend;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	
	
	
	



}
