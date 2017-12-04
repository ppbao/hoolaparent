package com.unisharing.hoola.hoolacommon.vo;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class SimpleUserForm implements Serializable {
	private static final long serialVersionUID = 8963873689066585494L;
	private long uid;//id
	private String nickName;//昵称
	private String avatar;//头像
	private int actionType;//动作类型
	private String signature;//签名
	private int isTalent;//是否认证达人
	public long getUid() {
		return uid;
	}
	public String getNickName() {
		return nickName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public String getSignature() {
		if (StringUtils.isBlank(this.signature)){
			return "";
		}else{
			if (this.signature.equalsIgnoreCase("null")){
				return "";
			}
		}
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "SimpleUserForm [actionType=" + actionType + ", avatar="
				+ avatar + ", nickName=" + nickName + ", uid=" + uid + "]";
	}
	public int getIsTalent() {
		return isTalent;
	}
	public void setIsTalent(int isTalent) {
		this.isTalent = isTalent;
	}
	
}
