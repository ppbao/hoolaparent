package com.unisharing.hoola.hoolaclient.dao;


import com.unisharing.hoola.hoolacommon.model.MessageModel;
import org.springframework.stereotype.Component;

@Component
public interface ClientMessageMapper {
	void insertUserMessage(MessageModel messageModel);
	void deleteSendUserMessage(long sendUid);//发送人删除消息
	void deleteReceiveUserMessage(long recUid);//接受人删除消息

}
