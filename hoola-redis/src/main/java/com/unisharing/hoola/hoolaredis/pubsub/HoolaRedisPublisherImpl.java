package com.unisharing.hoola.hoolaredis.pubsub;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service(value = "hoolaPublisher)")
public class HoolaRedisPublisherImpl extends BaseRedisService implements IRedisPublisher {

	private ChannelTopic topic;
	@Override
	public void publish(NoticeMessage msg) {
		jmsTemplate.convertAndSend(topic.getTopic(), JSON.toJSONString(msg));
	}

	public void setTopic(ChannelTopic topic) {
		this.topic = topic;
	}
}
