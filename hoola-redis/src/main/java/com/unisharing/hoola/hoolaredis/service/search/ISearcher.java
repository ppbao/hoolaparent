package com.unisharing.hoola.hoolaredis.service.search;



import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;

import java.util.List;
import java.util.Set;

public interface ISearcher {
	
	/**
	 * 根据关键词搜索
	 * @param key
	 */
	Set<String> searchSuggest(String key);
	
	/**
	 * 通过tag获取最新内容列表
	 * @param uid
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<ActionForm> searchTagContentsOrderByUploadTime(long uid, String key, int start, int end);

	/**
	 * 通过Tag获取最热内容列表
	 * @param uid
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<ActionForm> searchTagContentsOrderByLikeNum(long uid, String key, int start, int end);

	int countContent(String key);

	/**
	 * 搜索用户
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<SimpleUserForm> searchUser(String key, int start, int end);
	
	/**
	 * 获取用户搜索建议
	 * @param key
	 * @return
	 */
	Set<String> searchUserSuggest(String key);

}
