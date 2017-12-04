package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class FeedBackModel implements Serializable {
	
	private static final long serialVersionUID = 2881143807822019446L;
	
	private int id;
	private String feedbackText;//反馈内容
	private String screenshotUrl;//截屏
	private long createTime;//提交时间
	private long uid;//提交用户
	private int isCheck;//是否已经审核
	public int getId() {
		return id;
	}
	public String getFeedbackText() {
		return feedbackText;
	}
	public String getScreenshotUrl() {
		return screenshotUrl;
	}
	public long getCreateTime() {
		return createTime;
	}
	public long getUid() {
		return uid;
	}
	public int getIsCheck() {
		return isCheck;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}
	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	@Override
	public String toString() {
		return "FeedBackModel [createTime=" + createTime + ", feedbackText="
				+ feedbackText + ", id=" + id + ", isCheck=" + isCheck
				+ ", screenshotUrl=" + screenshotUrl + ", uid=" + uid + "]";
	}

}
