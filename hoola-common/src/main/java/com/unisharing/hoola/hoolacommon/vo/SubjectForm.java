package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class SubjectForm implements Serializable {
	
	private static final long serialVersionUID = -3087586549552705740L;
	private int sid;
	private String title;
	private String videoUrl;
	private String imgUrl;
	private String thumbnailsUrl;
	private String description;
	private String explainUrl;
	private String innerParam;
	private long createTime;
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getThumbnailsUrl() {
		return thumbnailsUrl;
	}
	public void setThumbnailsUrl(String thumbnailsUrl) {
		this.thumbnailsUrl = thumbnailsUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getExplainUrl() {
		return explainUrl;
	}
	public void setExplainUrl(String explainUrl) {
		this.explainUrl = explainUrl;
	}
	public String getInnerParam() {
		return innerParam;
	}
	public void setInnerParam(String innerParam) {
		this.innerParam = innerParam;
	}
	
	
	
	

}
