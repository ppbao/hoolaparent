package com.unisharing.hoola.hoolaredis.service.content;


import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.submit.Pager;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.ContentForm;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.List;
import java.util.Set;

public interface IRedisContentService {
	/**
	 * 删除我创作的内容
	 * @param uid
	 * @param cid
	 */
	public void deleteContentFromList(long uid, long cid);
	/**
	 * 添加内容
	 * @param content
	 */
	public ContentModel insertContent(ContentModel content);

	/**
	 * 更新内容MD5值
	 * @param cid
	 * @param md5
	 */
	public void updateContentMD5(long cid, String md5);

	/**
	 * 通过Id取得内容
	 * @param cid
	 * @return
	 */
	public ContentModel getContent(long cid);

	/**
	 * 根据喜欢数取得内容列表
	 * @return
	 */
	public List<ContentModel> loadContentsByLikeNum(int limit);

	/**
	 * 更新内容状态
	 * @param cid
	 * @param status
	 */
	public void updateContentStatus(long cid, int status);

	/**
	 * 取得用户个人中心的数据
	 * @param uid//当前列表所属用户
	 * @param visitUid //当前登陆用户ID
	 * @return
	 */
	public List<ActionForm> loadUserContents(long uid, long visitUid, Pager pager);

	/**
	 * 取得用户喜欢的内容列表
	 * @param uid
	 * @param visitUid
	 * @param pager
	 * @return
	 */
	public List<ActionForm> loadUserLikeContents(long uid, long visitUid, Pager pager);

	/**
	 * 取得用户转发的内容列表
	 * @param uid
	 * @param visitUid
	 * @param pager
	 * @return
	 */
	public List<ActionForm> loadUserForwardContents(long uid, long visitUid, Pager pager);

	/**
	 * 获取用户24小时之内发布的内容
	 * @param uid
	 * @return 返回的存储格式为Redis的存储格式主要包括value + score
	 */
	@SuppressWarnings("rawtypes")
	public Set<TypedTuple> loadUser24HoursContents(long uid);

	/**
	 * 返回用户个人中心我的列表记录数
	 * @param uid
	 * @return
	 */
	int countUserContents(long uid);

	/**
	 * 取得用户个人中心所有的数据
	 * @param uid//当前列表所属用户
	 * @param visitUid //当前登陆用户ID
	 * @return
	 */
	public List<ActionForm> loadUserAllContents(long uid, long visitUid, Pager pager);

	/**
	 * 统计用户个人中心我的所有列表记录数
	 * @param uid
	 * @param  uid
	 * @return
	 */
	int countUserAllContents(long uid);

	/**
	 * 获取内容喜欢用户列表
	 * @param cid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<FriendForm> loadLikeUsers(long cid, int start, int end);


	/**
	 * 统计喜欢数
	 * @param cid
	 * @return
	 */
	public int countLikeUsers(long cid);

	/**
	 * 获取内容转发用户列表
	 * @param cid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<FriendForm> loadForwardUsers(long cid, int start, int end);

	/**
	 * 统计喜欢数
	 * @param cid
	 * @return
	 */
	public int countForwardUsers(long cid);

	/**
	 * 更新评论数
	 * @param cid
	 * @param count
	 */
	public void updateCommentNum(long cid, int count);

	/**
	 * 更新喜欢数
	 * @param cid
	 * @param count
	 */
	public void updateLikeNum(long cid, int count);


	/**
	 * 更新转发数
	 * @param cid
	 * @param count
	 */
	public void updateForwardNum(long cid, int count);

	/**
	 * 更新新浪分享数
	 * @param cid
	 * @param count
	 */
	public int updateSinaShareNum(long cid, int count);

	/**
	 * 更新朋友圈分享数
	 * @param cid
	 * @param count
	 */
	public int updateFriendsShareNum(long cid, int count);


	/**
	 * 更新播放次数
	 * @param cid
     */
	public void updatePlayNum(long cid);
	/**
	 * 判断用户是否已经喜欢过该内容
	 * @param cid
	 * @param uid
	 * @return
	 */
	public boolean isLiked(long cid, long uid);

