package com.unisharing.hoola.hoolacommon.model;


import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.SysResourceForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class SysResourceModel implements Serializable {

	private static final long serialVersionUID = -4388811305636741915L;
	private long id;
	private String description;//描述
	private String resourceUrl;//资源包路径
	private String resourceIconUrl;//展示图标路径
	private String resourceMusicUrl;//音乐试听资源路径
	private String resourceMd5;//资源包MD5值
	private int size;//资源包大小
	private int type;//资源类型 1 背景音乐 2 表情 3 贴纸
	private long createTime;
	
	public static enum RES_TYPE{
		MUSIC(1),//音乐
		EXPRESSION(2),//表情
		PASTER(3);//贴纸
		
		int mark;
		private RES_TYPE(int mark){
			this.mark = mark;
		}
		public int getMark(){
			return mark;
		}
	}
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	
	public SysResourceForm asForm() {
		SysResourceForm form = new SysResourceForm();
		BeanUtils.copyProperties(this, form);
		if (StringUtils.isNotEmpty(form.getResourceIconUrl())){
			form.setResourceIconUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getResourceIconUrl());
		}else{
			form.setResourceIconUrl("");
		}
		if (StringUtils.isNotEmpty(form.getResourceMusicUrl())){
			form.setResourceMusicUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getResourceMusicUrl());
		}else{
			form.setResourceMusicUrl("");
		}
		if (StringUtils.isNotEmpty(form.getResourceUrl())){
			form.setResourceUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getResourceUrl());
		}else{
			form.setResourceUrl("");
		}
		return form;
	}
	
}
