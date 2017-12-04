package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class FriendForm implements Serializable {
	
	private static final long serialVersionUID = -8496675835371350575L;
	private int isFollow;	//当前用户是否关注该用户
	private int isNew = 0;//是否为新的
	
	private SimpleUserForm user;	//用户信息
	
	public int getIsFollow() {
		return isFollow;
	}
	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}
	
	
	public SimpleUserForm getUser() {
		return user;
	}
	public void setUser(SimpleUserForm user) {
		this.user = user;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	@Override
	public String toString() {
		return "FriendForm [ isFollow=" + isFollow + ", user=" + user + "]";
	}
}
