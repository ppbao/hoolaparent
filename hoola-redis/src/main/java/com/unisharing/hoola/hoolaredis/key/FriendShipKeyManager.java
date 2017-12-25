package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class FriendShipKeyManager {

	public static final String USER_FOLLOWS_KEY = "ss:user:{0}:follows";// 用户关注列表 Sort Set
	
	public static final String USER_NEW_FANS_KEY = "ss:user:{0}:fans:new";//用户新粉丝列表

	public static final String USER_FANS_KEY = "ss:user:{0}:fans"; // 用户粉丝列表 Sort Set
	
	public static final String FAMOUS_USER_KEY = "set:famous"; //名人列表
	
	public static final String USER_FOLLOW_FAMOUS_KEY = "set:user:{0}:follow:famous";//用户关注列表中的名人
	
	public static final String USER_FRIEND_KEY = "ss:user:{0}:friends";//相互关注用户（好友）
	
	public static final String USER_SINA_FRIEND_NEW = "ss:user:{0}:sina:friend:new";//用户最新新浪已经加入Hoola好友
	
	public static final String USER_SINA_FRIENDS = "ss:user:{0}:sina:friend";//用户新浪已经加入Hoola好友
	
	public static final String USER_TENCENT_FRIEND_NEW = "ss:user:{0}:tencent:friend:new";//用户最新腾讯微波已经加入Hoola好友
	
	public static final String USER_TENCENT_FRIENDS = "ss:user:{0}:tencent:friend";//用户腾讯微波已经加入Hoola好友
	
	public static final String USER_MOBILE_FRIEND_NEW = "ss:user:{0}:mobile:friend:new";//用户最新通讯录已经加入Hoola好友
	
	public static final String USER_MOBILE_FRIENDS = "ss:user:{0}:mobile:friend";//用户通讯录已经加入Hoola好友
	
	public static final String USER_NEW_FRENDS = "user:{0}:friends:new";//用户所有新匹配的好友列表Key
	
	public static final String SINA_NO_MATCH_FRIENDS = "ss:user:{0}:sina:nomatched:friends";//新浪未匹配成功列表
	
	public static final String TENCENT_NO_MATCH_FRIENDS = "ss:user:{0}:tencent:nomatched:friends";//新浪未匹配成功列表
	
	public static final String MOBILE_NO_MATCH_FRIENDS = "ss:user:{0}:mobile:nomatched:friends";//新浪未匹配成功列表
	
	public static final String USER_MOBILES = "hm:user:{0}:mobiles:{1}";//存储通讯录数据 //{0}:所属用户ID，{1}手机号码 ，主要是因为同一个号码在不同人手机里面名字不相同
	
	public static final String USER_SINAS = "hm:user:{0}:sina";//存储新浪用户数据	//{0}新浪用户ID
	
	public static final String USER_TENCENTS = "hm:user:{0}:tencent";//存储腾讯用户数据 //{0}腾讯用户ID
	
	public static final String ALL_SINA_USERS = "ss:user:{0}:all:sina";//存储用户所有新浪好友数据
	
	public static final String ALL_TENCENT_USERS = "ss:user:{0}:all:tencent";//存储用户所有腾讯好友数据
	
	public static final String USER_BLACKLIST = "user:{0}:blacklist";//存储用户黑名单
	
	public static final String USER_ALL_FRIEND_NEW="ss:user:{0}:all:friend:new";//用于存储最新新浪腾讯手机加入的Hoola好友
	
	
	
	
	
	
	

	/**
	 * 根据ID取出用户关注列表Key
	 * 
	 * @param uid
	 * @return
	 */
	public static String getFollowKey(long uid) {
		return MessageFormat.format(USER_FOLLOWS_KEY, uid);
	}
	
	/**根据ID取出用户最新的第三方平台加入Hoola的好友
	 * @param uid
	 * @return
	 */
	public static String getUserAllFriendNew(long uid){
		return MessageFormat.format(USER_ALL_FRIEND_NEW, uid);
	}

	/**
	 * 取出用户粉丝列表Key
	 * 
	 * @param uid
	 * @return
	 */
	public static String getFansKey(long uid) {
		return MessageFormat.format(USER_FANS_KEY, uid);
	}
	
	/**
	 * 获取名人key
	 * @return
	 */
	public static String getFamousUserKey(){
		return FAMOUS_USER_KEY;
	}
	
	/**
	 * 取得用户关注中名人列表
	 * @param uid
	 * @return
	 */
	public static String getUserFollowFamousKey(long uid){
		return MessageFormat.format(USER_FOLLOW_FAMOUS_KEY, String.valueOf(uid));
	}
	
	/**
	 * 取得用户好友key
	 * @param uid
	 * @return
	 */
	public static String getUserFriendKey(long uid){
		return MessageFormat.format(USER_FRIEND_KEY, String.valueOf(uid));
	}
	
	/**
	 * 取得用户新浪最新加入Hoola好友Key
	 * @param uid
	 * @return
	 */
	public static String getUserSinaNewFriendKey(long uid){
		return MessageFormat.format(USER_SINA_FRIEND_NEW, String.valueOf(uid));
	}
	
	/**
	 * 取得用户新浪已经加入Hoola好友key
	 * @param uid
	 * @return
	 */
	public static String getUserSinaFriendKey(long uid){
		return MessageFormat.format(USER_SINA_FRIENDS, String.valueOf(uid));
	}
	
	
	/**
	 * 取得用户腾讯微博最新加入Hoola好友Key
	 * @param uid
	 * @return
	 */
	public static String getUserTencentNewFriendKey(long uid){
		return MessageFormat.format(USER_TENCENT_FRIEND_NEW, String.valueOf(uid));
	}
	
	/**
	 * 取得用户新浪已经加入Hoola好友key
	 * @param uid
	 * @return
	 */
	public static String getUserTencentFriendKey(long uid){
		return MessageFormat.format(USER_TENCENT_FRIENDS, String.valueOf(uid));
	}
	
	/**
	 * 取得用户腾讯微博最新加入Hoola好友Key
	 * @param uid
	 * @return
	 */
	public static String getUserMobileNewFriendKey(long uid){
		return MessageFormat.format(USER_MOBILE_FRIEND_NEW, String.valueOf(uid));
	}
	
	/**
	 * 取得用户新浪已经加入Hoola好友key
	 * @param uid
	 * @return
	 */
	public static String getUserMobileFriendKey(long uid){
		return MessageFormat.format(USER_MOBILE_FRIENDS, String.valueOf(uid));
	}
	
	/**
	 * 用户新浪未匹配成功数据Key
	 * @param uid
	 * @return
	 */
	public static String getSinaNoMatchedFriends(long uid){
		return MessageFormat.format(SINA_NO_MATCH_FRIENDS, uid);
	}

	
	/**
	 * 用户腾讯未匹配成功数据Key
	 * @param uid
	 * @return
	 */
	public static String getTencentNoMatchedFriends(long uid){
		return MessageFormat.format(TENCENT_NO_MATCH_FRIENDS, uid);
	}

	
	
	/**
	 * 用户手机未匹配成功数据Key
	 * @param uid
	 * @return
	 */
	public static String getMobileNoMatchedFriends(long uid){
		return MessageFormat.format(MOBILE_NO_MATCH_FRIENDS, uid);
	}
	
	/**
	 * 取得手机号码对应的信息
	 * @param uid
	 * @param mobile
	 * @return
	 */
	public static String getMobilesUserInfoKey(long uid,String mobile){
		return MessageFormat.format(USER_MOBILES, String.valueOf(uid),mobile);
	}

	/**
	 * 根据第三方平台ID取得新浪用户
	 * @param openUserId
	 * @return
	 */
	public static String getSinaUserInfoKey(String openUserId){
		return MessageFormat.format(USER_SINAS, openUserId);
	}
	
	/**
	 * 根据第三方平台ID取出腾讯用户信息
	 * @param openUserId
	 * @return
	 */
	public static String getTencentUserInfoKey(String openUserId){
		return MessageFormat.format(USER_TENCENTS, openUserId);
	}
	
	/**
	 * 获取用户所有新浪好友数据
	 * @param uid
	 * @return
	 */
	public static  String getUserAllSinaFollowsKey(long uid){
		return MessageFormat.format(ALL_SINA_USERS, String.valueOf(uid));
	}
	
	/**
	 * 存取用户所有腾讯好友数据
	 * @param uid
	 * @return
	 */
	public static  String getUserAllTencentFollowsKey(long uid){
		return MessageFormat.format(ALL_TENCENT_USERS, String.valueOf(uid));
	}
	
	/**
	 * 存取用户新的粉丝数据
	 * @param uid
	 * @return
	 */
	public static String getNewFansKey(long uid){
		return MessageFormat.format(USER_NEW_FANS_KEY, String.valueOf(uid));
	}
	/**
	 * 用户黑名单KEY
	 * @param uid
	 * @return
	 */
	public static String getUserBlacklistKey(long uid){
		return MessageFormat.format(USER_BLACKLIST, String.valueOf(uid));
	}
	
	/**
	 * 获取用户所有平台好友列表合并的结果集
	 * @param uid
	 * @return
	 */
	public static String getUserAllNewMatchedFrendsKey(long uid){
		return MessageFormat.format(USER_NEW_FRENDS, String.valueOf(uid));
	}
}
