package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

/**
 * 用户个人中心信息
 * @author wanghaihua
 *
 */
public class UserDetailForm implements Serializable{

	private static final long serialVersionUID = -1876867259827663175L;
	private long uid;//id
	private String avatar;//头像
	private String bgimage;//背景
	private String nickName;//昵称
	private String signature;//个性签名
	private int fansCount;//粉丝数
	private int followsCount;//关注数
	private int friendsCount;//好友数
	private int isFollow;//是否已经关注
	private int isFans;//是否是粉丝
	private int contentsCount;//内容数
	
	private int isBlack;//是否黑名单
	
	//认证信息
	private int isTalent;//是否是达人
	private String talentDesc;//达人说明
	private String videoUrl;//视频介绍
	private String videoFaceUrl;//视频封面
	
	
	public long getUid() {
		return uid;
	}
	public String getAvatar() {
		return avatar;
	}
	public String getBgimage() {
		return bgimage;
	}
	public String getNickName() {
		return nickName;
	}
	public String getSignature() {
		return signature;
	}
	public int getFansCount() {
		return fansCount;
	}
	public int getFollowsCount() {
		return followsCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public int getIsFollow() {
		return isFollow;
	}
	public int getIsFans() {
		return isFans;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public void setBgimage(String bgimage) {
		this.bgimage = bgimage;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public void setFollowsCount(int followsCount) {
		this.followsCount = followsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}
	public void setIsFans(int isFans) {
		this.isFans = isFans;
	}
	public int getContentsCount() {
		return contentsCount;
	}
	public void setContentsCount(int contentsCount) {
		this.contentsCount = contentsCount;
	}
	public int getIsBlack() {
		return isBlack;
	}
	public void setIsBlack(int isBlack) {
		this.isBlack = isBlack;
	}
	public int getIsTalent() {
		return isTalent;
	}
	public void setIsTalent(int isTalent) {
		this.isTalent = isTalent;
	}
	public String getTalentDesc() {
		return talentDesc;
	}
	public void setTalentDesc(String talentDesc) {
		this.talentDesc = talentDesc;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoFaceUrl() {
		return videoFaceUrl;
	}
	public void setVideoFaceUrl(String videoFaceUrl) {
		this.videoFaceUrl = videoFaceUrl;
	}
	
}
