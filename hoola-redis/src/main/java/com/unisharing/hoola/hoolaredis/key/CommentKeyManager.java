package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class CommentKeyManager {
	
	private static String COMMENT_ID_GLOBAL_KEY = "kv:comment:global:id"; //用户ID生成key String
	
	private static String COMMENT_INFO_KEY = "hm:comment:{0}:data";//评论内容信息
	
	private static String CONTENT_COMMENT_KEY = "ss:content:{0}:comment";	//内容的评论
	
	private static String SUB_COMMENT_KEY = "ss:parent:{0}:comment";//子评论
	
	private static String CONTENT_COMMENT_USER = "ss:content:{0}:comment:user";//内容评论用户
	
	private static String USER_RECEIVE_COMMENTS = "ss:user:{0}:comments";//用户收到的评论
	
	private static String USER_RECEIVE_NEW_COMMENTS = "ss:user:{0}:comments:new";//用户收到的新评论
	
	/**
	 * 取得全局的评论ID
	 * @return
	 */
	public static String getCommentIdKey(){
		return COMMENT_ID_GLOBAL_KEY;
	}
	
	/**
	 * 取得评论内容空KEY
	 * @param id
	 * @return
	 */
	public static String getCommentInfoKey(long id){
		return MessageFormat.format(COMMENT_INFO_KEY, String.valueOf(id));
	}
	
	/**
	 * 取得内容主评论
	 * @param cid
	 * @return
	 */
	public static String getContentCommentKey(long cid){
		return MessageFormat.format(CONTENT_COMMENT_KEY, String.valueOf(cid));
	}
	
	/**
	 * 取得子评论KEY
	 * @param id
	 * @return
	 */
	public static String getSubCommentKey(long id){
		return MessageFormat.format(SUB_COMMENT_KEY, String.valueOf(id));
	}
	
	
	public static String getContentCommentUsersKey(long cid){
		return MessageFormat.format(CONTENT_COMMENT_USER, String.valueOf(cid));
	}
	
	/**
	 * 用户得到的评论消息Key
	 * @param uid
	 * @return
	 */
	public static String getUserReceiveCommentsKey(long uid){
		return MessageFormat.format(USER_RECEIVE_COMMENTS, String.valueOf(uid));
	}
	
	/**
	 * 用户得到的新评论消息Key
	 * @param uid
	 * @return
	 */
	public static String getUserReceiveNewCommentsKey(long uid){
		return MessageFormat.format(USER_RECEIVE_NEW_COMMENTS, String.valueOf(uid));
	}
}
