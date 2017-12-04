package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

/**
 * @ 信息
 * @author wanghaihua
 *
 */
public class AtMessageModel implements Serializable {
	
	private static final long serialVersionUID = 8875567375465645542L;
	
	private long id;	//id 自增
	private long uid;	//发送@信息者ID
	private long atUid;	//接收者ID
	private long cid;	//@内容ID
	private long createTime;//@时间
	private String memo;//备注信息
	public long getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public long getAtUid() {
		return atUid;
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
	public void setId(long id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setAtUid(long atUid) {
		this.atUid = atUid;
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
	@Override
	public String toString() {
		return "AtMessageModel [atUid=" + atUid + ", cid=" + cid
				+ ", createTime=" + createTime + ", id=" + id + ", memo="
				+ memo + ", uid=" + uid + "]";
	}
	
	

}
