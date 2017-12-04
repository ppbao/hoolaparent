package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class UserAdminModel extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5870473526567406404L;
	
	private int userid;
	private String username;
	private String password;
	private String fullname;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
