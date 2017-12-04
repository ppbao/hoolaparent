package com.unisharing.hoola.hoolacommon.bean;

public class FollowBean {
	
	long uid;
	long fid;
	long isTalent = 0;//是否推荐达人（公众帐号）
	long createtime;
	public long getUid() {
		return uid;
	}
	public long getFid() {
		return fid;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setFid(long fid) {
		this.fid = fid;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getIsTalent() {
		return isTalent;
	}
	public void setIsTalent(long isTalent) {
		this.isTalent = isTalent;
	}
	@Override
	public String toString() {
		return "FollowBean [uid=" + uid + ", fid=" + fid + ", createtime="
				+ createtime + "]";
	}
	

}
