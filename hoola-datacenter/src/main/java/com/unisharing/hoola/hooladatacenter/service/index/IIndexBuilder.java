package com.unisharing.hoola.hooladatacenter.service.index;

import java.util.Set;

public interface IIndexBuilder {
	
 
	/**
	 * 推荐词索引
	 * @param :id 内容Id 可以为null
	 * @param :content 索引内容
	 * 
	 */
	public void buildSuggestIndex(Set<String> tag);
	
	/**
	 * 创建标签内容对应关系
	 * @param cid
	 * @param tag
	 */
	public void buildTagIndex(long cid, Set<String> tag);

	/**
	 * 创建
	 * @param uid
	 */
	public void buildUserIndex(String nickName, long uid);
	
	public void buildUserSuggestIndex(String nickName);

}
