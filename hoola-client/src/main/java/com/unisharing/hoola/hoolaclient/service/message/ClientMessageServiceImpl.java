package com.unisharing.hoola.hoolaclient.service.message;


import com.unisharing.hoola.hoolaclient.dao.ClientMessageMapper;
import com.unisharing.hoola.hoolacommon.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientMessageService")
public class ClientMessageServiceImpl implements IClientMessageService {
    @Autowired
	private ClientMessageMapper clientMessageMapper;
	
	@Override
	public void insertUserMessage(MessageModel messageModel) {
		long uid=messageModel.getUid();
		long rec_uid=messageModel.getRecUid();
		String type="";
		if(uid<rec_uid){
			type=uid+"|"+rec_uid;
		}else if(uid>rec_uid){
			type=rec_uid+"|"+uid;
		}
		messageModel.setType(type);
		clientMessageMapper.insertUserMessage(messageModel);

	}
	
	

	@Override
	public void deleteUserMessage(long uid, int type) {
		if(type==0){
			clientMessageMapper.deleteSendUserMessage(uid);
		}else{
			clientMessageMapper.deleteReceiveUserMessage(uid);
		}
		
	}



	public ClientMessageMapper getClientMessageMapper() {
		return clientMessageMapper;
	}

	public void setClientMessageMapper(ClientMessageMapper clientMessageMapper) {
		this.clientMessageMapper = clientMessageMapper;
	}
	

}
