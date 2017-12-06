package com.unisharing.hoola.hoolacommon.dao.user;

import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface FriendMapper {

	/**
	 * 插入第三方好友信息
	 * 
	 * @param oFriend
	 */
	public void insertOpenFriend(OpenFriend oFriend);

	int queryOpenFriendListCount();

	/**
	 * 分页获取第三方平台好友
	 * 
	 * @param
	 * @return
	 */
	List<OpenFriend> queryOpenFriendList(@Param("pageStart") int pageStart,
                                         @Param("pageSize") int pageSize);

	int queryUserMobilesListCount();

	/**
	 * 分页获取通讯录
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<OpenFriend> queryUserMobilesList(@Param("pageStart") int pageStart,
                                          @Param("pageSize") int pageSize);

}
