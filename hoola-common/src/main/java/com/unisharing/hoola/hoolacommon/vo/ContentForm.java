package com.unisharing.hoola.hoolacommon.vo;

import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;

import java.io.Serializable;
import java.util.List;

public class ContentForm implements Serializable{
	
	private static final long serialVersionUID = 3023381366096139615L;
	private long cid;	//ID
	private String title;	//标题
	private String description;//描述
	private String videoUrlHD;	//高清视频地址
	private String videoUrl;//流畅版视频地址
	private String thumbnailsUrl;	//缩略图地址
	private long createTime;//拍摄时间
	private long uploadTime;//上传时间
	private float playTime;//视频时长
	private int showTimes;//播放次数
	private int commentNum;//评论数;
	private int likeNum;//喜欢次数
	private int forwardNum;//转发数
	private int sinaShareNum;//新浪分享次数
	private int friendsShareNum;//朋友圈分享次数
	private float width;	//宽度
	private float height;	//高度
	private int cStatus;	//内容状态
	private int isPrivate;//私有
	private String gifUrl;//gif地址
	
	//经纬度
	private float longitude;//经度
	private float latilude;//纬度
	
	private String location;//地址
	
	private String tags;//以|先分割 内容标签
	
	SimpleUserForm user;//发布者
	List<SimpleUserForm> optUsers;// 操作用户 喜欢，评论
	private boolean isLike;//是否已经喜欢过
	private boolean isForward;//是否已经转发过
	private boolean isLikeAndForward;//是否已经
	private String key;//唯一标识字符串
	private String shareUrl="";
	private String md5;//文件MD5值
	private int recommend;//推荐标识
	private List<SimpleUserForm> atUsers;//AtUsers
	
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
	public float getPlayTime() {
		return playTime;
	}
	public void setPlayTime(float playTime) {
		this.playTime = playTime;
	}
	public int getShowTimes() {
		return showTimes;
	}
	public void setShowTimes(int showTimes) {
		this.showTimes = showTimes;
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
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatilude() {
		return latilude;
	}
	public void setLatilude(float latilude) {
		this.latilude = latilude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public SimpleUserForm getUser() {
		return user;
	}
	public void setUser(SimpleUserForm user) {
		this.user = user;
	}
	public List<SimpleUserForm> getOptUsers() {
		return optUsers;
	}
	public void setOptUsers(List<SimpleUserForm> optUsers) {
		this.optUsers = optUsers;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	public boolean isForward() {
		return isForward;
	}
	public void setForward(boolean isForward) {
		this.isForward = isForward;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getShareUrl() {
		return HoolaConfig.getWapHost()+key+".htm";
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public int getSinaShareNum() {
		return sinaShareNum;
	}
	public void setSinaShareNum(int sinaShareNum) {
		this.sinaShareNum = sinaShareNum;
	}
	public int getFriendsShareNum() {
		return friendsShareNum;
	}
	public void setFriendsShareNum(int friendsShareNum) {
		this.friendsShareNum = friendsShareNum;
	}
	public List<SimpleUserForm> getAtUsers() {
		return atUsers;
	}
	public void setAtUsers(List<SimpleUserForm> atUsers) {
		this.atUsers = atUsers;
	}
	
	public boolean isLikeAndForward() {
		return isLikeAndForward;
	}
	
	public void setLikeAndForward(boolean isLikeAndForward) {
		this.isLikeAndForward = isLikeAndForward;
	}
	public String getGifUrl() {
		return gifUrl;
	}
	public void setGifUrl(String gifUrl) {
		this.gifUrl = gifUrl;
	}
	@Override
	public String toString() {
		return "ContentForm [cStatus=" + cStatus + ", cid=" + cid
				+ ", commentNum=" + commentNum + ", createTime=" + createTime
				+ ", description=" + description + ", forwardNum=" + forwardNum
				+ ", friendsShareNum=" + friendsShareNum + ", height=" + height
				+ ", isForward=" + isForward + ", isLike=" + isLike
				+ ", isPrivate=" + isPrivate + ", key=" + key + ", latilude="
				+ latilude + ", likeNum=" + likeNum + ", location=" + location
				+ ", longitude=" + longitude + ", md5=" + md5 + ", optUsers="
				+ optUsers + ", playTime=" + playTime + ", recommend="
				+ recommend + ", shareUrl=" + shareUrl + ", showTimes="
				+ showTimes + ", sinaShareNum=" + sinaShareNum + ", tags="
				+ tags + ", thumbnailsUrl=" + thumbnailsUrl + ", title="
				+ title + ", uploadTime=" + uploadTime + ", user=" + user
				+ ", videoUrl=" + videoUrl + ", videoUrlHD=" + videoUrlHD
				+ ", width=" + width + "]";
	}
}
