package com.unisharing.hoola.hoolacommon.submit;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class MTagSubmit extends Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8393847086380206525L;
	
	private String tagText;
	
	private long tid;
	private MultipartFile imageUrl;
	private int type;//推送类型,1不带图片，2带图片
	private List<Long> numList;
	private List<Long> tidList;

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public MultipartFile getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(MultipartFile imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public List<Long> getNumList() {
		return numList;
	}

	public void setNumList(List<Long> numList) {
		this.numList = numList;
	}

	public List<Long> getTidList() {
		return tidList;
	}

	public void setTidList(List<Long> tidList) {
		this.tidList = tidList;
	}
	
	

}
