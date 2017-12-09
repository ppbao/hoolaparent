package com.unisharing.hoola.hooladatacenter.service.message;

public interface IMessageService {
	
	/**
	 * 处理发送私信
	 */
	public void handleSendMessage();
	
	/**
	 * 删除单挑私信
	 */
	public void handleDeleteMessage(long msgId);
	
	/**
	 * 删除整个对话
	 */
	public void handleDeleteDialog();

}
