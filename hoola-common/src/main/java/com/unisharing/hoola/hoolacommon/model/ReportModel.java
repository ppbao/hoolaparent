package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

/**
 * 举报信息
 * @author wanghaihua
 *
 */
public class ReportModel implements Serializable{
	
	private static final long serialVersionUID = 3573187436805634667L;
	private int id;	//ID
	private long uid;	//举报用户
	private long cid;	//内容ID
	private long createTime;	//举报时间
	private String memo;	//备注信息
	private int isCheck;	//是否审核
	public int getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public long getCid() {
		return cid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public String getMemo() {
		return memo;
	}
	public int getIsCheck() {
		return isCheck;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	@Override
	public String toString() {
		return "ReportModel [cid=" + cid + ", createTime=" + createTime
				+ ", id=" + id + ", isCheck=" + isCheck + ", memo=" + memo
				+ ", uid=" + uid + "]";
	}
	

}
