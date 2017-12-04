package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;

public class AtMessageSubmit implements Serializable {
	
	private static final long serialVersionUID = 5110258765788896207L;
	
	private String duanquUser;	//通过 @ 分割
	private String sinaUser;	//通过@分割
	private String tencentUser; //通过@分割
	private long cid;			//内容ID
	public String getDuanquUser() {
		return duanquUser;
	}
	public String getSinaUser() {
		return sinaUser;
	}
	public String getTencentUser() {
		return tencentUser;
	}
	public long getCid() {
		return cid;
	}
	public void setDuanquUser(String duanquUser) {
		this.duanquUser = duanquUser;
	}
	public void setSinaUser(String sinaUser) {
		this.sinaUser = sinaUser;
	}
	public void setTencentUser(String tencentUser) {
		this.tencentUser = tencentUser;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
}
