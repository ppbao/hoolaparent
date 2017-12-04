package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 登录成功返回数据
 * @author wanghaihua
 *
 */
public class LoginForm implements Serializable {
	
	UserForm user;
	List<BindForm> bindForms;
	private int isNew = 0;
	
	public UserForm getUser() {
		return user;
	}

	public List<BindForm> getBindForms() {
		return bindForms;
	}

	public void setUser(UserForm user) {
		this.user = user;
	}

	public void setBindForms(List<BindForm> bindForms) {
		this.bindForms = bindForms;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

}
