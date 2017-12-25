package com.unisharing.hoola.hoolaclient.service.user;



import com.unisharing.hoola.hoolacommon.model.*;

import java.util.List;
import java.util.Map;

//客户端的所有行为，与之区别的是数据中心处理的是各种变化操作的影响，业务流程比如：用户注册，客户端就有ClientUserService register方法， 而数据中心就有相对的
//handle
public interface IClientUserService {
	
	void insertRegister(UserModel userModel);//Hoola注册
	void insertRegisterTh(UserModel userModel, BindModel bindModel);//第三方平台授权登录
	void updateLogin(UserModel userModel);//Hoola登录
	void updateUserInfoMobile(long uid, String mobile);//更新用户手机号

	/**
	 * Wanghh 修改时间：2013-06-20
	 * @param uid
	 * @param list
	 */
	void insertOpenFriend(long uid, List<OpenFriend> list, int openType);//第三方登录获取第三方我关注列表并插入数据库;
	void insertMobiles(long uid, List<UserMobileModel> list);//插入用户通讯录
	void updateThfriend(Map<String, Object> map);//调用存储过程匹配好友
	void insertBindModel(BindModel bindModel);//插入用户绑定第三方平台信息
	List<UserModel> queryMathchUserList(int type, long uid, long matchedTime);//type=0，获取全部匹配的用户信息，type=1 第三方平台，type=2手机通讯录

	void insertFriend(FriendModel friendModel);//添加关注
	void deleteFriend(FriendModel friendModel);//取消关注

	void followEachOther(long uid, long fid);//相互关注

	List<OpenFriend> queryOpenFriendListByUid(OpenFriend openFriend);//根据用户id和平台类型获取第3方平台好友

	//void getUserFriendList();//定时获取第3放平台的好友，并插入到数据库

	List<OpenFriend> queryMatchedOpenFriends(long uid);//获取最新匹配的数据
	void updateOpenFriend(long uid);//取出最新匹配的数据后，更新匹配成功标志为旧数据

    List<OpenFriend> queryMatchUserMobileModels(long uid);//获取通讯录最新匹配的数据

	void updateMatchUserMobileModels(long uid);

	void updateUserBind(BindModel bindModel);//更新第三方平台绑定信息

	/**
	 * @param userModel
	 * 修改用户信息
	 */
	void updateUserInfo(UserModel userModel);

	/**
	 * @param bindModel
	 * 插入用户绑定信息表
	 */
	void insertUserBind(BindModel bindModel);

	/**
	 * 根据用户ID取出用户信息
	 * @param uid
	 * @return
	 */
	UserModel getUser(long uid);


	/**
	 * 更新用户好友关系数据
	 * @param uid
	 * @param fid
	 */
	public void hoolaUpdateUserRelationshipCount(long uid, long fid);
	
	/**
	 * @param inviteModel
	 * 邀请记录
	 */
	void insertInvite(InviteModel inviteModel);
	
	
}
