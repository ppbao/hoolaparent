package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class OtherTagModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455470453206606338L;
	
	private int id;//数据自增id
	private int tagType;//标签类型
	private String tagText;//标签文本
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	
	
	
	
	
	
	

}
