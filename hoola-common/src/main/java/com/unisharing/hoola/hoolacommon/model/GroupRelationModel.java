package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class GroupRelationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817868291198664006L;
	
	private long id;
	private int gid;//组id
	private long relId;//关系id
	private long fuid;//关注用户id
	private long createTime;//关注时间
	private int isFriend;//是否相互关注
	private int isTrue;//真假关系
	private long addTime;//分组时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public long getRelId() {
		return relId;
	}
	public void setRelId(long relId) {
		this.relId = relId;
	}
	public long getFuid() {
		return fuid;
	}
	public void setFuid(long fuid) {
		this.fuid = fuid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}
	public int getIsTrue() {
		return isTrue;
	}
	public void setIsTrue(int isTrue) {
		this.isTrue = isTrue;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	
	
	
	

}
