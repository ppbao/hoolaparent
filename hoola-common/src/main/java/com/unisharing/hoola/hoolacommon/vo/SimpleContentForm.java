package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class SimpleContentForm implements Serializable {
	private static final long serialVersionUID = -8024465023444736874L;
	private long cid;	//ID
	private String description;//描述
	private String videoUrlHD;	//高清视频地址
	private String videoUrl;//流畅版视频地址
	private String thumbnailsUrl;	//缩略图地址
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVideoUrlHD() {
		return videoUrlHD;
	}
	public void setVideoUrlHD(String videoUrlHD) {
		this.videoUrlHD = videoUrlHD;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getThumbnailsUrl() {
		return thumbnailsUrl;
	}
	public void setThumbnailsUrl(String thumbnailsUrl) {
		this.thumbnailsUrl = thumbnailsUrl;
	}
	@Override
	public String toString() {
		return "SimpleContentForm [cid=" + cid + ", description=" + description
				+ ", videoUrlHD=" + videoUrlHD + ", videoUrl=" + videoUrl
				+ ", thumbnailsUrl=" + thumbnailsUrl + "]";
	}
	
}
