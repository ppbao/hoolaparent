package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

/**
 * 用户通讯录
 * @author wanghaihua
 *
 */
public class UserMobileModel implements Serializable{

	private static final long serialVersionUID = -4350368441912033333L;
	
	private long id;	//自增ID
	private long uid;	//用户ID
	private String nickName;	//通讯录名称
	private String mobile;	//手机号
	private long crateTime;	//创建时间
	public long getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public String getNickName() {
		return nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public long getCrateTime() {
		return crateTime;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setCrateTime(long crateTime) {
		this.crateTime = crateTime;
	}
	
}