package com.unisharing.hoola.hoolaredis.service.syn.system;


import com.unisharing.hoola.hoolaredis.key.SystemKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;

public class SystemSynServiceImpl extends BaseRedisService implements ISystemSynService {

	@SuppressWarnings("unchecked")
	@Override
	public void addBadword(String badword) {
		jmsTemplate.boundSetOps(SystemKeyManager.getBadwordsKey()).add(badword.trim());
		//重建
		//Set wordSet = jmsTemplate.boundSetOps(SystemKeyManager.getBadwordsKey()).members();
		//HoolaUtils.wordSetToMap(wordSet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBadword(String badword) {
		jmsTemplate.boundSetOps(SystemKeyManager.getBadwordsKey()).remove(badword.trim());
		//重建
		//Set wordSet = jmsTemplate.boundSetOps(SystemKeyManager.getBadwordsKey()).members();
		//HoolaUtils.wordSetToMap(wordSet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void synNewVersionTips(String tips) {
		jmsTemplate.boundValueOps(SystemKeyManager.getNewVersionTipsKey()).set(tips);
		
	}

}
