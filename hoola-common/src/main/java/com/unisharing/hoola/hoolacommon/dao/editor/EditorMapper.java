package com.unisharing.hoola.hoolacommon.dao.editor;

import com.unisharing.hoola.hoolacommon.model.*;

import java.util.List;
import java.util.Map;


public interface EditorMapper {
	int insertActiveInfo(ActiveInfoModel activeInfoModel);//插入活动信息表
	void insertActiveContent(ActiveContentModel activeContentModel);//插入活动内容对照表
	void insertEditorTag(EditorTagModel editorTagModel);//插入编辑推存标签
	void insertEditorTalent(EditorTalentModel editorTalentModel);//插入编辑推存达人榜
	void insertBannerInfo(BannerInfoModel bannerInfoModel);//插入广告位信息表（运营条共用这张表）
	void updateActiveContent(ActiveContentModel activeContentModel);//更新活动排序
	void deleteEditorTag(int id);//删除编辑推存标签
	void deleteEditorTalent(int id);//删除编辑推存达人榜
	List<BannerInfoModel> queryBannerInfoModels();//显示运营条内容
	List<EditorTagModel> queryEditorTagModels();//显示编辑推存的标签
	List<Map<String, Object>> queryTagInfo();//查询热门标签
	//Map 信息如下：tid:标签id,tag_text:标签名称,clcountSum:该标签被喜欢的总数，countNum：在喜欢内容中该标签被使用的总数，avgNum:喜欢数和使用数的比值
	List<EditorTalentModel> queryEditorTalentModels();//查询达人榜明细表
	
	void insertEditorTalentInfo(EditorTalentInfoModel editorTalentInfoModel);//插入达人榜信息表
	List<EditorTalentInfoModel> queryEditorTalentInfoModels();//查询达人榜信息表
	
	

}
