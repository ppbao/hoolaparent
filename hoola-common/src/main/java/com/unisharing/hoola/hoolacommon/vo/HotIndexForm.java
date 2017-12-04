package com.unisharing.hoola.hoolacommon.vo;

import java.util.List;

public class HotIndexForm {
	//运营条
	List<BannerForm> banners;
	//编辑推荐标签
	List<EditorTagForm> hotTags;
	//系统推荐标签
	List<String> tags;
	public List<BannerForm> getBanners() {
		return banners;
	}
	public List<EditorTagForm> getHotTags() {
		return hotTags;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setBanners(List<BannerForm> banners) {
		this.banners = banners;
	}
	public void setHotTags(List<EditorTagForm> hotTags) {
		this.hotTags = hotTags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
