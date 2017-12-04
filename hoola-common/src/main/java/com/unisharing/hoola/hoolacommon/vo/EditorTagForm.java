package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class EditorTagForm implements Serializable{
	
	private static final long serialVersionUID = -7815797196911748856L;
	private int id;//数据库自增id
	private String tagText;//标签文本
	private String tagImage;//标签图片
	private int orderNum;//排序号
	private float width;//宽
	private float height;//高
	private String title;//显示标题
	public int getId() {
		return id;
	}
	public String getTagText() {
		return tagText;
	}
	public String getTagImage() {
		return tagImage;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	public void setTagImage(String tagImage) {
		this.tagImage = tagImage;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
