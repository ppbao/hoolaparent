package com.unisharing.hoola.hoolaredis.key;

import java.text.MessageFormat;

public class GroupKeyManager {
	
	private static String USER_GROUP = "set:user:{0}:groups"; //存储用户的组 存放组名
	
	private static String GROUP_USER = "set:group:{0}:{1}:users"; //组中的用户{0} 用户ID {1} 组名词
	
	/**
	 * 根据用户ID取出用户组列表KEY
	 * @param uid
	 * @return
	 */
	public static String getUserGroupsKey(long uid){
		return MessageFormat.format(USER_GROUP, String.valueOf(uid));
	}
	
	/**
	 * 根据组ID取出用户列表
	 * @param gid
	 * @return
	 */
	public static String getGroupUsersKey(String gName,long uid){
		return MessageFormat.format(GROUP_USER, String.valueOf(uid),gName);
	}

}
