package com.unisharing.hoola.hoolacommon.submit;


import com.unisharing.hoola.hoolacommon.model.BindModel;

import java.io.Serializable;

public class BindSubmit implements Serializable{
	
	private static final long serialVersionUID = -1290796746874676226L;
	private int openType;	//开放平台类型
	private String openUid;	//开放平台用户ID
	private String accessToken;	//访问Token
	private String refreshToken;	//刷新token
	private String expiresDate;//过期时间（到哪天过期）
	private String openNickName;// 第三方昵称
	public int getOpenType() {
		return openType;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getOpenUid() {
		return openUid;
	}
	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}
	public String getOpenNickName() {
		return openNickName;
	}
	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}
	public BindModel asBindModel(){
		BindModel model = new BindModel();
		model.setAccessToken(this.accessToken);
		model.setCreatetime(System.currentTimeMillis());
		model.setExpiresDate(expiresDate);
		model.setOpenType(this.openType);
		model.setOpenUid(openUid);
		model.setRefreshToken(refreshToken);
		model.setOpenNickName(this.openNickName);
		return model;
	}
	public String getExpiresDate() {
		return expiresDate;
	}
	public void setExpiresDate(String expiresDate) {
		this.expiresDate = expiresDate;
	}
	
	
}
