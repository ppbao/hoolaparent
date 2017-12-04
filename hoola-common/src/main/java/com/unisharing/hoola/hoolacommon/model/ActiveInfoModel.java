package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class ActiveInfoModel implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3623460123070657204L;
	
	private int id;//数据库自增id
	private String activeTitle;//活动标题
	private long beginTime;//开始时间
	private long endTime;//结束时间
	private long createTime;//创建时间
	private int flag;//是否置顶视频 0不置顶，1置顶
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActiveTitle() {
		return activeTitle;
	}
	public void setActiveTitle(String activeTitle) {
		this.activeTitle = activeTitle;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
