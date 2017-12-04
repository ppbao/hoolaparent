package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class SysResourceForm implements Serializable {
	
	private static final long serialVersionUID = -8605961695815638631L;
	
	private long id;
	private String description;//描述
	private String resourceUrl;//资源包路径
	private String resourceIconUrl;//展示图标路径
	private String resourceMusicUrl;//音乐试听资源路径
	private String resourceMd5;//资源包MD5值
	private int size;//资源包大小
	private long createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getResourceIconUrl() {
		return resourceIconUrl;
	}
	public void setResourceIconUrl(String resourceIconUrl) {
		this.resourceIconUrl = resourceIconUrl;
	}
	public String getResourceMusicUrl() {
		return resourceMusicUrl;
	}
	public void setResourceMusicUrl(String resourceMusicUrl) {
		this.resourceMusicUrl = resourceMusicUrl;
	}
	public String getResourceMd5() {
		return resourceMd5;
	}
	public void setResourceMd5(String resourceMd5) {
		this.resourceMd5 = resourceMd5;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
