package com.unisharing.hoola.hoolacommon.model;

import com.unisharing.hoola.hoolacommon.vo.EditorTagForm;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class EditorTagModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1980025905208561684L;
	
	private int id;//数据库自增id
	private String tagText;//标签文本
	private int tagType;
	private String tagImage;//标签图片
	private String tagImageMore;
	private int is_show;
	
	private int orderNum;//排序号
	private long createTime;//推存时间
	private float width;//宽
	private float height;//高
	
	private String title;//显示标题
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	public String getTagImage() {
		return tagImage;
	}
	public void setTagImage(String tagImage) {
		this.tagImage = tagImage;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
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
	
	
	
	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	public String getTagImageMore() {
		return tagImageMore;
	}
	public void setTagImageMore(String tagImageMore) {
		this.tagImageMore = tagImageMore;
	}
	public int getIs_show() {
		return is_show;
	}
	public void setIs_show(int is_show) {
		this.is_show = is_show;
	}
	private EditorTagForm asForm(){
		EditorTagForm form = new EditorTagForm();
		BeanUtils.copyProperties(this, form);
		return form;
	}
}
