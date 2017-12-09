package com.unisharing.hoola.hooladatacenter.service.message;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.message.IClientMessageService;
import com.unisharing.hoola.hoolacommon.bean.DialogBean;
import com.unisharing.hoola.hoolacommon.model.MessageModel;
import com.unisharing.hoola.hoolacommon.model.SettingModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.utils.PushSender;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class MessageServiceImpl extends BaseRedisService implements IMessageService {
	Log logger = LogFactory.getLog(MessageServiceImpl.class);
	@Autowired
	IClientMessageService clientMessageService;
    @Autowired
	IRedisUserService redisUserService;
    @Autowired
	IRedisMessageService redisMessageService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleSendMessage() {
		String likeJson = null;
		MessageModel message = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getMessageListKey()).rightPop();
		while(likeJson != null){
			message = JSON.parseObject(likeJson, MessageModel.class);
			//推送私信消息到客户端
			SettingModel setting = redisUserService.getUserSetting(message.getRecUid());

			UserModel revUser = redisUserService.getUser(message.getRecUid());
			if (revUser != null && revUser.getDeviceToken() != null
					&& revUser.getDeviceToken().trim().length() >= 64) {
				int count = redisMessageService.countTotalNewMessage(revUser.getUid());
				Set<String> tokens = redisUserService.getUserDeviceToken(revUser.getUid());
				if (setting.getMessage() == 1) {
					UserModel senderUser = redisUserService.getUser(message.getUid());
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, senderUser.getNickName()
									+ "给我发了私信", count, "qupai://message/dialog");
						}
					}
				} else {
					for (String token : tokens) {
						if (token.trim().length() == 64) {
							PushSender.send(token, null, count, "qupai://message/dialog");
						}
					}
				}
			}
			
			// 插入数据库
			try{
				message.setMessageText(EmojiUtils.encodeEmoji(message.getMessageText()));
				clientMessageService.insertUserMessage(message);
			}catch (Exception e) {
				logger.equals("私信入库出错！Message="+e.getMessage()+",Params:"+message);
			}
			
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getMessageListKey()).rightPop();
		}

	}
	@Override
	public void handleDeleteMessage(long msgId) {
		//TODO 删除数据库数据（私信删除）
		System.out.println("msgId="+msgId);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void handleDeleteDialog() {
		String likeJson = null;
		DialogBean dialog = null;
		likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getDeleteDialogListKey()).rightPop();
		while(likeJson != null){
			dialog = JSON.parseObject(likeJson, DialogBean.class);
			//TODO 删除数据库私信
			likeJson = (String)jmsTemplate.boundListOps(JMSKeyManager.getDeleteDialogListKey()).rightPop();
		}
	}
	public void setClientMessageService(IClientMessageService clientMessageService) {
		this.clientMessageService = clientMessageService;
	}
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}
	public void setRedisMessageService(IRedisMessageService redisMessageService) {
		this.redisMessageService = redisMessageService;
	}
	
}
