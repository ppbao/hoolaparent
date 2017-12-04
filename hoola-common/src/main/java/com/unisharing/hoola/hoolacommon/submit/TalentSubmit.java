package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;
import java.util.List;

public class TalentSubmit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1860957583148675399L;
	
	private List<Integer> uidList;
	
	private int talentid;
	
	private String comment;

	public List<Integer> getUidList() {
		return uidList;
	}

	public void setUidList(List<Integer> uidList) {
		this.uidList = uidList;
	}

	public int getTalentid() {
		return talentid;
	}

	public void setTalentid(int talentid) {
		this.talentid = talentid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
