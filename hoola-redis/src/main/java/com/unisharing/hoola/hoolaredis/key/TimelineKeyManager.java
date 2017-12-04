package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class TimelineKeyManager {
	
	private static String USER_PUBLIC_TIMELINE = "set:user:{0}:public:timeline";//用户接受公开内容推送动态列表（包括关注用户喜欢和发表的）
	
	private static String USER_PUBLIC_TIMELINE_CREATE = "user:{0}:create:timeline";//用户接受公开内容推送列表（关注用户发表的）
	
	private static String USER_PUBLIC_TIMELINE_FORWARD = "user:{0}:forward:timeline";//用户接受公开内容推送列表（关注用户转发的）
	
	private static String USER_PUBLIC_LIKE_TIMELINE = "user:{0}:like";//用户已经接受喜欢推送的内容（主要用户内容排重）
	
	public static String USER_GROUP_TIMELINE = "set:user:{0}:group:timeline";//用户组内分享推送动态列表（关注用户发表的）
	
	private static String USER_PUBLIC = "set:user:{0}:public";//用户公开内容列表
	
	public static String USER_PRIVATE = "set:user:{0}:private";//用户私密内容列表
	
	public static String USER_GROUP_CONTENT = "set:user:{0}:group";//用户组内分享
	
	public static String USER_FORWARD = "set:user:{0}:forward";//用户转发列表

	public static String USER_LIKE = "set:user:{0}:like";//用户喜欢列表
	
	public static String USER_SHARE = "set:user:{0}:share";//用户分享列表
	
	public static String USER_AT = "set:user:{0}:at";//用户At消息列表
	
	public static String EDITOR_RECOMMEND = "set:editor:recommend";//编辑推荐列表
	
	public static String EDITOR_RECOMMEND_TOP = "set:editor:recommend:top";//编辑推荐最新几条
	
	public static String USER_CONTENT_LIST_OUT = "set:user:{0}:contents";//用户自己的内容列表
	
	public static String USER_CONTENT_LIST_OTHER_OUT = "set:user:{0}:contents:{1}";//访问别人个人主页内容列表
	
	public static String USER_ALL_CONTENT_LIST_OTHER_OUT = "set:user:{0}:all:contents:{1}";//访问别人个人主页所有内容列表
	
	public static String USER_ALL_CONTENT_LIST_OUT = "set:user:{0}:all:contents";//用户自己的所有内容列表
	
	public static String USER_TIMELINE_OUT	= "set:user:{0}:timeline";//用户动态列表
	
	public static String USER_QUPAI_LIST = "set:user:{0}:qupai";//用户获取趣拍发布的内容，需要获取趣拍注册以后的数据
	
	public static String USER_LAST_VISIT_TIME = "user:{0}:visit:time";//最后一次访问动态列表时间
	
	public static String FRIEND_GROUP_CONTENT_LIST_OUT = "friend:{0}:group:contents";//好友组内分享
	
	
	
	/**
	 * 取得用户的公开推送动态列表key
	 * @param uid
	 * @return
	 */
	public static String getPublicTimelineKey(long uid){
		return MessageFormat.format(USER_PUBLIC_TIMELINE, String.valueOf(uid));
	}
	
	/**
	 * 取得用户的组内分享推送动态列表key
	 * @param uid
	 * @return
	 */
	public static String getGroupTimelineKey(long uid){
		return MessageFormat.format(USER_GROUP_TIMELINE, String.valueOf(uid));
	}
	
	/**
	 * 取得用户公开内容列表KEY
	 * @param uid
	 * @return
	 */
	public static String getUserPublicList(long uid){
		return MessageFormat.format(USER_PUBLIC, String.valueOf(uid));
	}
	
	/**
	 * 取得用户私密内容列表KEY
	 * @param uid
	 * @return
	 */
	public static String getUserPrivateList(long uid){
		return MessageFormat.format(USER_PRIVATE, String.valueOf(uid));
	}
	
	/**
	 * 取得用户组内分享内容列表Key
	 * @param uid
	 * @return
	 */
	public static String getUserGroupContentsKey(long uid){
		return MessageFormat.format(USER_GROUP_CONTENT, String.valueOf(uid));
	}
	
	/**
	 * 取得用户转发内容列表KEY
	 * @param uid
	 * @return
	 */
	public static String getUserForwardList(long uid){
		return MessageFormat.format(USER_FORWARD, String.valueOf(uid));
	}
	
	/**
	 * 取得用户喜欢内容列表KEY
	 * @param uid
	 * @return
	 */
	public static String getUserLikeList(long uid){
		return MessageFormat.format(USER_LIKE, String.valueOf(uid));
	}
	/**
	 * 编辑推荐key
	 * @param uid
	 * @return
	 */
	public static String getEditorRecommendList(){
		return EDITOR_RECOMMEND;
	}
	
	/**
	 * 编辑推荐Key TOP
	 * @return
	 */
	public static String getEditorRecommendTopKey(){
		return EDITOR_RECOMMEND_TOP;
	}
	
	/**
	 * 取得用户合并后的内容列表
	 * @param uid
	 * @return
	 */
	public static String getUserContentListKey(long uid){
		return MessageFormat.format(USER_CONTENT_LIST_OUT, String.valueOf(uid));
	}
	
	/**
	 * 取得访问别人用户合并后的内容列表
	 * @param uid
	 * @return
	 */
	public static String getOtherUserContentListKey(long uid,long visitUid){
		return MessageFormat.format(USER_CONTENT_LIST_OTHER_OUT, String.valueOf(uid),String.valueOf(visitUid));
	}
	
	/**
	 * 取得用户合并后的内容列表
	 * @param uid
	 * @return
	 */
	public static String getUserAllContentListKey(long uid){
		return MessageFormat.format(USER_ALL_CONTENT_LIST_OUT, String.valueOf(uid));
	}
	
	/**
	 * 取得访问别人用户合并后的所有内容列表
	 * @param uid
	 * @return
	 */
	public static String getOtherUserAllContentListKey(long uid,long visitUid){
		return MessageFormat.format(USER_ALL_CONTENT_LIST_OTHER_OUT, String.valueOf(uid),String.valueOf(visitUid));
	}
	
	/**
	 * 取得用户动态列表Key
	 * @param uid
	 * @return
	 */
	public static String getUserTimeLineKey (long uid){
		return MessageFormat.format(USER_TIMELINE_OUT, String.valueOf(uid));
	}
	
	/**
	 * 用户分享列表
	 * @param uid
	 * @return
	 */
	public static String getUserShareListKey(long uid){
		return MessageFormat.format(USER_SHARE, String.valueOf(uid));
	}
	
	/**
	 * 用户分享列表
	 * @param uid
	 * @return
	 */
	public static String getUserAtListKey(long uid){
		return MessageFormat.format(USER_AT, String.valueOf(uid));
	}
	
	/**
	 * 获取用户注册以后趣拍发布的内容
	 * @param uid
	 * @return
	 */
	public static String getUserQupaiList(long uid){
		return  MessageFormat.format(USER_QUPAI_LIST, String.valueOf(uid));
	}
	
	/**
	 * 获取用户已经收到的喜欢推送
	 * @param uid
	 * @return
	 */
	public static String getUserRevLikeTimeline(long uid){
		return MessageFormat.format(USER_PUBLIC_LIKE_TIMELINE, String.valueOf(uid));
	}
	
	/**
	 * 获取用户关注列表用户发表内容存储Key
	 * @param uid
	 * @return
	 */
	public static String getUserFollowsCreateTimelineKey(long uid){
		return MessageFormat.format(USER_PUBLIC_TIMELINE_CREATE, String.valueOf(uid));
	}
	
	/**
	 * 获取用户关注列表用户转发内容存储Key
	 * @param uid
	 * @return
	 */
	public static String getUserFollowsForwardTimelineKey(long uid){
		return MessageFormat.format(USER_PUBLIC_TIMELINE_FORWARD, String.valueOf(uid));
	}
	
	/**
	 * 获取用户最后一次访问动态列表时间
	 * @param uid
	 * @return
	 */
	public static String getUserLastVisitTimelineTimeKey(long uid){
		return MessageFormat.format(USER_LAST_VISIT_TIME, String.valueOf(uid));
	}
	
	
	/**
	 * 好友组内分享数据临时存放
	 * @param uid
	 * @return
	 */
	public static String getFriendGroupContents(long uid){
		return MessageFormat.format(FRIEND_GROUP_CONTENT_LIST_OUT, uid);
	}
}
