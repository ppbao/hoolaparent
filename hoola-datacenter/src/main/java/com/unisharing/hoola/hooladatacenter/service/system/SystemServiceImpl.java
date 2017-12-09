package com.unisharing.hoola.hooladatacenter.service.system;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.feedback.IClientFeedBackService;
import com.unisharing.hoola.hoolaclient.service.report.IClientReportService;
import com.unisharing.hoola.hoolaclient.service.user.IClientUserService;
import com.unisharing.hoola.hoolacommon.model.FeedBackModel;
import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import com.unisharing.hoola.hoolacommon.model.ReportModel;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SystemServiceImpl extends BaseRedisService implements ISystemService {
    @Autowired
	IClientFeedBackService clientFeedBackService;
	@Autowired
	IClientReportService clientReportService;
	@Autowired
	IClientUserService clientUserService;
	@Autowired
	IRedisRelationshipService redisRelationshipService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleFeedback() {
		String likeJson = null;
		FeedBackModel feedback = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getFeedBackListKey()).rightPop();
		while(likeJson != null){
			feedback = JSON.parseObject(likeJson, FeedBackModel.class);
			
			//反馈数据插入数据库
			clientFeedBackService.insertUserFeedBackModel(feedback);
			
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getFeedBackListKey()).rightPop();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleReport() {
		String json = null;
		ReportModel report = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getReportListKey()).rightPop();
		while(json != null){
			report = JSON.parseObject(json, ReportModel.class);
			
			// 插入数据库
			clientReportService.insertReportModel(report);
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getReportListKey()).rightPop();
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleOpenFriendSyn() {
		String json = null;
		OpenFriend open = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getOpenFriendSynListKey()).rightPop();
		while(json != null){
			open = JSON.parseObject(json, OpenFriend.class);
			// 同步数据
			List<OpenFriend> friends = clientUserService.queryOpenFriendListByUid(open);
			redisRelationshipService.insertNoMatchFriends(open.getUid(),friends,  open.getOpenType());
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getOpenFriendSynListKey()).rightPop();
		}
	}

	public void setClientFeedBackService(
			IClientFeedBackService clientFeedBackService) {
		this.clientFeedBackService = clientFeedBackService;
	}

	public void setClientReportService(IClientReportService clientReportService) {
		this.clientReportService = clientReportService;
	}

	public void setClientUserService(IClientUserService clientUserService) {
		this.clientUserService = clientUserService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

}
