package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class MatchedUserModel implements Serializable {
	
	private static final long serialVersionUID = 6053579922570955676L;
	long uid;//在短趣的用户ID
	String userName;//第三方平台用户名
	//long matchedTime;//匹配时间
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "MatchedUserModel [uid=" + uid + ", userName=" + userName + "]";
	}

}
