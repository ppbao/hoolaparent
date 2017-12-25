package com.unisharing.hoola.hoolacommon.model;

import com.unisharing.hoola.hoolacommon.vo.BindForm;

import java.io.Serializable;

/**
 * 绑定信息
 * 
 * @author wanghaihua
 * 
 */
public class BindModel implements Serializable {

	private static final long serialVersionUID = -938539446787488888L;
	private String openNickName;//第三方平台昵称
	private long uid; // 用户ID
	private int openType; // 开放平台类型
	private String openUid;// 开放平台用户ID
	private String accessToken;
	private String refreshToken;
	private String expiresDate;
	private long createtime;

	public enum OpenType {
		WEIXIN(5),//微信
		FRIENDS(4),//朋友圈
		MOBILE(3),//手机
		SINA(1), //新浪
		TENCENT(2),//腾讯
		DUANQU(0),//Hoola
		TENCENTWEIBO(6);//腾讯微博
		int mark;
		private OpenType(int mark) {
			this.mark = mark;
		}
		public int getMark() {
			return this.mark;
		}
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public String getOpenUid() {
		return openUid;
	}

	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(String expiresDate) {
		this.expiresDate = expiresDate;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getOpenNickName() {
		return openNickName;
	}

	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}

	public BindForm asBindForm(){
		BindForm form = new BindForm();
		form.setAccessToken(this.accessToken);
		form.setOpenType(this.openType);
		form.setOpenUid(this.openUid);
		form.setExpiresDate(expiresDate);
		form.setOpenNickName(openNickName);
		return form;
	}

}
