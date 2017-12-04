package com.unisharing.hoola.hoolacommon.vo.unit;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = -8687882822084999067L;
	private long uid;	//用户ID
	private String nickName;	//昵称
	private String avatarUrl;	//头像
	private float longitude;
	private float latitude;
	private int signature;	//签名
	private String backgroundUrl;//背景图片
	private int roleId;//角色ID
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
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
	public int getSignature() {
		return signature;
	}
	public void setSignature(int signature) {
		this.signature = signature;
	}
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	@Override
	public String toString() {
		return "User [avatarUrl=" + avatarUrl + ", backgroundUrl="
				+ backgroundUrl + ", latitude=" + latitude + ", longitude="
				+ longitude + ", nickName=" + nickName + ", roleId=" + roleId
				+ ", signature=" + signature + ", uid=" + uid + "]";
	}
	
}
