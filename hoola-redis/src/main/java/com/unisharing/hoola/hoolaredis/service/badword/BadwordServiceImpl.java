package com.unisharing.hoola.hoolaredis.service.badword;

import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolaredis.key.SystemKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.springframework.stereotype.Service;


import java.util.Set;
@Service("badService")
public class BadwordServiceImpl extends BaseRedisService implements
		IBadwordService {

	@Override
	public String filterBadword(String string) {
		this.loadBadword();
		return HoolaUtils.filter(string, HoolaUtils.badwordMap);
	}

	@Override
	public String hasBadWord(String string) {
		this.loadBadword();
		return HoolaUtils.hasSensitiveWord(string, HoolaUtils.badwordMap);
	}
	
	@SuppressWarnings("unchecked")
	public void loadBadword(){
		Set wordSet = jmsTemplate.boundSetOps(SystemKeyManager.getBadwordsKey()).members();
		HoolaUtils.wordSetToMap(wordSet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getNewVersionTips() {
		Object obj = jmsTemplate.boundValueOps(SystemKeyManager.getNewVersionTipsKey()).get();
		if (obj != null){
			return (String) obj;
		} else {
			return "新版本上线，赶紧更新吧！";
		}
	}

}
