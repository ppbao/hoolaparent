package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;
import java.util.List;

public class MUserSubmit extends Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5494896373226537244L;
	
	private String nickname;//按用户名
	private String usertype;//按用户类型
	private String userbind;//按绑定账号
	private String zt;//按用户状态
	private List<Long> uidList;
	private String uid;
	private String pxtj;//排序条件
	private String pxlx;//排序类型：升序降序
	private String  isAuthentication;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getUserbind() {
		return userbind;
	}
	public void setUserbind(String userbind) {
		this.userbind = userbind;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public List<Long> getUidList() {
		return uidList;
	}
	public void setUidList(List<Long> uidList) {
		this.uidList = uidList;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPxtj() {
		return pxtj;
	}
	public void setPxtj(String pxtj) {
		this.pxtj = pxtj;
	}
	public String getPxlx() {
		return pxlx;
	}
	public void setPxlx(String pxlx) {
		this.pxlx = pxlx;
	}
	public String getIsAuthentication() {
		return isAuthentication;
	}
	public void setIsAuthentication(String isAuthentication) {
		this.isAuthentication = isAuthentication;
	}
	
	
}
