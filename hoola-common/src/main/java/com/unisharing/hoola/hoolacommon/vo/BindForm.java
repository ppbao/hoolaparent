package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class BindForm implements Serializable{
	
	private static final long serialVersionUID = -7110124051927433206L;
	private int openType; // 开放平台类型
	private String openUid;// 开放平台用户ID
	private String accessToken; //访问Token
	private String expiresDate;//过期时间
	private String openNickName;//第三方平台名字
	public int getOpenType() {
		return openType;
	}
	public String getOpenUid() {
		return openUid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiresDate() {
		return expiresDate;
	}
	public void setExpiresDate(String expiresDate) {
		this.expiresDate = expiresDate;
	}
	public String getOpenNickName() {
		return openNickName;
	}
	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}
}
