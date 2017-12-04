package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class HotContentKeyManager {
	
	private static String USER_RECOMMEND = "user_recomment";//推荐用户
	
	private static String TALENT_RECOMMEND = "talent_recomment";//推荐达人用户
	
	private static String RECOMMEND_USER_DATA = "rec:user:{0}:data";//推荐用户详细信息主要包括推荐理由
	
	private static String TOP_ADV = "top_adv";//运营条
	
	private static String TOP_ACTIVE = "top_active";//编辑推荐标签
	
	private static String TOP_TAG = "top_tag";//热门标签
	
	private static String MAIN_TOP_TAG = "main_top_tag";//首页热门标签
	
	private static String SYS_TOP_TAG = "sys_top_tag";//系统推荐热门标签
	
	private static String TOP_USER = "l:top:user";//热门用户
	
	private static String TOP_CONTENT = "l:top:content";//热门内容
	
	private static String QUPAI_TOP_CONTENT = "qupai:top:content";//趣拍置顶内容
	
	private static String HOT_CHANNEL_LIST = "hot:channel:list";//热门频道列表
	
	private static String SUBJECT_DATA = "subject:{0}:data";//话题内容存储
	
	private static String SUBJECU_CONTENT_LIST = "subject:{0}:contents";//话题内容列表
	
	private static String SUBJECT_LIST = "subject:list";//话题列表
	
	private static String TOP_CONTENT_ALL = "top:content:all";//内容总榜单
	
	private static String TOP_CONTENT_WEEK = "top:content:week";//内容周榜单
	
	
	/**
	 * 取得推荐用户KEY
	 * @return
	 */
	public static String getRecommendUserKey() {
		return USER_RECOMMEND;
	}
	
	/**
	 * 取得运营条内容key
	 * @return
	 */
	public static String getTopAdvKey(){
		return TOP_ADV;
	}

	/**
	 * 取得热门活动
	 * @return
	 */
	public static String getTopActiveKey(){
		return TOP_ACTIVE;
	}
	
	/**
	 * 取得热门标签
	 * @return
	 */
	public static String getTopTagKey(){
		return TOP_TAG;
	}
	/**
	 * 取得首页热门标签
	 * @return
	 */
	public static String getMainTopTagKey(){
		return MAIN_TOP_TAG;
	}
	
	/**
	 * 系统自动推荐热门标签
	 * @return
	 */
	public static String getSysTopTagKey(){
		return SYS_TOP_TAG;
	}
	
	/**
	 * 取得热门用户Key
	 * @return
	 */
	public static String getTopUserKey(){
		return TOP_USER;
	}
	
	/**
	 * 取得热门内容Key
	 * @return
	 */
	public static String getTopContentKey(){
		return TOP_CONTENT;
	}
	/**
	 * 总榜单
	 * @return
	 */
	public static String getAllTopContentKey(){
		return TOP_CONTENT_ALL;
	}
	
	/**
	 * 周榜单
	 * @return
	 */
	public static String getWeekTopContentKey(){
		return TOP_CONTENT_WEEK;
	}
	
	/**
	 * 趣拍置顶内容；
	 * @return
	 */
	public static String getQupaiTopContentKey(){
		return QUPAI_TOP_CONTENT;
	}
	
	/**
	 * 取得热门频道列表key
	 * @return
	 */
	public static String getChannelListKey(){
		return HOT_CHANNEL_LIST;
	}
	
	/**
	 * 推荐达人用户列表
	 * @return
	 */
	public static String getRecommendTalentListKey(){
		return TALENT_RECOMMEND;
	}
	
	/**
	 * 推荐用户详细信息key
	 * @param uid
	 * @return
	 */
	public static String getRecommendUserDataKey(long uid){
		return MessageFormat.format(RECOMMEND_USER_DATA, String.valueOf(uid));
	}
	
	/**
	 * 取得话题信息存储KEY
	 * @param sid
	 * @return
	 */
	public static String getSubjectDateKey(int sid){
		return MessageFormat.format(SUBJECT_DATA, String.valueOf(sid));
	}
	
	/**
	 * 取得话题列表key
	 * @return
	 */
	public static String getSubjectListKey(){
		return SUBJECT_LIST;
	}
	
	/**
	 * 获取话题内容列表Key
	 * @param sid
	 * @return
	 */
	public static String getSubjectContentsKey(long sid){
		return MessageFormat.format(SUBJECU_CONTENT_LIST, sid);
	}
}
