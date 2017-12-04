package com.unisharing.hoola.hoolacommon.model;


import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.SubjectForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class SubjectModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sid;
	private String title;
	private String videoUrl;
	private String imgUrl;
	private String thumbnailsUrl;
	private String description;
	private String explainUrl;
	private String innerParam;
	private long createTime;
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getThumbnailsUrl() {
		return thumbnailsUrl;
	}
	public void setThumbnailsUrl(String thumbnailsUrl) {
		this.thumbnailsUrl = thumbnailsUrl;
	}
	public String getExplainUrl() {
		return explainUrl;
	}
	public void setExplainUrl(String explainUrl) {
		this.explainUrl = explainUrl;
	}
	public String getInnerParam() {
		return innerParam;
	}
	public void setInnerParam(String innerParam) {
		this.innerParam = innerParam;
	}
	public SubjectForm asForm(){
		SubjectForm form = new SubjectForm();
		BeanUtils.copyProperties(this, form);
		if (StringUtils.isNotBlank(form.getImgUrl())){
			form.setImgUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getImgUrl());
		}else{
			form.setImgUrl("");
		}
		if(StringUtils.isNotBlank(form.getExplainUrl())){
			form.setExplainUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getExplainUrl());
		}else{
			form.setExplainUrl("");
		}
		if (StringUtils.isNotBlank(form.getThumbnailsUrl())){
			form.setThumbnailsUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getThumbnailsUrl());
		}else{
			form.setThumbnailsUrl("");
		}
		if(StringUtils.isNotBlank(form.getInnerParam())){
			form.setInnerParam(form.getInnerParam());
		}else{
			form.setInnerParam("");
		}
		
		if (StringUtils.isNotBlank(form.getVideoUrl())){
			form.setVideoUrl(HoolaConfig.getAliyunSystemImagesDomain()+form.getVideoUrl());
		}else{
			form.setVideoUrl("");
		}
		return form;
	}
}
