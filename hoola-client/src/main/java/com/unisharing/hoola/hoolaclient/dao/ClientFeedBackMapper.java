package com.unisharing.hoola.hoolaclient.dao;


import com.unisharing.hoola.hoolacommon.model.FeedBackModel;
import org.springframework.stereotype.Component;

@Component
public interface ClientFeedBackMapper {
	
	void insertUserFeedBackModel(FeedBackModel feedBackModel);//

}
