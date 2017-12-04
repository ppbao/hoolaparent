package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;

public class MPushHistorySubmit extends Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String isShow;//0 待显示 1 显示
	private int id;
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
	
	
	

}
