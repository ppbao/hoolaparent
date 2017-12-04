package com.unisharing.hoola.hoolaredis.pubsub;


import com.unisharing.hoola.hoolacommon.NoticeMessage;

public interface IRedisPublisher {
	
	
	/**
	 * 发送消息
	 * @param msg
	 */
	public void publish(NoticeMessage msg);
}
