package com.unisharing.hoola.hoolacommon.model;

public class RedisMessageModel {
	
	private long sendUid;//发送者
	private long revUid;//接收者
	private long msgId;//消息Id
	public long getSendUid() {
		return sendUid;
	}

	public long getRevUid() {
		return revUid;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setSendUid(long sendUid) {
		this.sendUid = sendUid;
	}

	public void setRevUid(long revUid) {
		this.revUid = revUid;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString(){
		return this.sendUid+":"+this.revUid+":"+this.msgId;
	}

	public static RedisMessageModel parse(String str){
		String[] params = str.split(":");
		if (params.length<3){
			return null;
		}else{
			try{
				RedisMessageModel message = new RedisMessageModel();
				message.setSendUid(Long.valueOf(params[0]));
				message.setRevUid(Integer.parseInt(params[1]));
				message.setMsgId(Long.valueOf(params[2]));
				return message;
			}catch (Exception e) {
				return null;
			}
		}
	}
}
