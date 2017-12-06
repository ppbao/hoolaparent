package com.unisharing.hoola.hoolacommon.vo;

import com.unisharing.hoola.hoolacommon.model.TagModel;
import com.unisharing.hoola.hoolacommon.utils.DateUtil;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ManagerContentForm implements Serializable {

	/**
	 * 内容展示列表 dm.yang
	 */
	private static final long serialVersionUID = 8104363997597283299L;
	
	private long uid;
	private long cid;
	private String thumbnailsUrl;
	private String videoUrl;
	private String videoUrlHd;
	private long uploadTime;
	private String uploadTimeStr;
	private String nickName;
	private String description;
	private List<TagModel> list;
	private int commentNum;
	private int likeNum;
	private int showTimes;
	private int forwardNum;
	private int falseForwardNum;
	private int falseCommentNum;
	private int falseLikeNum;
	private int falseShowTimes;
	private int cStatus;
	private int xpls;//新评论数
	private int isPrivate;//内容公开性
	
	//private int bqlx;//该字段用于区分内容列表中标签行是添加标签还是修改标签
	
	//private long bqid;//如果是修改标签获取该修改标签的id
	private int sinashareNum;//新浪分享次数真
	private int sinashareFalseNum;
	private int quanshareNum;
	private int quanshareFalseNum;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getThumbnailsUrl() {
		return thumbnailsUrl;
	}
	public void setThumbnailsUrl(String thumbnailsUrl) {
		this.thumbnailsUrl = thumbnailsUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoUrlHd() {
		return videoUrlHd;
	}
	public void setVideoUrlHd(String videoUrlHd) {
		this.videoUrlHd = videoUrlHd;
	}
	public long getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getUploadTimeStr() {
		return uploadTimeStr;
	}
	public void setUploadTimeStr(String uploadTimeStr) {
		this.uploadTimeStr = uploadTimeStr;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<TagModel> getList() {
		return list;
	}
	public void setList(List<TagModel> list) {
		this.list = list;
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
	public int getShowTimes() {
		return showTimes;
	}
	public void setShowTimes(int showTimes) {
		this.showTimes = showTimes;
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
	
	public int getcStatus() {
		return cStatus;
	}
	public void setcStatus(int cStatus) {
		this.cStatus = cStatus;
	}
	public String toDate(long uploadTime){
		return DateUtil.format(new Date(uploadTime),"yyyy-MM-dd HH:mm:ss");
	}
	public int getForwardNum() {
		return forwardNum;
	}
	public void setForwardNum(int forwardNum) {
		this.forwardNum = forwardNum;
	}
	public int getFalseForwardNum() {
		return falseForwardNum;
	}
	public void setFalseForwardNum(int falseForwardNum) {
		this.falseForwardNum = falseForwardNum;
	}
	public int getXpls() {
		return xpls;
	}
	public void setXpls(int xpls) {
		this.xpls = xpls;
	}
	public int getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}
	public int getSinashareNum() {
		return sinashareNum;
	}
	public void setSinashareNum(int sinashareNum) {
		this.sinashareNum = sinashareNum;
	}
	public int getSinashareFalseNum() {
		return sinashareFalseNum;
	}
	public void setSinashareFalseNum(int sinashareFalseNum) {
		this.sinashareFalseNum = sinashareFalseNum;
	}
	public int getQuanshareNum() {
		return quanshareNum;
	}
	public void setQuanshareNum(int quanshareNum) {
		this.quanshareNum = quanshareNum;
	}
	public int getQuanshareFalseNum() {
		return quanshareFalseNum;
	}
	public void setQuanshareFalseNum(int quanshareFalseNum) {
		this.quanshareFalseNum = quanshareFalseNum;
	}
	
}
