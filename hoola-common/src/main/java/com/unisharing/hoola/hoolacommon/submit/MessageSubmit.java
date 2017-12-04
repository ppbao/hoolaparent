package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;

/**
 * 私信发送提交实体
 * @author wanghaihua
 *
 */
public class MessageSubmit implements Serializable {

	private static final long serialVersionUID = 9146388512745776520L;
	
	private String messageText;//私信内容
	private long recUid;	//接收者ID
	public String getMessageText() {
		return messageText;
	}
	public long getRecUid() {
		return recUid;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setRecUid(long recUid) {
		this.recUid = recUid;
	}
	
	

}
