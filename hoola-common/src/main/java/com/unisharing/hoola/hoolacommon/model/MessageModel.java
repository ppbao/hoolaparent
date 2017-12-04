package com.unisharing.hoola.hoolacommon.model;


import com.unisharing.hoola.hoolacommon.vo.MessageForm;

import java.io.Serializable;

/**
 * 私信
 * @author wanghaihua
 *
 */
public class MessageModel implements Serializable{
	
	private static final long serialVersionUID = -4832398593942299208L;
	
	private long id;	//私信ID 自赠
	private long uid;	//发送私信用户ID
	private long recUid;//接收私信用户ID
	private String messageText; //私信内容
	private long createTime;//创建时间
	private int isDelete;//是否删除
	private int isNew;//是否新消息
	private String type;//
	public long getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public long getRecUid() {
		return recUid;
	}
	public String getMessageText() {
		return messageText;
	}
	public long getCreateTime() {
		return createTime;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setRecUid(long recUid) {
		this.recUid = recUid;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	
	public MessageForm asForm(){
		MessageForm form = new MessageForm();
		form.setCreateTime(this.getCreateTime());
		form.setId(id);
		form.setMessageText(messageText);
		return form;
	}
	 
	public RedisMessageModel asRedisForm(){
		RedisMessageModel redisModel = new RedisMessageModel();
		redisModel.setMsgId(this.id);
		redisModel.setRevUid(this.getRecUid());
		redisModel.setSendUid(this.getUid());
		return redisModel;
	}
	@Override
	public String toString() {
		return "MessageModel [createTime=" + createTime + ", id=" + id
				+ ", isDelete=" + isDelete + ", isNew=" + isNew
				+ ", messageText=" + messageText + ", recUid=" + recUid
				+ ", uid=" + uid + "]";
	}
	
	public String toRedisString(){
		return this.uid+":"+this.recUid+":"+this.id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
