package com.unisharing.hoola.hoolaclient.dao;

import com.unisharing.hoola.hoolacommon.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component

public interface ClientUserMapper {
	
	void insertUserInfo(UserModel userModel);//插入用户基本信息表
	void insertUserToken(UserModel userModel);//插入用户token表
	void insertUserBind(BindModel bindModel);//第三方用户插入用户绑定信息表
	void updateUserInfo(UserModel userModel);//更新用户基本信息表
	void updateUserToken(UserModel userModel);//更新用户token表
	void updateUserBind(BindModel bindModel);//更新第三方用户绑定信息表
	UserModel selectUserInfo(UserModel userModel);//获取用户信息
	void inserOpenFriend(OpenFriend openFriend);//插入第三方平台好友
	void insertMobiles(UserMobileModel userMobileModel);//插入通讯录
	OpenFriend selectOpenFriend(OpenFriend openFriend);//查询第三方平台好友是不是已经存在账号关系表
	void updateThfriend(Map<String, Object> map);//调用存储过程匹配好友 map 中方的参数类型 key="p_exectype" value=1：程序调用；2：作业调用
	                                            //key="p_functype" 第三方平台类型  key="p_uid"  用户id
	List<UserModel> queryThfriendList(Map<String, Object> hashMap);//匹配成功的第三方用户信息
	List<UserModel> queryMobileList(Map<String, Object> hashMap);//匹配成功的通讯录用户信息
	List<UserModel> queryAllList(Map<String, Object> hashMap);//匹配成功所有用户信息
	UserMobileModel getMobiles(@Param("uid") long uid, @Param("mobile") String mobile);

	void insertFriend(FriendModel friendModel);//关注
	void deleteFriend(FriendModel friendModel);//取消关注
	
	FriendModel getFriendModel(FriendModel friendModel);
	void updateIsFriend(FriendModel friendModel);//相互关注的情况下需要更新关注方的is_friend的值
	
	List<BindModel> queryBindModelList();//获取绑定表里的用户信息
	List<OpenFriend> queryOpenFriendlByUid(OpenFriend openFriend);//根据用户id和平台类型获取第三方平台好友的数据
	
	List<OpenFriend> queryMatchedOpenFriends(long uid);//获取第三方平台匹配的数据（所有，最新和老数据通过 isMatched进行区分）
	void updateOpenFriend(long uid);//取出最新匹配的数据后，更新匹配成功标志为旧数据
	
	List<OpenFriend> queryMatchUserMobileModels(long uid);//获取通讯录最新匹配的数据
	
	void updateMatchUserMobileModels(long uid);
	
	/**
	 * @param map
	 * 更新关注数
	 */
	void updateUserGzs(Map<String, Object> map);
	
	/**
	 * @param map
	 * 更新真粉丝数
	 */
	void updateUserZfss(Map<String, Object> map);
	
	/**
	 * @param map
	 * 更新好友数和关注数
	 */
	void updateUserHysAndGzs(Map<String, Object> map);
	
	/**
	 * @param map
	 * 更新对方的好友数和真粉丝数
	 */
	void updateUserHysAndZfss(Map<String, Object> map);
	
	/**
	 * 统一更新用户的好友数，关注数，粉丝数
	 * @param uid
	 */
	void updateUserRelationshipCount1(long uid);
	
	/**
	 * @param inviteModel
	 * 添加邀请好友日志
	 */
	void insertInvite(InviteModel inviteModel);
	

}
