package com.unisharing.hoola.hoolaredis.service.badword;

public interface IBadwordService {
	
	/**
	 * 判断是否有敏感词
	 * @param string
	 * @return
	 */
	String hasBadWord(String string);
	
	/**
	 * 过滤敏感词
	 * @param string
	 * @return
	 */
	String filterBadword(String string);
	
	/**
	 * 获取新版本更新提示信息
	 * @return
	 */
	String getNewVersionTips();

}
