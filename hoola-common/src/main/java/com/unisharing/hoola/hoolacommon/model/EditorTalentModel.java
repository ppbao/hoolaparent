package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class EditorTalentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2555242252101233407L;
	
	private int id;
	private long uid;
	private long createTime;
	private String comment;
	private int infoId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getInfoId() {
		return infoId;
	}
	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}
	
	
	
	

}
