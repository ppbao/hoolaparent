package com.unisharing.hoola.hoolacommon.vo.unit;

import java.io.Serializable;

public class Content implements Serializable {
	
	private static final long serialVersionUID = -150396604744164388L;
	long cid;	//ID
	String title;	//标题
	String videoUrlHD;	//高清视频地址
	String videoUrl;//流畅版视频地址
	String thumbnailsUrl;//缩略图
	float playTime;//视频时长
	String description;//描述
	long createTime;//拍摄时间
	long uploadTime;//上传时间
	long uid;//作者ID
	int commentNum;//评论数
	int likeNum;//喜欢数
	int forwardNum;//转发数
	int showTimes;//播放次数
	float longitude;//经度
	float latitude;//维度
	String location;//地址
	float width;	//宽度
	float height;	//高度
	int cStatus;	//内容状态
	int isPrivate;//私有
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public float getPlayTime() {
		return playTime;
	}
	public void setPlayTime(float playTime) {
		this.playTime = playTime;
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
	public long getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getForwardNum() {
		return forwardNum;
	}
	public void setForwardNum(int forwardNum) {
		this.forwardNum = forwardNum;
	}
	public int getShowTimes() {
		return showTimes;
	}
	public void setShowTimes(int showTimes) {
		this.showTimes = showTimes;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public int getcStatus() {
		return cStatus;
	}
	public void setcStatus(int cStatus) {
		this.cStatus = cStatus;
	}
	public int getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}
	
}
