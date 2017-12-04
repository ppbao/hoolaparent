package com.unisharing.hoola.hoolacommon.model;

import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.ContentForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleContentForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 内容
 * 
 * @author wanghaihua
 * 
 */
public class ContentModel implements Serializable {

	private static final long serialVersionUID = 1755617967764414L;

	private long cid; // ID
	private long uid; // 用户Id
	private String title; // 标题
	private String description;// 描述，经过敏感词过滤的
	private String videoUrlHD; // 高清视频地址
	private String videoUrl;// 流畅版视频地址
	private String thumbnailsUrl; // 缩略图地址
	private long createTime;// 拍摄时间
	private long uploadTime;// 上传时间
	private float playTime;// 视频时长
	private int showTimes;// 播放次数
	private int sinaShareNum;//新浪分享次数
	private int friendsShareNum;//朋友圈分享次数
	private int commentNum;// 评论数;
	private int likeNum;// 喜欢次数
	private int forwardNum;// 转发数
	private float width; // 宽度
	private float height; // 高度
	private int cStatus; // 内容状态
	private int isPrivate;// 私有
	private int activeId;//活动ID
	private String gifUrl;//gif地址

	// 经纬度
	private float longitude;// 经度
	private float latilude;// 纬度

	private String location;// 地址
	
	//dm.yang
	private int falseCommentNum;//假评论数
	private int falseLikeNum;//假喜欢数
	private int falseShowTimes; //假播放次数
	private int falseForwardNum;//假转发数
	
	private int isShow;//投放标志
	private String key;//唯一标识字符串
	
	private String md5;//文件md5值
	
	private int recommend;

	private String originalDesc;//原始的描述
	private int top;//置顶
	public static enum Status {
		NORMAL(0),//正常
		AUTHOR_DELETE(1),	//作者删除
		EDITOR_DELETE(2), //编辑删除
		SHIELDED(3); //屏蔽
		int mark;
		private Status(int mark) {
			this.mark = mark;
		}
		public int getMark() {
			return this.mark;
		}
		
	}
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
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
	
	public int getActiveId() {
		return activeId;
	}

	public void setActiveId(int activeId) {
		this.activeId = activeId;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getFalseCommentNum() {
		return falseCommentNum;
	}

	public void setFalseCommentNum(int falseCommentNum) {
		this.falseCommentNum = falseCommentNum;
	}

	public int getFalseLikeNum() {
		return falseLikeNum;
	}

	public void setFalseLikeNum(int falseLikeNum) {
		this.falseLikeNum = falseLikeNum;
	}

	public int getFalseShowTimes() {
		return falseShowTimes;
	}

	public void setFalseShowTimes(int falseShowTimes) {
		this.falseShowTimes = falseShowTimes;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public int getFalseForwardNum() {
		return falseForwardNum;
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

	public void setFalseForwardNum(int falseForwardNum) {
		this.falseForwardNum = falseForwardNum;
	}
	public String getOriginalDesc() {
		return originalDesc;
	}
	public void setOriginalDesc(String originalDesc) {
		this.originalDesc = originalDesc;
	}

	public String getGifUrl() {
		return gifUrl;
	}

	public void setGifUrl(String gifUrl) {
		this.gifUrl = gifUrl;
	}

	public ContentForm asContentFormForClient(){
		ContentForm form = new ContentForm();
		SimpleUserForm user = new SimpleUserForm();
		user.setUid(this.uid);
		BeanUtils.copyProperties(this, form);
		form.setUser(user);
		String tThumbnailsUrl = form.getThumbnailsUrl();
		if (tThumbnailsUrl.indexOf("http://")<0){
			form.setThumbnailsUrl(HoolaConfig.getAliyunThumbnailDomain()
					+ form.getThumbnailsUrl());// 加上域名
		}
		String tVideoUrl = form.getVideoUrl();
		if (tVideoUrl.indexOf("http://") < 0) {
			if (tVideoUrl.equals(form.getVideoUrlHD())) {
				form.setVideoUrl(HoolaConfig.getAliyunHDVideoDomain()
						+ form.getVideoUrl());
			} else {
				form.setVideoUrl(HoolaConfig.getAliyunVideoDomain()
						+ form.getVideoUrl());
			}
			
		}
		String tVideoUrlHD = form.getVideoUrlHD();
		if (tVideoUrlHD.indexOf("http://") < 0) {
			form.setVideoUrlHD(HoolaConfig.getAliyunHDVideoDomain()
					+ form.getVideoUrlHD());
		}
		String gifUrl = form.getGifUrl();
		if (StringUtils.hasText(gifUrl) && !gifUrl.equalsIgnoreCase("null")){
			form.setGifUrl(HoolaConfig.getAliyunThumbnailDomain()
					+ form.getGifUrl());
		}
		
		form.setDescription(EmojiUtils.decodeEmoji(description));
		form.setKey(this.key);
		form.setLocation(this.location == null?"":this.location);
		form.setMd5(this.getMd5());
		if (form.getMd5()!= null && form.getMd5().equalsIgnoreCase("null")){
			form.setMd5(null);
		}
		form.setcStatus(this.cStatus);
		form.setRecommend(this.recommend);
		return form;
	}
	
	public SimpleContentForm asSimpleContentForm(){
		SimpleContentForm content = new SimpleContentForm();
		content.setCid(this.cid);
		content.setDescription(EmojiUtils.decodeEmoji(description));

		String tThumbnailsUrl = this.getThumbnailsUrl();
		if (tThumbnailsUrl.indexOf("http://") < 0) {
			content.setThumbnailsUrl(HoolaConfig.getAliyunThumbnailDomain()
					+ tThumbnailsUrl);// 加上域名
		} else {
			content.setThumbnailsUrl(tThumbnailsUrl);
		}
		String tVideoUrl = this.getVideoUrl();
		if (tVideoUrl != null) {
			if (tVideoUrl.indexOf("http://") < 0) {
				content.setVideoUrl(HoolaConfig.getAliyunVideoDomain()
						+ tVideoUrl);

			}
		} else {
			content.setVideoUrl(HoolaConfig.getAliyunHDVideoDomain()
					+ this.videoUrlHD);
		}

		String tVideoUrlHD = this.getVideoUrlHD();
		if (tVideoUrlHD.indexOf("http://") < 0) {
			content.setVideoUrlHD(HoolaConfig.getAliyunHDVideoDomain()
					+ tVideoUrlHD);
		}
		return content;
	}

	@Override
	public String toString() {
		return "ContentModel [cid=" + cid + ", uid=" + uid + ", title=" + title
				+ ", description=" + description + ", videoUrlHD=" + videoUrlHD
				+ ", videoUrl=" + videoUrl + ", thumbnailsUrl=" + thumbnailsUrl
				+ ", createTime=" + createTime + ", uploadTime=" + uploadTime
				+ ", playTime=" + playTime + ", showTimes=" + showTimes
				+ ", commentNum=" + commentNum + ", likeNum=" + likeNum
				+ ", forwardNum=" + forwardNum + ", width=" + width
				+ ", height=" + height + ", cStatus=" + cStatus
				+ ", isPrivate=" + isPrivate + ", activeId=" + activeId
				+ ", longitude=" + longitude + ", latilude=" + latilude
				+ ", location=" + location + ", falseCommentNum="
				+ falseCommentNum + ", falseLikeNum=" + falseLikeNum
				+ ", falseShowTimes=" + falseShowTimes + ", falseForwardNum="
				+ falseForwardNum + ", isShow=" + isShow + ", key=" + key + "]";
	}
}
