package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class SystemKeyManager {
	
	private static String USER_SETTING = "hm:user:{0}:setting";
	
	private static String MOBILE_CODE = "kv:mobile:";
	
	private static String SEARCH_TAG_LIST = "l:tag:search";//搜索推荐
	
	private static String PUBLISH_TAG_LIST = "l:tag:publish";//发布推荐
	
	private static String BADWORDS = "set:badwords";
	
	private static String NEW_VERSION_TIPS = "new:version:tips";//新版本提示语
	
	private static String MUEIC_LIST = "musics";//音乐列表
	
	private static String EXPRESSION_LIST = "expriessions";//表情列表
	
	private static String PASTER_LIST = "pasters";//贴纸列表
	
	private static String RESOURCE_DATA = "res:{0}:data";//资源信息
	
	private static String EXPRESSION_SET="set:{0}:expriessions";//表情对应的用户信息
	
	private static String PASTER_SET="set:{0}:pasters";//贴纸对应的用户信息
	
	private static String EXPRESSION_AND_PASTER="expriessions:pasters";//贴纸表情列表
	
	public static String getUserSettingKey(long uid){
		return MessageFormat.format(USER_SETTING, String.valueOf(uid));
	}
	
	/**
	 * 表情贴纸KEY
	 * @return
	 */
	public static String getExpressionAndPasterKey(){
		return EXPRESSION_AND_PASTER;
	}

	public static String getMobileCodeKey(String mobile){
		return MOBILE_CODE + mobile;
	}
	
	public static String getSearchTag(){
		return  SEARCH_TAG_LIST;
	}
	
	public static String getPublishTag(){
		return PUBLISH_TAG_LIST;
	}
	
	public static String getBadwordsKey(){
		return BADWORDS;
	}
	
	public static String getNewVersionTipsKey(){
		return NEW_VERSION_TIPS;
	}
	
	/**
	 * 获取音乐列表KEY
	 * @return
	 */
	public static String getMusicsKey(){
		return MUEIC_LIST;
	}
	
	/**
	 * 获取表情列表KEY
	 * @return
	 */
	public static String getExpressionsKey(){
		return EXPRESSION_LIST;
	}
	
	/**
	 * 获取贴纸列表KEY
	 * @return
	 */
	public static String getPastersKey(){
		return PASTER_LIST;
	}
	
	/**
	 * 资源信息KEY
	 * @param id
	 * @return
	 */
	public static String getResourceKey(long id){
		return MessageFormat.format(RESOURCE_DATA, String.valueOf(id));
	}
	
	/**表情对应用户KEY
	 * @param id
	 * @return
	 */
	public static String getExpressionsSetKey(long id){
		return MessageFormat.format(EXPRESSION_SET, String.valueOf(id));
	}
	/**贴纸对应用户KEY
	 * @param id
	 * @return
	 */
	public static String getPastersSetKey(long id){
		return MessageFormat.format(PASTER_SET, String.valueOf(id));
	}
	
}
