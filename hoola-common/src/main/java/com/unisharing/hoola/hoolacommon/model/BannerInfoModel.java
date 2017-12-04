package com.unisharing.hoola.hoolacommon.model;



import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.BannerForm;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class BannerInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2420204086025368203L;
	
	private int bid;
	private String title;
	private String description;//描述
	private String imgUrl;//图片地址
	private String bannerType;//类型
	private int isShow;
	private long createTime;//创建时间
	private int orderNum;//排序号
	private String innerUrl; //参数
	

	public static enum BannerType {

		INVITE("invite"), // 邀请好友
		TAG("tag"), // 标签
		HOT_TALENT("hot_talent"), // 热门达人
		HOT_CONTENT("hot_content"), // 热门内容
		USER("user"), // 用户个人中心
		CHANNEL("channel"),// 频道
		WAP("wap"),//wap页面
		SEARCH_KEY("search_key"),//搜索
		TOPIC_INDEX("topic_index"),//话题列表页面
		TOPIC_LIST("topic_list");//话题内容列表页

		String mark;

		private BannerType(String mark) {
			this.mark = mark;
		}

		public String getMark() {
			return this.mark;
		}
	}
	
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getInnerUrl() {
		return innerUrl;
	}
	public void setInnerUrl(String innerUrl) {
		this.innerUrl = innerUrl;
	}
	
	public String getBannerType() {
		return bannerType;
	}
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}
	public BannerForm asForm(){
		BannerForm form = new BannerForm();
		BeanUtils.copyProperties(this, form);
		form.setImgUrl(HoolaConfig.getAliyunSystemImagesDomain()+this.getImgUrl());
		form.setId(this.bid);
		return form;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
}
