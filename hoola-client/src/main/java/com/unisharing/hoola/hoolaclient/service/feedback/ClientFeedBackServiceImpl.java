package com.unisharing.hoola.hoolaclient.service.feedback;


import com.unisharing.hoola.hoolaclient.dao.ClientFeedBackMapper;
import com.unisharing.hoola.hoolacommon.model.FeedBackModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientFeedBackService")
public class ClientFeedBackServiceImpl implements IClientFeedBackService {

	@Autowired
	ClientFeedBackMapper clientFeedBackMapper;
	
	@Override
	public void insertUserFeedBackModel(FeedBackModel feedBackModel) {
		clientFeedBackMapper.insertUserFeedBackModel(feedBackModel);
	}
	

	public ClientFeedBackMapper getClientFeedBackMapper() {
		return clientFeedBackMapper;
	}

	public void setClientFeedBackMapper(ClientFeedBackMapper clientFeedBackMapper) {
		this.clientFeedBackMapper = clientFeedBackMapper;
	}
	
	
	

	


}
