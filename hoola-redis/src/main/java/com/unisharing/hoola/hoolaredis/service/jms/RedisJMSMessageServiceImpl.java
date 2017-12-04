package com.unisharing.hoola.hoolaredis.service.jms;


import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.bean.*;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.key.SystemKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;

import java.util.concurrent.TimeUnit;

public class RedisJMSMessageServiceImpl extends BaseRedisService implements
		IRedisJMSMessageService {

	@SuppressWarnings("unchecked")
	@Override
	public long insertAddContentMessageQueue(ContentBean bean) {
		if (bean != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getNewContentMQKey())
					.leftPush(JSON.toJSONString(bean));
		} else {
			return 0;
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public long insertConvertMessageQueue(ContentBean bean) {
		if (bean != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getConvertMqKey()).leftPush(JSON.toJSONString(bean));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long insertFollowMessageQueue(FollowBean bean) {
		if (bean != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getNewFollowListKey())
					.leftPush(JSON.toJSONString(bean));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long insertNewUserMessageQueue(UserModel model) {
		if (model != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getNewUserMQKey())
					.leftPush(JSON.toJSONString(model));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long insertUnFollowMessageQueue(FollowBean bean) {
		if (bean != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getUnFollowListKey())
					.leftPush(JSON.toJSONString(bean));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long insertBindingMessageQueue(BindModel bind) {
		if (bind != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getBindingListKey())
					.leftPush(JSON.toJSONString(bind));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long insertLikeMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getLikeListKey())
					.leftPush(JSON.toJSONString(action));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long deleteLikeMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getLikeListKey()).remove(0, JSON.toJSONString(action));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long insertDislikeMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getDislikeListKey())
					.leftPush(JSON.toJSONString(action));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long deleteDislikeMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getDislikeListKey()).remove(0, JSON.toJSONString(action));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long insertCommentMessageQueue(CommentModel comment) {
		if (comment != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getCommentListKey()).leftPush(JSON.toJSONString(comment));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long insertSendMessageQueue(MessageModel message) {
		if (message != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getMessageListKey()).leftPush(JSON.toJSONString(message));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public long insertDeleteDialogQueue(DialogBean dialog) {
		if (dialog != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getDeleteDialogListKey()).leftPush(JSON.toJSONString(dialog));
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addMobileCode(String mobile, String code) {
		jmsTemplate.boundValueOps(SystemKeyManager.getMobileCodeKey(mobile)).set(code, 30*60, TimeUnit.SECONDS);
	}
	@SuppressWarnings("unchecked")
	@Override
	public String getMobileCode(String mobile) {
		Object obj = jmsTemplate.boundValueOps(SystemKeyManager.getMobileCodeKey(mobile)).get();
		if (obj != null){
			return (String)obj;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteMobileCode(String mobile) {
		jmsTemplate.delete(SystemKeyManager.getMobileCodeKey(mobile));
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertInviteQueue(InviteBean invite) {
		if (invite != null) {
			jmsTemplate.boundListOps(JMSKeyManager.getInviteListKey()).leftPush(JSON.toJSONString(invite));
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertShareQueue(ShareBean bean) {
		if (bean != null){
			jmsTemplate.boundListOps(JMSKeyManager.getShareListKey()).leftPush(JSON.toJSONString(bean));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertFeedBackQueue(FeedBackModel feedback) {
		if (feedback != null){
			jmsTemplate.boundListOps(JMSKeyManager.getFeedBackListKey()).leftPush(JSON.toJSONString(feedback));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertReportQueue(ReportModel report) {
		if (report != null){
			jmsTemplate.boundListOps(JMSKeyManager.getReportListKey()).leftPush(JSON.toJSONString(report));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long insertForwardMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getForwardListKey())
					.leftPush(JSON.toJSONString(action));
		} else {
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public long insertCancelForwardMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getCancelForwardListKey())
					.leftPush(JSON.toJSONString(action));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long deleteForwardMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getForwardListKey())
					.remove(0,JSON.toJSONString(action));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long deleteCancelForwardMessageQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getCancelForwardListKey())
					.remove(0,JSON.toJSONString(action));
		} else {
			return 0;
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void insertAtQueue(AtMessageBean bean) {
		if (bean != null) {
			jmsTemplate.boundListOps(JMSKeyManager.getAtListKey()).leftPush(
					JSON.toJSONString(bean));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertGroupAddQueue(GroupBean group) {
		if(group != null){
			jmsTemplate.boundListOps(JMSKeyManager.getGroupAddListKey()).leftPush(
					JSON.toJSONString(group));
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertGroupEditQueue(GroupBean group) {
		if(group != null){
			jmsTemplate.boundListOps(JMSKeyManager.getGroupEditListKey()).leftPush(
					JSON.toJSONString(group));
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertGroupDeleteQueue(GroupBean group) {
		if(group != null){
			jmsTemplate.boundListOps(JMSKeyManager.getGroupDeleteListKey()).leftPush(
					JSON.toJSONString(group));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertGroupUsersDeleteQueue(GroupBean group) {
		if(group != null){
			jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersDeleteListKey()).leftPush(
					JSON.toJSONString(group));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertGroupUsersAddQueue(GroupBean group) {
		if(group != null){
			jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersAddListKey()).leftPush(
					JSON.toJSONString(group));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertOpenFrienDataSynFromDB(OpenFriend openFriend) {
		if(openFriend != null){
			jmsTemplate.boundListOps(JMSKeyManager.getOpenFriendSynListKey()).leftPush(
					JSON.toJSONString(openFriend));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertContactsUpQueue(MobileMessageBean mobiles) {
		if(mobiles != null){
			jmsTemplate.boundListOps(JMSKeyManager.getMobilesListkey()).leftPush(
					JSON.toJSONString(mobiles));
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertUserLoginQueue(BindModel bind) {
		if(bind != null){
			jmsTemplate.boundListOps(JMSKeyManager.getLoginListKey()).leftPush(
					JSON.toJSONString(bind));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertUserEditQueue(UserModel user) {
		if(user != null){
			jmsTemplate.boundListOps(JMSKeyManager.getUserEditKey()).leftPush(
					JSON.toJSONString(user));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public int getAutoFollowFlag() {
		Object obj = jmsTemplate.boundValueOps(JMSKeyManager.getAutoFollowFlag()).get();
		if (obj == null ){
			return 1;
		}else{
			try{
				return Integer.parseInt((String)obj);
			}catch (Exception e) {
				return 0;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getAutoUploadStatusFlag() {
		Object obj = jmsTemplate.boundValueOps(JMSKeyManager.getAutoUploadStatusFlag()).get();
		if (obj == null ){
			return 1;
		}else{
			try{
				return Integer.parseInt((String)obj);
			}catch (Exception e) {
				return 0;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertCancelLikeForwardQueue(ActionModel action) {
		if (action != null) {
			jmsTemplate.boundListOps(JMSKeyManager.getCancelLikeForwardMqKey())
					.leftPush(JSON.toJSONString(action));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertLikeForwardQueue(ActionModel action) {
		if (action != null) {
			jmsTemplate.boundListOps(JMSKeyManager.getLikeForwardMqKey())
					.leftPush(JSON.toJSONString(action));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long deleteCancelLikeForwardQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getCancelLikeForwardMqKey()).remove(0,JSON.toJSONString(action));
		}
		
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public long deleteLikeForwardQueue(ActionModel action) {
		if (action != null) {
			return jmsTemplate.boundListOps(JMSKeyManager.getLikeForwardMqKey()).remove(0,JSON.toJSONString(action));
		}
		return 0;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertSynWeiboQueue(long uid) {
		
		if (uid > 0) {
			jmsTemplate.boundListOps(JMSKeyManager.getSynWeiboMqKey()).leftPush(String.valueOf(uid));
		}
	}

	
}
