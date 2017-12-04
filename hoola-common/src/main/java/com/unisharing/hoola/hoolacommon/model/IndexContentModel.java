package com.unisharing.hoola.hoolacommon.model;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

public class IndexContentModel implements Serializable {

	private static final long serialVersionUID = -7292342006259156384L;
	@JestId
	long cid;
	String tags;
	String description;
	long time;
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	

}
