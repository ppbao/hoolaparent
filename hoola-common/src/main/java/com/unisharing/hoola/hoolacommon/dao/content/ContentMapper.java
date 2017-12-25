package com.unisharing.hoola.hoolacommon.dao.content;

import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.submit.MContentSubmit;
import com.unisharing.hoola.hoolacommon.vo.ManagerContentForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;



public interface ContentMapper {
	
	List<ManagerContentForm> queryContentForms(MContentSubmit mContentSubmit);//查询内容列表
	List<Map<String, Object>> queryTsContentForms(MContentSubmit mContentSubmit);//查询推送内容列表
	List<TagModel> queryTagModels(long cid);//查询标签列表
	long queryContentFormsCount(MContentSubmit mContentSubmit);//统计内容总数
	long queryTsContentFormsCount(MContentSubmit mContentSubmit);//统计推送内容总数
	void insertRecommendModel(RecommendModel recommendModel);//插入编辑推存内容表
	void updateContent(ContentModel contentModel);//屏蔽或者删除内容
	void insertContentInfo(ContentModel contentModel);//插入基本信息
	List<Long> queryUserList();//查询马甲用户列表
	TagModel selectTagInfo(String tagText);//查询标签表，主要验证该标签是否存在
	void insertTagInfo(TagModel tagModel);//插入标签表，并返回数据库自增id
	void insertContentTag(ContentTagModel contentTagModel);//插入标签和内容关系表
	void deleteRecommendModel(Map<String, Object> map);//删除编辑推存内容表
	List<Long> selectHotContentList(Map<String, Object> map);//查询热门内容列表
	List<ContentModel> selectContentModels(long uploadTime);//查询内容表投放标志为0，且上传时间小于当前时间的内容
	void updateContentShow(long cid);//更新内容投放状态
	
	List<Map<String, Object>> queryReportList(MContentSubmit mContentSubmit);//查询举报列表
	/**
	 * @param mContentSubmit
	 * @return
	 */
	long queryReportListCount(MContentSubmit mContentSubmit);//统计举报总数
	
	
	/**
	 * @param jbid
	 * 举报信息驳回
	 */
	void updateReport(long jbid);
	
	/**
	 * @param createTime
	 * @return扫描定时推送信息
	 */
	List<RecommendModel> queryReList(long createTime);
	
	/**
	 * @param recommendModel
	 * 更新推送信息的投放状态
	 */
	void updateRecommendShow(RecommendModel recommendModel);
	
	/**
	 * @param recommendModel
	 * @return
	 * 查询编辑推荐内容
	 */
	RecommendModel getRecommendModel(RecommendModel recommendModel);
	
	/**
	 * @param recommendModel
	 * 更新推送时间
	 */
	void updateRecommendCreateTime(RecommendModel recommendModel);
	
	/**
	 * @param mContentSubmit
	 * @return查询Hoola君列表
	 */
	List<ManagerContentForm> queryContentFormsDqj(MContentSubmit mContentSubmit);
	
	/**
	 * @param mContentSubmit
	 * @return统计Hoola君总数
	 */
	long queryContentFormsDqjCount(MContentSubmit mContentSubmit);
	
	/**
	 * @param map
	 * 更新播放次数
	 */
	void updateContentShowTimes(Map<String, Object> map);

	/**
	 * @param map
	 * 更新假播放次数
	 */
	void updateContentFalseShowTimes(Map<String, Object> map);



	/**
	 * @param map
	 * 添加标签时更新描述信息
	 */
	void insertContentDescription(Map<String, Object> map);

	void updateContentDescription(ContentModel contentModel);

	ContentModel getContentModel(long cid);

	Map<String, Object> getContentSinaQuan(long cid);

	/**
	 * @param map
	 * 更新新浪假分享数
	 */
	void updateContentSinaNum(Map<String, Object> map);

	/**
	 * @param map
	 * 更新朋友圈假分享数
	 */
	void updateContentQuanNum(Map<String, Object> map);




	/**
	 * @param mContentSubmit
	 * @return
	 * 通过标签里的内容数进入内容列表
	 */
	List<ManagerContentForm> queryContentFormsTag(MContentSubmit mContentSubmit);

	long queryContentFormsTagCount(MContentSubmit mContentSubmit);

    /**
     * 从话题列表进入内容列表
     * @param mContentSubmit
     * @return
     */
    List<ManagerContentForm> queryContentFormsSubject(MContentSubmit mContentSubmit);

	long queryContentFormsSubjectCount(MContentSubmit mContentSubmit);

	/**
	 * @param cid
	 * @return
	 * 获取内容排行
	 */
	Map<String, Object> getSetContentList(@Param("cid") long cid, @Param("type") int type);

	/**
	 * @param setContentModel
	 * 设置排行位置
	 */
	void insertSetContent(SetContentModel setContentModel);

	/**
	 * @param setContentModel
	 * 位置更新
	 */
	void updateSetContent(SetContentModel setContentModel);

	/**
	 * @param cid
	 * @return
	 * 获取推送热门对象
	 */
	SetContentModel getSetContentModel(@Param("cid") long cid, @Param("type") int type);

	/**
	 * @return
	 * 获取推送热门的列表
	 */
	List<SetContentModel> querySetContentModels();

	/**
	 * @return
	 * 获取推送热门的列表
	 */
	List<Map<String, Object>> queryTsHotContentList(MContentSubmit mContentSubmit);

	/**
	 * @return
	 * 获取推送热门的总数
	 */
	long queryTsHotContentListCount(MContentSubmit mContentSubmit);

	/**
	 * @param cid
	 * 取消热门推送
	 */
	void deleteTsHotContent(@Param("cid") long cid, @Param("type") int type);

	/**
	 * @return
	 * 获取新版本推送热门的列表
	 */
	List<SetContentModel> queryNewSetContentModels();


	/**
	 * 新版本总榜排行榜
	 * @return
	 */
	List<Long> selectNewAllHotContentList();


	/**
	 * 新版本本周排行榜
	 * @param time
	 * @return
	 */
	List<Long> selectNewWeekHotContentList(long time);

	/**
	 * 查询公共账号
	 * @return
	 */
	List<Map<String, Object>> queryPublishUserList();


	ForwardContentModel getForwardContentModel(@Param("uid") long uid, @Param("cid") long cid);


	void insertForwardContentModel(ForwardContentModel forwardContentModel);//插入转发表
	void updateContentForwardNum(@Param("cid") long cid, @Param("num") int num);//更新转发数
	
	/**
	 * @param map
	 * @return
	 */
	List<ResultHot> selectHotContentListNew(Map<String, Object> map);
	
	
	
	List<Long> totalHotContent();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
