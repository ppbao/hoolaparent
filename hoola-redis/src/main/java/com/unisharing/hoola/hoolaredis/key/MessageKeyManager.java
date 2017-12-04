package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class MessageKeyManager {
	
	private static String MESSAGE_COMMENT = "ss:user:{0}:comment:message";//用户评论列表
	
	private static String AT_MESSAGE_ID_GLOBAL_KEY = "kv:at:global:id";//at消息ID全局KEY
	
	private static String AT_MESSAGE_INFO_KEY = "hm:at:{0}:data";
	
	private static String USER_MESSAGE_AT_NEW = "ss:user:{0}:at:message:new";//用户受到的AT消息列表（新的）
	
	private static String USER_MESSAGE_AT = "ss:user:{0}:at:message";//用户收到的AT消息列表
	
	private static String MESSAGE_ID_GLOBAL_KEY = "kv:message:global:id";//私信ID全局KEY
	
	private static String MESSAGE_INFO_KEY = "hm:message:{0}:data";//私信内容KEY
	
	private static String MESSAGE_DIALOG = "ss:message:{0}:{1}:dialog";//用户{0} 和 {1} 的对话内容存储Key
	
	private static String DIALOG_USERS = "ss:user:{0}:dialog:users";//用户{0} 的对话用户列表；score存放最后一次对话时间
	
	private static String USER_NEW_MESSAGE_COUNT = "hm:user:{0}:message:newnum";//用户{0}的每个对话用户的新消息数；
								//说明：每个key对应一个有新消息的用户ID，value对应新消息数
	
	private static String MESSAGE_RIGHT_USERS = "message:right:user";//私信特权用户
	
	
	/**
	 * 用户收到的评论KEY
	 * @param uid
	 * @return
	 */
	public static String getCommentMessageKey(long uid){
		return MessageFormat.format(MESSAGE_COMMENT, String.valueOf(uid));
	}
	
	/**
	 * 取得At消息自增IDkey
	 * @return
	 */
	public static String getAtMessageIdKey(){
		return AT_MESSAGE_ID_GLOBAL_KEY;
	}
	
	/**
	 * 取得At信息KEY
	 * @param id
	 * @return
	 */
	public static String getAtMessageInfoKey(long id){
		return MessageFormat.format(AT_MESSAGE_INFO_KEY, String.valueOf(id));
	}
	
	/**
	 * 取得At信息KEY
	 * @param uid
	 * @return
	 */
	public static String getUserAtMessageKey(long uid){
		return MessageFormat.format(USER_MESSAGE_AT, String.valueOf(uid));
	}
	
	/**
	 * 取得新的At信息KEY
	 * @param uid
	 * @return
	 */
	public static String getUserNewAtMessageKey(long uid){
		return MessageFormat.format(USER_MESSAGE_AT_NEW, String.valueOf(uid));
	}
	
	/**
	 * 取得私信全局IDkey
	 * @return
	 */
	public static String getMessageIdKey(){
		return MESSAGE_ID_GLOBAL_KEY;
	}
	
	/**
	 * 取得私信内容KEY
	 * @param id
	 * @return
	 */
	public static String getMessageInfoKey(long id){
		return MessageFormat.format(MESSAGE_INFO_KEY, String.valueOf(id));
	}
	
	
	/**
	 * 取得用户收到私信列表KEY
	 * @param id
	 * @return
	 */
	public static String getDialogKey(long uid,long dialogUid){
		return MessageFormat.format(MESSAGE_DIALOG, String.valueOf(uid),String.valueOf(dialogUid));
	}

	/**
	 * 取得用户的对话列表
	 * @param uid
	 * @return
	 */
	public static String getDialogUsers(long uid){
		return MessageFormat.format(DIALOG_USERS, String.valueOf(uid));
	}
	
	/**
	 * 取得新消息数
	 * @param uid
	 * @return
	 */
	public static String getNewMessageKey(long uid){
		return MessageFormat.format(USER_NEW_MESSAGE_COUNT, String.valueOf(uid));
	}
	/**
	 * 私信特权用户列表
	 * @return
	 */
	public static String getMessageRightUser(){
		return MESSAGE_RIGHT_USERS;
	}
}
