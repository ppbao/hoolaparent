package com.unisharing.hoola.hoolacommon.vo;


import com.unisharing.hoola.hoolacommon.model.UserModel;

import java.io.Serializable;

public class UserForm implements Serializable{
	
	private static final long serialVersionUID = -8229711522654999723L;
	
	private long uid;//id
	private String nickName;//昵称
	private String avatar;//头像
	private String token;//本地token
	private String backgroundUrl;//背景图片
	private int status;//用户状态
	
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserForm() {
		super();
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserForm(UserModel model) {
		this.uid = model.getUid();
		this.nickName = model.getNickName();
		this.avatar = model.getAvatarUrl();
		this.token = model.getToken();
		this.backgroundUrl = model.getBackgroundUrl();
	}
	@Override
	public String toString() {
		return "UserForm [avatar=" + avatar + ", nickName=" + nickName
				+ ", token=" + token + ", uid=" + uid + "]";
	}
	
}
