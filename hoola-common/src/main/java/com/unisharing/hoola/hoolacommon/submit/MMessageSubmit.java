package com.unisharing.hoola.hoolacommon.submit;


import com.unisharing.hoola.hoolacommon.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class MMessageSubmit extends Pager implements Serializable {

	/**
	 * 定义私信查询，先无查询条件，只简单继承pager获取分页支持
	 */
	private static final long serialVersionUID = -7404068346785621280L;
	
	private String messageText;//按信息关键字
	
	private String createTimeQ;
	private String createTimeZ;
	
	private long createTimeQL;
	private long createTimeZL;
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getCreateTimeQ() {
		return createTimeQ;
	}
	public void setCreateTimeQ(String createTimeQ) {
		this.createTimeQ = createTimeQ;
	}
	public String getCreateTimeZ() {
		return createTimeZ;
	}
	public void setCreateTimeZ(String createTimeZ) {
		this.createTimeZ = createTimeZ;
	}
	public long getCreateTimeQL() {
		return createTimeQL;
	}
	public void setCreateTimeQL(long createTimeQL) {
		this.createTimeQL = createTimeQL;
	}
	public long getCreateTimeZL() {
		return createTimeZL;
	}
	public void setCreateTimeZL(long createTimeZL) {
		this.createTimeZL = createTimeZL;
	}
	
	public long toLong(String date) throws Exception{
		Date timeDate= DateUtil.parse(date, "yyyy-MM-dd HH:mm:ss");
		return timeDate.getTime();
	}

}
