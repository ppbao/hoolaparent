package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

/**
 * 手机通讯录上传消息传递类
 * @author tiger
 *
 */
public class MobileMessageBean implements Serializable{
	
	private static final long serialVersionUID = -1700017125937351479L;
	String mobileStr;
	long uid;
	public String getMobileStr() {
		return mobileStr;
	}
	public void setMobileStr(String mobileStr) {
		this.mobileStr = mobileStr;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
