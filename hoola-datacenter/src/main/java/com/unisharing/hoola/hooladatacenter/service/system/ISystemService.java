package com.unisharing.hoola.hooladatacenter.service.system;

public interface ISystemService {
	
	/**
	 * 处理意见反馈
	 */
	public void handleFeedback();
	
	/**
	 * 处理举报
	 */
	public void handleReport();
	
	/**
	 * 同步第三方好友数据
	 */
	public void handleOpenFriendSyn();
	
}
