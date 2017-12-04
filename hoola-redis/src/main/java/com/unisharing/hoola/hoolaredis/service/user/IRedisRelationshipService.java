package com.unisharing.hoola.hoolaredis.service.user;


import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolacommon.vo.SystemRmdUserForm;
import com.unisharing.hoola.hoolacommon.vo.TipsShowUser;

import java.util.List;
import java.util.Set;

public interface IRedisRelationshipService {
	
	/**
	 * 插入第三方平台信息
	 * @param openFriend
	 */
	public void insertOpenFriend(long uid, OpenFriend openFriend);

	/**
	 * 根据第三方平台ID取出详细信息
	 * @param key
	 * @return
	 */
	public OpenFriend getOpenFriend(String key);

	/**
	 * 关注，跟随
	 * @param srcUid //
	 * @param targetUid	//跟随目标用户
	 */
	public void follow(long srcUid, long targetUid);

	/**
	 * 取消关注
	 * @param srcUid
	 * @param targetUid
	 */
	public void unfollow(long srcUid, long targetUid);

	/**
	 * 取得粉丝数
	 * @param uid
	 * @return
	 */
	public int countFans(long uid);

	/**
	 * 取得新粉丝数
	 * @param uid
	 * @return
	 */
	public int countNewFans(long uid);

	/**
	 * 取得用户关注列表
	 * @param uid
	 * @return
	 */
	public List<SimpleUserForm> loadFollows(long uid, int page, int pageSize);

	/**
	 * 取得关注数
	 * @param uid
	 * @return
	 */
	public int countFollows(long uid);

	/**
	 * 取得好友数
	 * @param uid
	 * @return
	 */
	public int countFriends(long uid);

	/**
	 * 取得用户粉丝列表
	 * @param uid //即将返回粉丝列表的用户ID
	 * @param page
	 * @param pageSize
	 * @param curUid//当前访问用户的用户ID
	 * @return
	 */
	public List<SimpleUserForm> loadFans(long uid, int page, int pageSize, long curUid);

	/**
	 * 取得用户粉丝ID
	 * @param uid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Set loadFansUid(long uid, int page, int pageSize);

	/**
	 * 判断是否关注
	 * @param srcUid
	 * @param targetUid
	 * @return
	 */
	public boolean isFollowed(long srcUid, long targetUid);

	/**
	 * 获取用户的好友，相互关注的
	 * @param uid
	 * @return
	 */
	public List<SimpleUserForm>  loadFriend(long uid, int page, int pageSize);

	/**
	 * 取得系统匹配用户列表
	 * @param uid
	 * @return
	 */
	public List<SystemRmdUserForm> loadMatchedFriends(long uid, int openType);

	/**
	 * 插入匹配好的好友数据
	 * @param users
	 */
	public void insertMatchedFriends(long uid, List<OpenFriend> users);

	/**
	 * 批量插入匹配好的好友数据
	 * @param users
	 */
	public void batchInsertMatchFriends(List<OpenFriend> users);

	/**
	 * 从匹配列表删除好友信息
	 * @param uid
	 */
	public void deleteMatchedFriends(long uid, long fid);

	/**
	 * 插入第三方平台好友信息
	 * @param openFriends
	 */
	public void insertNoMatchFriends(long uid, List<OpenFriend> openFriends, int openType);

	/**
	 * 查询第三方平台好友列表
	 * @param start
	 * @param end
	 * @param uid
	 * @return
	 */
	List<OpenFriend> loadNoMatchFriends(int openType, int start, int end, long uid);


	List<OpenFriend> loadAllOpenFriends(int openType, int start, int end, long uid);


	/**
	 * 从未匹配列表中删除已经成功匹配的用户
	 * @param
	 */
	//public void deleteNoMatchedFriend(long uid,OpenFriend matchedUser,int  openType);

	int countNoMatchFriends(int openType, long uid);

	/**
	 * 判断是否为好友关系
	 * @param uid
	 * @param fid
	 * @return
	 */
	boolean isFriend(long uid, long fid);

	void rename();

	/**
	 * 统计新的系统推荐第三方平台用户
	 * @param uid
	 * @return
	 */
	public int countNewRecommendFriendsNum(long uid);

	/**
	 * 统计编辑推荐的新的好友
	 * @return
	 */
	public int countNewEditorRecommendFriendNum(long time);

	/**
	 * 插入用户Id到消息队列
	 * @param uid
	 */
	public void insertUserUpdateQueue(long uid);

	/**
	 * 从消息队列获取用户ID
	 * @return
	 */
	public long getUpdateUserIdFromQueue();


	/**
	 * 添加黑名单
	 * @param uid 操作用户Id
	 * @param blackUid	被添加黑名单用户
	 */
	public  void addBlacklist(long uid, long blackUid);

	/**
	 * 删除黑名单
	 * @param uid 操作用户ID
	 * @param blackUid	被取消黑名单用户
	 */
	public void cancelBlacklist(long uid, long blackUid);

	/**
	 * 获取黑名单列表
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SimpleUserForm> loadBlacklist(long uid, int start, int end);

	/**
	 * 统计用户黑名单数量
	 * @param uid
	 * @return
	 */
	public int countBlacklist(long uid);

	/**
	 * 判断是否为黑名单
	 * @param uid 操作用户ID
	 * @param blackUid	被判断用户ID
	 * @return
	 */
	public boolean isBlackUser(long uid, long blackUid);
	
	/**
	 * 取得最新的推荐用户（系统推荐+编辑推荐）
	 * @param time
	 * @return
	 */
	TipsShowUser getNewestRecommend(long uid, long time);
}
