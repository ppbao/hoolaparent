package com.unisharing.hoola.hoolaclient.service.message;


import com.unisharing.hoola.hoolacommon.model.MessageModel;

public interface IClientMessageService {
	
	void insertUserMessage(MessageModel messageModel);
	void deleteUserMessage(long uid, int type);//uid 发送者id或者接受者id,type 类型0 发送者 1接受者
	

}
