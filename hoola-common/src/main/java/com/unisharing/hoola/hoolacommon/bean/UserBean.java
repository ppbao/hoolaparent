package com.unisharing.hoola.hoolacommon.bean;


import com.unisharing.hoola.hoolacommon.model.BindModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;

import java.io.Serializable;

public class UserBean implements Serializable {
	private static final long serialVersionUID = -4914974638246814494L;
	UserModel user;
	BindModel bind;

	public UserModel getUser() {
		return user;
	}

	public BindModel getBind() {
		return bind;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public void setBind(BindModel bind) {
		this.bind = bind;
	}

}
