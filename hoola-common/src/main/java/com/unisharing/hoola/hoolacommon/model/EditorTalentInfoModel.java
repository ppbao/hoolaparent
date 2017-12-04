package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class EditorTalentInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7289520614097966300L;
	
	private int id;
	private String title;
	private String imgUrl;
	private int status;
	private long createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
	

}
