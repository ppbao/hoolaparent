package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class TagHotModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8243903762084394346L;
	private long tid;
	private String imageUrl;
	private int orderNum;
	private int tagType;
	private long createTime;
	private String tagText;
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
}
