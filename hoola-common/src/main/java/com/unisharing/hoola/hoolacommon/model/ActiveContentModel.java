package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class ActiveContentModel implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 6579667118834833794L;
	
	private int activeId;//活动id
	private long cid;//内容id
	private int orderNum;//排序号
	public int getActiveId() {
		return activeId;
	}
	public void setActiveId(int activeId) {
		this.activeId = activeId;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	

}
