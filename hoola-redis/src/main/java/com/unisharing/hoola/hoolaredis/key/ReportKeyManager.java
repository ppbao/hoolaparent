package com.unisharing.hoola.hoolaredis.key;

public class ReportKeyManager {
	
	private static String MUSIC_KEY = "set:report:music:";// 音乐
	
	private static String BIAOQING_KEY = "set:report:biaoqing:";//表情
	
	private static String TIEZHI_KEY = "set:report:tiezhi:";//帖子
	
	private static String FILTER = "set:report:filter:";//滤镜

	public static String getMusicKey(String musicNo){
		return MUSIC_KEY + musicNo;
	}
	
	public static String getBiaoqingKey(String biaoqingNo){
		return BIAOQING_KEY + biaoqingNo;
	}
	
	public static String getTiezhiKey(String tiezhiNo){
		return TIEZHI_KEY + tiezhiNo;
	}
	
	public static String getFilterKey(String filterNo){
		return FILTER + filterNo;
	}
}
