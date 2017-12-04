package com.unisharing.hoola.hoolaredis.service.syn.system;

public interface ISystemSynService {
	
	/**
	 * 添加敏感词
	 * @param badword
	 */
	public void addBadword(String badword);
	
	/**
	 * 删除敏感词
	 * @param badword
	 */
	public void deleteBadword(String badword);
	
	/**
	 * 新版本提示语
	 * @param tips
	 */
	public void synNewVersionTips(String tips);

}
