package com.unisharing.hoola.hoolaredis.key;

public class SearchKeyManager {

	private static String CONTENT_INDEX_PREFIX = "tag:";
	private static String AUTOCOMPLETE_SUFFIX_SYMBOLE = "*";
	private static String AUTOCOMPLETE_KEY = "suggest";
	private static String RESULT_KEY = "result:";
	
	private static String USER_INDEX = "user:";
	private static String USER_AUTOCOMPLETE_KEY = "user:suggest";
	private static String USER_RESULT_KEY = "user:result:";
	
	/**
	 * 取得标签内容对应关系
	 * @param tag
	 * @return
	 */
	public static String getContentIndexKey(String tag){
		return CONTENT_INDEX_PREFIX + tag;
	}
	
	/**
	 * 取得内容搜索建议key
	 * @return
	 */
	public static String getSuggestKey(){
		return AUTOCOMPLETE_KEY;
	}
	
	/**
	 * 取得结束 *
	 * @return
	 */
	public static String getSymbole(){
		return AUTOCOMPLETE_SUFFIX_SYMBOLE;
	}
	
	/**
	 * 取得内容搜索结果Key
	 * @param key
	 * @return
	 */
	public static String getResultKey(String key){
		return RESULT_KEY + key;
	}
	
	
	/**
	 * 取得昵称用户ID对应关系key
	 * @param key
	 * @return
	 */
	public static String getUserNicknameKey(String key){
		return USER_INDEX + key;
	}
	
	/**
	 * 取得用户建议
	 * @return
	 */
	public static String getUserSuggestKey(){
		return USER_AUTOCOMPLETE_KEY;
	}
	
	/**
	 * 用户搜索结果
	 * @param key
	 * @return
	 */
	public static String getUserResultKey(String key){
		return USER_RESULT_KEY + key;
	}
	
	
}
