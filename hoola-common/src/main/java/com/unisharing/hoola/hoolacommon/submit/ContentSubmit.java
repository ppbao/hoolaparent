package com.unisharing.hoola.hoolacommon.submit;

import com.unisharing.hoola.hoolacommon.bean.ContentBean;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class ContentSubmit implements Serializable {

	private static final long serialVersionUID = 2957693002983728317L;
	
	private long cid = 0;//id
	private String description;// 描述
	private MultipartFile video;// 文件地址
	private MultipartFile thumbnails;// 缩略图
	private String tags;// 标签串，以“;”隔开
	private String atUserDuanqu; // @Duanqu 用户
	private String atUserSina; // @Sina 用户
	private String atUserTencent; // @腾讯用户
	private int shareSina = 0; // 分享新浪
	private int shareTencent = 0;// 分享腾讯
	private int shareTencentWeiBo = 0;//分享腾讯微博
	private int isPrivate = 0;// 0=公开？1=私密？
	private float longitude = 0;// 经度
	private float latitude = 0;// 纬度
	private String createtime;// 拍摄时间
	private float playTime = 0;// 视频时长
	private float width = 0;// 宽度
	private float height = 0;// 高度
	private String location;// 地理位置（商业场所）
	private String groupNames;//需要分享的组，以逗号隔开
	private int activeId = 0;//活动ID
	private String biaoqingNo;//表情编号
	private String tiezhiNo;//帖子编号;
	private String musicNo;//背景音乐编号
	private String filterNo;//滤镜编号，多个则使用“，”号分开
	private String key;//唯一标识字符串

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getVideo() {
		return video;
	}

	public void setVideo(MultipartFile video) {
		this.video = video;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAtUserDuanqu() {
		return atUserDuanqu;
	}

	public void setAtUserDuanqu(String atUserDuanqu) {
		this.atUserDuanqu = atUserDuanqu;
	}

	public String getAtUserSina() {
		return atUserSina;
	}

	public void setAtUserSina(String atUserSina) {
		this.atUserSina = atUserSina;
	}

	public String getAtUserTencent() {
		return atUserTencent;
	}

	public void setAtUserTencent(String atUserTencent) {
		this.atUserTencent = atUserTencent;
	}

	public int getShareSina() {
		return shareSina;
	}

	public void setShareSina(int shareSina) {
		this.shareSina = shareSina;
	}

	public int getShareTencent() {
		return shareTencent;
	}

	public void setShareTencent(int shareTencent) {
		this.shareTencent = shareTencent;
	}

	public int getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public float getPlayTime() {
		return playTime;
	}

	public void setPlayTime(float playTime) {
		this.playTime = playTime;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public MultipartFile getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(MultipartFile thumbnails) {
		this.thumbnails = thumbnails;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getGroupNames() {
		return groupNames;
	}

	public int getActiveId() {
		return activeId;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public void setActiveId(int activeId) {
		this.activeId = activeId;
	}
	
	public void setActiveId(Integer activeId) {
		this.activeId = activeId;
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

	public ContentModel asContentModel() {
		ContentModel model = new ContentModel();
		model.setCommentNum(0);
		try {
			model.setCreateTime(Long.valueOf(this.createtime));
		} catch (Exception e) {
			model.setCreateTime(System.currentTimeMillis());
		}
		model.setcStatus(ContentModel.Status.NORMAL.getMark());
		
		model.setForwardNum(0);
		model.setHeight(this.height);
		model.setIsPrivate(this.isPrivate);
		model.setLatilude(this.latitude);
		model.setLikeNum(0);
		
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
		model.setKey(this.key);
		
		return model;
	}
	
	
	public ContentBean asContentBean() {
		ContentBean bean = new ContentBean();
		bean.setAtUserDuanqu(atUserDuanqu);
		bean.setAtUserSina(atUserSina);
		bean.setAtUserTencent(atUserTencent);
		try {
			bean.setCreateTime(Long.valueOf(this.createtime));
		} catch (Exception e) {
			bean.setCreateTime(System.currentTimeMillis());
		}
		try{
			bean.setDescription(new String(this.description.getBytes("UTF-8")));
		}catch(Exception e){
			bean.setDescription(this.description);
		}
		bean.setHeight(this.height);
		bean.setIsPrivate(this.isPrivate);
		bean.setLatitude(this.latitude);
		
		try{
			bean.setLocation(new String(this.location.getBytes("UTF-8")));
		}catch(Exception e){
			bean.setLocation(this.location);
		}
		bean.setLongitude(this.longitude);
		bean.setPlayTime(this.playTime);
		bean.setTitle("");
		bean.setUploadTime(System.currentTimeMillis());
		bean.setWidth(this.width);
		bean.setShareSina(this.shareSina);
		bean.setShareTencent(this.shareTencent);
		bean.setGroupNames(this.groupNames);
		bean.setKey(this.key);
		bean.setMusicNo(this.musicNo);
		bean.setTiezhiNo(this.tiezhiNo);
		bean.setBiaoqingNo(this.biaoqingNo);
		bean.setFilterNo(this.filterNo);
		bean.setActiveId(this.activeId);
		bean.setShareTencentWeiBo(this.getShareTencentWeiBo());
		return bean;
	}

	public int getShareTencentWeiBo() {
		return shareTencentWeiBo;
	}

	public void setShareTencentWeiBo(int shareTencentWeiBo) {
		this.shareTencentWeiBo = shareTencentWeiBo;
	}

	
}
