package com.unisharing.hoola.hoolacommon.bean;

import java.io.Serializable;

public class DialogBean implements Serializable{
	
	private static final long serialVersionUID = -768623004239576067L;
	long uid;
	long dialogUid;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getDialogUid() {
		return dialogUid;
	}
	public void setDialogUid(long dialogUid) {
		this.dialogUid = dialogUid;
	}
	
	

}
