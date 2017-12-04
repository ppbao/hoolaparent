package com.unisharing.hoola.hoolaredis.service.timeline;


import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.submit.Pager;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;

import java.util.List;
import java.util.Set;

public interface IRedisTimelineService {
	
	/**
	 * 插入用户内容列表
	 * @param uid	//用户ID
	 * @param cid	//内容ID
	 * @param isPrivate	//是否公开
	 */
	public boolean insertUserContentList(long uid, long cid, int isPrivate);

	/**
	 * 插入用户喜欢列表
	 * @param uid
	 * @param cid
	 */
	public boolean insertUserLikeList(long uid, long cid);

	/**
	 * 删除用户喜欢列表的数据
	 * @param uid
	 * @param cid
	 */
	public boolean deleteUserLikeList(long uid, long cid);

	/**
	 * 插入用户AT列表
	 * @param uid
	 * @param cid
	 */
	public boolean insertUserAtList(long uid, long cid);

	/**
	 * 插入用户转发列表
	 * @param uid
	 * @param cid
	 */
	public boolean insertUserForwardList(long uid, long cid);

	/**
	 * 插入动态列表（包括关注用户创建和喜欢的推送）
	 * @param uid
	 * @param member

	 */
	public boolean insertUserTimeLine(long uid, ActionModel member);

	/**
	 * 插入用户关注内容列表（关注用户发表的内容）
	 * @param uid
	 * @param member
	 * @return
	 */
	//public boolean insertUserCreateTimeline(long uid,ActionModel member);

	/**
	 * 插入用户转发内容列表（关注用户转发的内容）
	 * @param uid
	 * @param member
	 * @return
	 */
	//public boolean insertUserForwardTimeline(long uid,ActionModel member);


	/**
	 * 插入用户分享列表
	 * @param uid
	 * @param cid
	 */
	public boolean insertUserShareList(long uid, long cid);

	/**
	 * 插入动态列表，组内分享的。
	 * @param uid
	 * @param action
	 * @return
	 */
	public boolean insertUserGroupTimeLine(long uid, ActionModel action);

	/**
	 * 删除动态列表（包括关注用户创建和喜欢的推送）
	 * @param uid
	 */
	public boolean deleteUserTimeLine(long uid, ActionModel member);

	/**
	 * 取得动态列表
	 * @param uid
	 * @param pager
	 * @param type 列表类型 all forward create
	 * @return
	 */
	public List<ActionForm> loadUserTimeline(long uid, Pager pager, String type);

	/**
	 * 取得用户最新的动态内容，最后一次访问后的新数据
	 * @param uid
	 * @return
	 */
	public List<ActionForm> loadUserNewestTime(long uid);
	/**
	 * 刷新内容操作人员列表
	 * @param cid
	 */
	public void refreshOptUsersCache(long cid);

	/**
	 * 读取用户动态条数
	 * @param uid
	 * @return
	 */
	public int countUserTimeline(long uid);

	public List<SimpleUserForm> loadOptUsers(long cid, int start, int end);

	public List<SimpleUserForm> loadOptUsersFromCache(long cid, int start, int end);

	/**
	 * 取出描述里面的AT用户信息
	 * @param nickName
	 * @return
	 */
	public List<SimpleUserForm> loadAtUsers(Set<String> nickName);

	/**
	 * 删除用户内容列表
	 */
	public void deleteUserContentList(ActionModel action);

	/**
	 * 查询动态首页单元格信息
	 * @param uid
	 * @param time 动态时间，如果为0这为内容上传时间
	 * @return
	 */
	public ActionForm getActionForm(ActionModel actionModel, long uid, long time, boolean isFromCache);

	/**
	 * 统计最新的动态列表数据
	 * @param uid
	 * @return
	 */
	public int countNewTimeline(long uid);

	/**
	 * 插入趣拍置顶内容
	 * @param action
	 * @return
	 */
	public void insertTimelineTopAction(ActionModel action);

	/**
	 * 插入用户接受的喜欢推送内容
	 * @param cid
	 */
	public void insertUserRevLikeTime(long uid, long cid, long actionUserId);

	/**
	 * 删除用户接受的喜欢推送内容
	 * @param cid
	 */
	public void deleteUserRevLikeTime(long uid, long cid, long actionUserId);

	/**
	 * 判断是否已经接受过推送
	 * @param uid
	 * @param cid
	 * @return
	 */
	public boolean isRevLikeTime(long uid, long cid);


	/**
	 * 计算用户私密内容条数
	 * @param uid
	 * @return
	 */
	public int countUserPrivateContents(long uid);

	/**
	 * 给新用户推送趣拍的内容
	 * @param uid 用户ID
	 * @param count 条数
	 */
	public void pushOneContentsToNewUser(long uid, int count);
	
}
