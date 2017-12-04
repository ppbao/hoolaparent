package com.unisharing.hoola.hoolaredis.service.hot;



import com.unisharing.hoola.hoolacommon.model.BannerInfoModel;
import com.unisharing.hoola.hoolacommon.model.SubjectModel;
import com.unisharing.hoola.hoolacommon.model.TagHotModel;
import com.unisharing.hoola.hoolacommon.model.UserRecommendModel;
import com.unisharing.hoola.hoolacommon.vo.*;

import java.util.List;

public interface IRedisHotService {
	
	/**
	 * 插入新的运营条
	 * @param banner
	 */
	public void addBanner(List<BannerInfoModel> banners);
	
	/**
	 * 取首页运营条
	 * @return
	 */
	public List<BannerForm> loadBanners();
	
	/**
	 * 添加首页新的热门标签
	 */
	public void addMainHotTag(EditorTagForm tagForm);
	
	/**
	 * 添加新的热门标签
	 * @param tagForm
	 */
	public void addHotTag(EditorTagForm tagForm);
	
	
	/**
	 * 获取首页热门标签
	 * @return
	 */
	List<EditorTagForm> loadMainHotTags();
	
	/**
	 * 获取更多热门标签列表
	 * @param start
	 * @param end
	 * @return
	 */
	List<EditorTagForm> loadHotTags(int start, int end);

	/**
	 * 取得系统推荐标签
	 * @param start
	 * @param end
	 * @return
	 */
	List<String> loadTags(int start, int end);
	/**
	 * 统计系统推荐的标签数量
	 * @return
	 */
	int countTags();
	/**
	 * 添加系统标签
	 * @param tag
	 */
	void addTag(List<String> tag);

	/**
	 * 添加编辑推荐用户
	 * @param uid
	 */
	void addRecommendUser(UserRecommendModel recUser);

	/**
	 * 获取推荐的用户，排除已经关注的用户和自己
	 * @param uid
	 * @return
	 */
	public List<SimpleUserForm> loadRecommendUsers(long uid);

	/**
	 * 获取推荐达人（公众帐号列表）
	 * @param uid
	 * @return
	 */
	public List<SimpleUserForm> loadRecommendTalents(long uid);

	/**
	 * 删除编辑推荐的用户
	 * @param uid
	 */
	public void deleteRecommendUser(UserRecommendModel recUser);

	/**
	 * 插入热门用户列表
	 * @param uid
	 */
	void insertHotUser(List<Long> uid);

	/**
	 * 插入热门内容列表
	 * @param cid
	 * @param type 榜单类型 0:原来热门内容，1：总榜单；2：周榜单
	 */
	void insertHotContent(List<Long> cid, int type);

	/**
	 * 查询热门用户
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	List<TalentListForm> loadHotUser(long uid, int start, int end);

	/**
	 * 查询热门达人榜
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	List<TalentForm> loadTalentList(long uid, int start, int end);

	/**
	 * 查询热门内容
	 * @param uid
	 * @param start
	 * @param end
	 * @param type 榜单类型 0：原来的内容榜单，1：总榜单，2：周榜单
	 * @return
	 */
	List<ActionForm> loadHotContent(long uid, int start, int end, int type);

	/**
	 * 统计热门内容数量
	 * @param type 榜单类型 0：原来的内容榜单，1：总榜单，2：周榜单
	 * @return
	 */
	int countHotContent(int type);

	/**
	 * 统计热门用户数量
	 * @return
	 */
	int countHotUser();

	/**
	 * 插入搜索推荐标签//标签以“|”分开
	 * @param tagStr
	 */
	public void insertSearchTag(String tagStr);

	/**
	 * 查询搜索推荐标签
	 * @return
	 */
	public List<String> loadSearchTags();

	/**
	 * 插入发布推荐标签
	 * @param tagStr //标签以“|”分开
	 */
	public void insertPublishTag(String tagStr);

	/**
	 * 查询发布推荐标签
	 * @return
	 */
	public List<String> loadPublishTags();

	/**
	 * 查询编辑推荐的频道列表
	 * @return
	 */
	public List<TagHotModel> loadChannelTags();

	/**
	 * 插入编辑推荐的频道列表
	 * @param tags
	 */
	public void insertChannelTags(List<TagHotModel> tags);

	/**
	 * 获取推荐用户详细信息
	 * @param
	 */
	public UserRecommendModel getRecommendUser(long uid);

	/**
	 * 添加话题
	 * @param subject
	 */
	public void insertSubject(SubjectModel subject);

	/**
	 * 插入内容话题对应关系
	 * @param sid
	 * @param cid
	 */
	public void insertSubjectContent(int sid, long cid);

	/**
	 * 获取话题内容列表
	 * @param sid
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<ActionForm> loadSubjectContents(long uid, int sid, int begin, int end);

	/**
	 * 统计话题内容数
	 * @param sid
	 * @return
	 */
	public int countSubjectContents(long sid);

	/**
	 * 删除话题
	 * @param sid
	 */
	public void deleteSubject(int sid);

	/**
	 * 取得话题信息
	 * @param sid
	 * @return
	 */
	public SubjectForm getSubject(int sid);

	/**
	 * 取得话题列表
	 * @param uid
	 * @param begin
	 * @param end
	 * @return
	 */
	List<SubjectForm> loadSubjects(long uid, int begin, int end);
	
	/**
	 * 获取话题总条数
	 * @return
	 */
	int countSubject();
	
	/**
	 * 修改好友推荐和公共账号推荐的推荐理由
	 * @param userRecommendModel
	 */
	void updateRecommendReason(UserRecommendModel userRecommendModel);
	
}
