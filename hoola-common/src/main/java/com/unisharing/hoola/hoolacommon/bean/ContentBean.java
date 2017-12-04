package com.unisharing.hoola.hoolacommon.bean;


import com.unisharing.hoola.hoolacommon.model.ContentModel;

import java.io.Serializable;

public class ContentBean implements Serializable {

	private static final long serialVersionUID = -7745348899881274647L;
	private long cid = 0;//id
	private long uid;	//用户Id
	private String description;// 描述
	private int shareSina; // 分享新浪
	private int shareTencent;// 分享腾讯
	private int shareTencentWeiBo;//分享腾讯微博
	private int isPrivate;// 0=公开？1=私密？
	private float longitude;// 经度
	private float latitude;// 纬度latilude
	private String createtime;// 拍摄时间
	private float playTime;// 视频时长
	private float width;// 宽度
	private float height;// 高度
	private String location;// 地理位置（商业场所）
	
	private String title;	//标题
	private String videoUrlHD;	//高清视频地址
	private String videoUrl;//流畅版视频地址
	private String thumbnailsUrl;	//缩略图地址
	private long createTime;//拍摄时间
	private long uploadTime;//上传时间
	private String atUserDuanqu; // @Duanqu 用户
	private String atUserSina; // @Sina 用户
	private String atUserTencent; // @腾讯用户
	private int activeId;//活动ID
	private String groupNames;//需要分享的组名字，用”,“分开
	//统计
	private String biaoqingNo;//表情编号
	private String tiezhiNo;//帖纸编号;
	private String musicNo;//音乐编号
	private String filterNo;//多个用，好隔开
	private String key;//字符串
	private String md5;//文件md5字符串
	private String originalDesc;//原始的描述
	public long getCid() {
		return cid;
	}
	public String getDescription() {
		return description;
	}
	public String getAtUserDuanqu() {
		return atUserDuanqu;
	}
	public String getAtUserSina() {
		return atUserSina;
	}
	public String getAtUserTencent() {
		return atUserTencent;
	}
	public int getShareSina() {
		return shareSina;
	}
	public int getShareTencent() {
		return shareTencent;
	}
	public int getIsPrivate() {
		return isPrivate;
	}
	public float getLongitude() {
		return longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public String getCreatetime() {
		return createtime;
	}
	public float getPlayTime() {
		return playTime;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public String getLocation() {
		return location;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAtUserDuanqu(String atUserDuanqu) {
		this.atUserDuanqu = atUserDuanqu;
	}
	public void setAtUserSina(String atUserSina) {
		this.atUserSina = atUserSina;
	}
	public void setAtUserTencent(String atUserTencent) {
		this.atUserTencent = atUserTencent;
	}
	public void setShareSina(int shareSina) {
		this.shareSina = shareSina;
	}
	public void setShareTencent(int shareTencent) {
		this.shareTencent = shareTencent;
	}
	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public void setPlayTime(float playTime) {
		this.playTime = playTime;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public long getUid() {
		return uid;
	}
	public String getTitle() {
		return title;
	}
	public String getVideoUrlHD() {
		return videoUrlHD;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public String getThumbnailsUrl() {
		return thumbnailsUrl;
	}
	public long getCreateTime() {
		return createTime;
	}
	public long getUploadTime() {
		return uploadTime;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setVideoUrlHD(String videoUrlHD) {
		this.videoUrlHD = videoUrlHD;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public void setThumbnailsUrl(String thumbnailsUrl) {
		this.thumbnailsUrl = thumbnailsUrl;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public int getActiveId() {
		return activeId;
	}
	public String getGroupNames() {
		return groupNames;
	}
	public void setActiveId(int activeId) {
		this.activeId = activeId;
	}
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}
	
	public String getBiaoqingNo() {
		return biaoqingNo;
	}
	public String getTiezhiNo() {
		return tiezhiNo;
	}
	public String getMusicNo() {
		return musicNo;
	}
	public String getFilterNo() {
		return filterNo;
	}
	public void setBiaoqingNo(String biaoqingNo) {
		this.biaoqingNo = biaoqingNo;
	}
	public void setTiezhiNo(String tiezhiNo) {
		this.tiezhiNo = tiezhiNo;
	}
	public void setMusicNo(String musicNo) {
		this.musicNo = musicNo;
	}
	public void setFilterNo(String filterNo) {
		this.filterNo = filterNo;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getOriginalDesc() {
		return originalDesc;
	}
	public void setOriginalDesc(String originalDesc) {
		this.originalDesc = originalDesc;
	}
	public ContentModel asContentModel(){
		ContentModel model = new ContentModel();
		model.setCid(cid);
		model.setUid(this.uid);
		model.setCommentNum(0);
		try {
			model.setCreateTime(Long.valueOf(this.createtime));
		} catch (Exception e) {
			model.setCreateTime(System.currentTimeMillis());
		}
		model.setcStatus(ContentModel.Status.NORMAL.getMark());
		try{
			model.setDescription(new String(this.description.getBytes("UTF-8")));
		}catch(Exception e){
			model.setDescription(this.description);
		}
		model.setOriginalDesc(model.getOriginalDesc());
		model.setForwardNum(0);
		model.setHeight(this.height);
		model.setIsPrivate(this.isPrivate);
		model.setLatilude(this.latitude);
		model.setLikeNum(0);
		model.setKey(this.key);
		
		try{
			model.setLocation(new String(this.location.getBytes("UTF-8")));
		}catch(Exception e){
			model.setLocation(this.location);
		}
		
		model.setLongitude(this.longitude);
		model.setPlayTime(this.playTime);
		model.setShowTimes(0);
		model.setTitle("");
		model.setUploadTime(System.currentTimeMillis());
		model.setWidth(this.width);
		model.setActiveId(activeId);
		model.setThumbnailsUrl(thumbnailsUrl);
		model.setVideoUrl(videoUrl);
		model.setVideoUrlHD(videoUrlHD);
		model.setKey(this.key);
		model.setMd5(this.md5);
		return model;
	}
	public int getShareTencentWeiBo() {
		return shareTencentWeiBo;
	}
	public void setShareTencentWeiBo(int shareTencentWeiBo) {
		this.shareTencentWeiBo = shareTencentWeiBo;
	}
}
