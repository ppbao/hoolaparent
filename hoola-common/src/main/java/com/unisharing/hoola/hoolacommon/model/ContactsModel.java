package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class ContactsModel implements Serializable {
	
	private static final long serialVersionUID = -8272560053914580887L;
	
	String name;
	String mobile;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
}
