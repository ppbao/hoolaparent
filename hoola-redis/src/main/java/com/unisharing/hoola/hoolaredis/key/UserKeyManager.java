package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class UserKeyManager {
	
	private static String USER_ID_GLOBAL_KEY = "kv:user:global:id"; //用户ID生成key String
	private static String USER_INFO_KEY = "hm:user:{0}:data";//用户信息Key Hashmap	{0}uid
	private static String USEREMAIL_ID_KEY_PERFIX = "kv:user:email:";//用户名-用户ID对应关系 String
	private static String USERLIST_KEY = "l:user:timeline";//用户按照时间排序List
	private static String USER_ID_PASSWORD_KEY = "kv:user:{0}:password";//{0}uid
	private static String USER_TOKEN_ID_KEY = "kv:user:token:";
	private static String USER_3TH_ID_KEY = "kv:user:open:{0}:{1}";//{0}openType,{1}openUserId
	private static String USER_BIND_INFO_KEY = "hm:user:{0}:{1}:bind";// {0} uid 平台Uid，BindModel
	private static String USER_DEVICE_TOKEN_KEY = "hm:user:{0}:device";//手机设备号
	
	private static String USER_MOBILE_KEY = "kv:mobile:";//手机号码和用户ID对应关系
	
	private static String USER_SETTING_KEY = "hm:user:{0}:setting";//用户设置
	
	private static String USER_LIKE_SYN_NUM_ONEDAY = "kv:user:{0}:like:syn:num";//用户喜欢同步数统计
	
	private static String USER_NICKNAME_ID_KEY = "kv:user:nickname:";//用户昵称和id对应关系
	
	private static String USER_UPDATE_QUEUE_KEY = "l:uid:queue";//用户更新关系数据
	
	private static String USER_LAST_VISIT_KEY="hm:user:{0}:visit";//记录用户最后一次访问某功能点的时间
	


	/**
	 * 取得新用户自增ID，
	 * @return
	 */
	public static String getUserIdKey(){
		return USER_ID_GLOBAL_KEY;
	}
	
	/**
	 * 取得获取用户详细信息（HashMap）key
	 * @param uid
	 * @return
	 */
	public static String getUserInfoKey(long uid){
		return MessageFormat.format(USER_INFO_KEY, String.valueOf(uid));
	}
	
	/**取得用户最后一次访问KEY
	 * @param uid
	 * @return
	 */
	public static String getUserLastVisit(long uid){
		return MessageFormat.format(USER_LAST_VISIT_KEY, String.valueOf(uid));
	}
	
	/**
	 * 获取通过用户名取得用户ID的可以
	 * @param username
	 * @return
	 */
	public static String getUserIdByEmailKey(String email){
		return USEREMAIL_ID_KEY_PERFIX+email;
	}
	
	
	/**
	 * 根据用户ID取出用户密码
	 * @param uid
	 * @return
	 */
	public static String getUserPasswordByIdKey(long uid){
		return MessageFormat.format(USER_ID_PASSWORD_KEY, uid);
	}
	
	/**
	 * 通过TOken获取 用户ID
	 * @param token
	 * @return
	 */
	public static String getUserIdByTokenKey(String token){
		return USER_TOKEN_ID_KEY + token;
	}
	
	/**
	 * 通过第三方平台信息取得用户ID
	 * @param openType
	 * @param openUserId
	 * @return
	 */
	public static String getUserIdBy3thKey(String openType,String openUserId){
		return MessageFormat.format(USER_3TH_ID_KEY, openType,openUserId);
	}
	/**
	 * 取得通过用户注册时间队列 List
	 * @return
	 */
	public static String getUserByTimeKey(){
		return USERLIST_KEY;
	}
	
	/**
	 * 生产绑定信息KEY
	 * @param uid
	 * @return
	 */
	public static String getUserBindInfoKey(long uid,int openType){
		return MessageFormat.format(USER_BIND_INFO_KEY,String.valueOf(uid),String.valueOf(openType));
	}
	
	/**
	 * 手机号码和用户ID对应Key
	 * @param mobile
	 * @return
	 */
	public static String getUserMobileKey(String mobile){
		return USER_MOBILE_KEY + mobile;
	}
	
	/**
	 * 用户设置Key
	 * @param uid
	 * @return
	 */
	public static String getUserSettingKey(long uid){
		return MessageFormat.format(USER_SETTING_KEY, String.valueOf(uid));
	}
	
	/**
	 * 手机设备号
	 * @param uid
	 * @return
	 */
	public static String getUserDeviceTokenKey(long uid){
		return MessageFormat.format(USER_DEVICE_TOKEN_KEY, String.valueOf(uid));
	}
	
	/**
	 * 统计每天用户喜欢同步数Key
	 * @param uid
	 * @return
	 */
	public static String getUserLikeSysNumKey(long uid){
		return MessageFormat.format(USER_LIKE_SYN_NUM_ONEDAY, String.valueOf(uid));
	}
	
	/**
	 * 获取用户昵称和ID对应关系
	 * @param nickName
	 * @return
	 */
	public static String getUserIdByNickName(String nickName){
		return USER_NICKNAME_ID_KEY + nickName;
	}
	
	/**
	 * 异步更新用户粉丝数，关注数，好友数消息队列
	 * @return
	 */
	public static String getUserUpdateQueueKey(){
		return USER_UPDATE_QUEUE_KEY;
	}
}
