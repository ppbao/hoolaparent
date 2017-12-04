package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class FriendModel implements Serializable {
	
	private static final long serialVersionUID = -1327574319737286274L;
	
	private long id;
	private long uid;
	private long fid;
	private long createTime;
	private int isFriend;
	private int isTrue;
	
	public enum FriendType {
		SINA(1), //新浪
		TENCENT(2),//腾讯
		MOBILE(3),//手机通讯录
		DUANQU(0);//短趣
		int mark;
		private FriendType(int mark) {
			this.mark = mark;
		}
		public int getMark() {
			return this.mark;
		}
	}
	public long getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public long getFid() {
		return fid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public int getIsFriend() {
		return isFriend;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setFid(long fid) {
		this.fid = fid;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	@Override
	public String toString() {
		return "FriendModel [createTime=" + createTime + ", fid=" + fid
				+ ", id=" + id + ", isFriend=" + isFriend + ", uid=" + uid
				+ "]";
	}
	
	

}
