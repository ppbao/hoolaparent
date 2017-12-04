package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class BannerForm implements Serializable {
	private static final long serialVersionUID = -3020976118411957646L;
	private int id;
	private String title;
	private String description;
	private String imgUrl;
	private String bannerType;
	private String innerUrl;//访问参数
	
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getImgUrl() {
		return imgUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getBannerType() {
		return bannerType;
	}
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}
	public String getInnerUrl() {
		return innerUrl;
	}
	public void setInnerUrl(String innerUrl) {
		this.innerUrl = innerUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
