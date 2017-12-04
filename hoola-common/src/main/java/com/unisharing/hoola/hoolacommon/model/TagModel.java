package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class TagModel implements Serializable {

	private static final long serialVersionUID = -4872101481079096237L;
	
	private long tid;//标签ID
	private String tagText;//标签文本
	private int tagType;//标签类型
	public long getTid() {
		return tid;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	
	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	@Override
	public String toString() {
		return "TagModel [tagText=" + tagText + ", tid=" + tid + "]";
	}
	
	
}
