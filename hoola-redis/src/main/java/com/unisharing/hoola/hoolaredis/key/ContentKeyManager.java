package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class ContentKeyManager {
	
	private static String CONTENT_ID_GLOBAL_KEY = "kv:content:global:id"; //内容ID生成key String
	
	private static String CONTENT_KEY_ID = "kv:content:key:";//key和ID对应
	
	private static String CONTENT_INFO_KEY = "hm:content:{0}:data";//内容信息Key着Hashmap
	
	private static String CONTENT_OPT_USER_KEY = "ss:content:{0}:opt:user";//内容操作用户
	
	private static String CONTENT_LIKE_USER_KEY = "ss:content:{0}:like:user";//内容喜欢用户
	
	private static String CONTENT_FORWARD_USER_KEY = "ss:content:{0}:forward:user";//内容转发用户
	
	private static String CONTENT_COMMENT_USER_KEY = "ss:content:{0}:comment:user";//内容评论用户列表
	
	private static String CONTENT_LIKE_NUM_KEY = "kv:content:{0}:likenum";//喜欢数
	
	private static String CONTENT_FORWARD_NUM_KEY = "kv:content:{0}:forward";//转发数
	
	private static String CONTENT_COMMENT_NUM_KEY = "kv:content:{0}:commentnum";//评论数
	
	private static String CONTENT_SINA_SHARE_NUM_KEY = "kv:content:{0}:sina";//新浪分享数
	
	private static String CONTENT_FRIENDS_SHARE_NUM_KEY = "kv:content:{0}:friend";//朋友圈分享数
	
	private static String CONTENT_PLAY_NUM_KEY = "kv:content:{0}:playnum";//播放那个次数
	
	private static String CONTENT_LIST = "l:content:list";//所有公开最新内容列表
	
	private static String CONTENT_FIND_LIST = "l:content:find";//发现内容列表
	
	private static String CONTENT_SHOW_QUEUE = "cid:queue";//播放次数同步
	
	private static String CONTENT_OPT_USER_CACHE_KEY = "ss:content:{0}:opt:user:cache";//操作用户缓存
	
	private static String CONTENT_AT_USER_CACHE_KEY = "content:{0}:at:users";//内容的At用户
	
	
	/**
	 * 取得内容序列ID Key
	 * @return
	 */
	public static String getContentIdKey(){
		return CONTENT_ID_GLOBAL_KEY;
	}
	
	/**
	 * 根据内容ID取得内容详细信息key
	 * @param cid
	 * @return
	 */
	public static String getContentInfoKey(String cid){
		return MessageFormat.format(CONTENT_INFO_KEY, cid);
	}
	
	/**
	 * 内容操作用户列表Key
	 * @param cid
	 * @return
	 */
	public static String getContentOptUserKey(long cid){
		return MessageFormat.format(CONTENT_OPT_USER_KEY, String.valueOf(cid));
	}
	
	/**
	 * 内容喜欢用户
	 * @param cid
	 * @return
	 */
	public static String getContentLikeUserkey(long cid){
		return MessageFormat.format(CONTENT_LIKE_USER_KEY, String.valueOf(cid));
	}
	
	/**
	 * 内容转发用户
	 * @param cid
	 * @return
	 */
	public static String getContentForwardUserkey(long cid){
		return MessageFormat.format(CONTENT_FORWARD_USER_KEY, String.valueOf(cid));
	}
	
	/**
	 * 评论用户列表
	 * @param cid
	 * @return
	 */
	public static String getContentCommentUserKey(long cid){
		return MessageFormat.format(CONTENT_COMMENT_USER_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得内容的评论数key
	 * @param cid
	 * @return
	 */
	public static String getContentCommentNumKey(long cid){
		return MessageFormat.format(CONTENT_COMMENT_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得内容的喜欢数key
	 * @param cid
	 * @return
	 */
	public static String getContentLikeNumKey(long cid){
		return MessageFormat.format(CONTENT_LIKE_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得内容的喜欢数key
	 * @param cid
	 * @return
	 */
	public static String getContentForwardNumKey(long cid){
		return MessageFormat.format(CONTENT_FORWARD_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得内容播放次数Key
	 * @param cid
	 * @return
	 */
	public static String getContentPlayNumKey(long cid){
		return MessageFormat.format(CONTENT_PLAY_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 最新内容列表Key
	 * @return
	 */
	public static String getContentListKey(){
		return CONTENT_LIST;
	}
	
	/**
	 * 内容发现列表
	 * @return
	 */
	public static String getContentFindListKey(){
		return CONTENT_FIND_LIST;
	}
	
	/**
	 * 取出根据Key获取Id的key
	 * @param key
	 * @return
	 */
	public static String getContentIdByKey(String key){
		return CONTENT_KEY_ID+key;
	}
	
	/**
	 * 同步播放次数
	 * @return
	 */
	public static String getContentIdQueueKey(){
		return CONTENT_SHOW_QUEUE;
	}
	
	/**
	 * 取得内容新浪分享
	 * @param cid
	 * @return
	 */
	public static String getSinaShareNumKey(long cid){
		return MessageFormat.format(CONTENT_SINA_SHARE_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得朋友圈分享次数
	 * @param cid
	 * @return
	 */
	public static String getFriendsShareNumKey(long cid){
		return MessageFormat.format(CONTENT_FRIENDS_SHARE_NUM_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得内容操作用户缓存数据
	 */
	public static String getOptUserCacheKey(long cid){
		return MessageFormat.format(CONTENT_OPT_USER_CACHE_KEY, String.valueOf(cid));
	}
	
	/**
	 * 获取内容At用户列表
	 */
	public static String getAtUserCacheKey(long cid){
		return MessageFormat.format(CONTENT_AT_USER_CACHE_KEY, String.valueOf(cid));
	}
}
