package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class TalentListForm implements Serializable{
	
	private static final long serialVersionUID = 830673689116754828L;
	private TalentForm user;
	private int isFollow;
	public TalentForm getUser() {
		return user;
	}
	public void setUser(TalentForm user) {
		this.user = user;
	}
	public int getIsFollow() {
		return isFollow;
	}
	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}
	
}
