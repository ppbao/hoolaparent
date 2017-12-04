package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class UserTokenModel implements Serializable {
	
	private static final long serialVersionUID = -5902346139442139991L;
	
	private long uid;//用户ID
	private String token;//
	private int expriseIn;//过期时间 单位：秒
	private long createTime;//创建时间
	public long getUid() {
		return uid;
	}
	public String getToken() {
		return token;
	}
	public int getExpriseIn() {
		return expriseIn;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setExpriseIn(int expriseIn) {
		this.expriseIn = expriseIn;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "UserTokenModel [createTime=" + createTime + ", expriseIn="
				+ expriseIn + ", token=" + token + ", uid=" + uid + "]";
	}
	
	
}
