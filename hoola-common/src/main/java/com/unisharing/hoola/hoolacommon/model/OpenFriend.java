package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

/**
 * 第三方开放平台好友关系
 * @author wanghaihua
 *
 */
public class OpenFriend implements Serializable {

	private static final long serialVersionUID = -121490080967211585L;
	private long uid;//用户ID
	private long id;	//ID
	private int openType;	//第三方平台类型	
	private String openUserId;	//第三方平台用户ID
	private String openNickName;	//第三方平台的用户昵称
	private String openUserName;	//第三方平台用户名：腾讯At用户和昵称是不一样的
	private String avatarUrl;//头像地址
	private String matchedUid;//匹配成功的用户ID
	private int isMatched;//匹配成功 1：老数据，2：新数据
	
	
	public OpenFriend(int openType) {
		super();
		this.openType = openType;
	}
	
	public OpenFriend() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public int getOpenType() {
		return openType;
	}
	public String getOpenUserId() {
		return openUserId;
	}
	public String getOpenNickName() {
		return openNickName;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public void setOpenUserId(String openUserId) {
		this.openUserId = openUserId;
	}
	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}
	
	
	@Override
	public String toString() {
		return "OpenFriend [uid=" + uid + ", id=" + id + ", openType="
				+ openType + ", openUserId=" + openUserId + ", openNickName="
				+ openNickName + ", openUserName=" + openUserName
				+ ", avatarUrl=" + avatarUrl + ", matchedUid=" + matchedUid
				+ ", isMatched=" + isMatched + "]";
	}

	public String getOpenUserName() {
		return openUserName;
	}
	public void setOpenUserName(String openUserName) {
		this.openUserName = openUserName;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}


	public String getMatchedUid() {
		return matchedUid;
	}

	public void setMatchedUid(String matchedUid) {
		this.matchedUid = matchedUid;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getIsMatched() {
		return isMatched;
	}

	public void setIsMatched(int isMatched) {
		this.isMatched = isMatched;
	}
	
}