	/**
	 * 判断用户是否已经转发过该内容
	 * @param cid
	 * @param uid
	 * @return
	 */
	public boolean isForwarded(long cid, long uid);

	/**
	 * 转发内容
	 * @param cid
	 * @param uid
	 */
	public void forwardContent(long cid, long uid);

	/**
	 * 取消转发
	 * @param cid
	 * @param uid
	 */
	public void cancelForwardContent(long cid, long uid);

	/**
	 * 喜欢内容
	 * @param cid
	 * @param uid
	 * @return
	 */
	public void likeContent(long cid, long uid);

	/**
	 * 创建标签和内容的对应关系
	 * @param cid
	 * @param tags
	 */
	public void buildTagIndex(long cid, Set<String> tags);

	/**
	 * 取消喜欢内容
	 * @param cid
	 * @param uid
	 * @return
	 */
	public void disLikeContent(long cid, long uid);

	/**
	 * 内容评论用户列表
	 * @param cid
	 * @param uid
	 */
	public void addCommentUser(long cid, long uid);

	/**
	 * 获取最新内容
	 */
	public List<ActionForm> listContents(long uid, int start, int end);

	/**
	 * 插入最新列表
	 * @param cid
	 */
	public void insertContentList(long cid);
	/**
	 * 获取最新的内容数
	 * @return
	 */
	public int countContentList();

	/**
	 * 插入发现接口
	 * @param cid
	 */
	public void insertContentFindList(long cid, int recommend);

	/**
	 * 删除发现内容列表
	 * @param cid
	 */
	public void deleteContentFindList(long cid);

	/**
	 * 获取发现内容
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ActionForm> loadFindContentList(long uid, int start, int end);

	/**
	 * 获取发现数
	 * @return
	 */
	public int countFindContent();

	/**
	 * 获取所有内容ID
	 */
	public List<Long> loadAllContentId();

	/**
	 * 插入用户分享内容列表
	 * @param uid
	 * @param cid
	 */
	public void insertShareContentList(long uid, long cid);

	/**
	 * 获取内容ID
	 * @return
	 */
	long getContentId();

	/**
	 * 加入首页编辑推荐列表
	 * @param cid
	 */
	public void insertEditorRecommend(long cid);

	/**
	 * 删除首页编辑推荐
	 * @param cid
	 */
	public void deleteEditorRecommend(long cid);

	/**
	 * 根据Key获取内容ID
	 * @param key
	 * @return
	 */
	public long getCid(String key);

	/**
	 * 获取列表每个单元格完整数据
	 * @param cid
	 * @param uid
	 * @return
	 */
	public ContentForm getContent(long cid, long uid, boolean isFromCache);

	/**
	 * 删除标签内容对应关系
	 * @param tag
	 * @param cid
	 */
	public void delTagIndex(String tag, long cid);

	public long getCidFromQueue();

	public void insertCidIntoQueue(long cid);

	/**
	 * 更新内容描述
	 * @param cid
	 * @param description
	 */
	public void updateDescription(long cid, String description);

	/**
	 * 获取内容所有的喜欢数据
	 * @param cid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Set loadContentLikes(long cid);

	/**
	 * 获取内容所有转发数据
	 * @param cid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Set loadContentForwards(long cid);

	/**
	 * 喜欢并转发内容
	 * @param uid
	 * @param cid
	 */
	public void likeAndForwardContent(long uid, long cid);

	/**
	 * 取消喜欢并转发内容
	 * @param uid
	 * @param cid
	 */
	public void cancelLikeAndForwardContent(long uid, long cid);

	/**
	 * 统计用户公开发表的内容
	 * @param uid
	 * @return
	 */
	public int countUserPublicContents(long uid);

	/**
	 * 统计用户私密发布的内容
	 * @param uid
	 * @return
	 */
	public int countUserPrivateContents(long uid);

	/**
	 * 获取用户喜欢内容数；
	 * @param uid
	 * @return
	 */
	public int countUserLikeContents(long uid);
	/**
	 * 获取用户转发内容数
	 * @param uid
	 * @return
	 */
	public int countUserForwardContents(long uid);

	public int saveTmpVideo(String key, String data);
	
	public String getTmpVideo(String key);
}
