package com.unisharing.hoola.hoolaclient.dao;

import com.unisharing.hoola.hoolacommon.model.*;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface ClientContentMapper {
	void insertContentInfo(ContentModel contentModel);//插入基本信息
	TagModel selectTagInfo(String tagText);//查询标签表
	void insertTagInfo(TagModel tagModel);//插入标签表，并返回数据库自增id
	void insertContentTag(ContentTagModel contentTagModel);//插入标签和内容关系表
	void updateContent(long cid);//用户删除
	
	/**
	 * 查看该cid是否在推荐列表
	 * @param cid
	 * @return
	 */
	ContentModel getContentRecommend(long cid); 
	
	/**
	 * 删除编辑推荐内容
	 * @param cid
	 */
	void deleteRecommendModel(long cid);
	
	
	
	
	/**
	 * @param contentModel
	 * 更新内容的md5值
	 */
	void updateContentMd5(ContentModel contentModel);
	
	
	/**
	 * @param map
	 * 更新播放次数
	 */
	void updateContentShowTimes(Map<String, Object> map);
	
	ContentModel getContentModel(long cid);
	
	/**
	 * @param map
	 * 更新新浪分享次数
	 */
	void updateContentSinaShareNum(Map<String, Object> map);
	
	/**
	 * @param map
	 * 更新朋友圈分享次数
	 */
	void updateContentQuanShareNum(Map<String, Object> map);
	
	/**
	 * @param logShareModel
	 * 添加分享日志
	 */
	void insertShareContent(LogShareModel logShareModel);
	
	/**
	 * @param logShowModel
	 * 添加播放日志
	 */
	void insertShowContent(LogShowModel logShowModel);
	
	/**
	 * @param subjectContentModel
	 * 添加活动内容对照表
	 */
	void insertSubjectContent(SubjectContentModel subjectContentModel);
}
