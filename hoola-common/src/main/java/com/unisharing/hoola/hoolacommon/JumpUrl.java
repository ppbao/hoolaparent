package com.unisharing.hoola.hoolacommon;

public enum JumpUrl {
	MESSAGE_DIALOG("qupai://message/dialog"),//私信对话列表
	MESSAGE_AT("qupai://message/at"),//私信AT消息
	MESSAGE_COMMENT("qupai://message/comments"),//消息评论列表
	INDEX("qupai://index"),//首页
	HOT_CONTENT("qupai://hot_content"),//热门内容榜
	HOT_TALENT("qupai://hot_talent"),//热门达人榜
	HOT_CHANNEL("qupai://channel"),//热门频道
	USER_INDEX("qupai://user?uid="),//用户个人中心
	CONTENT_DETAIL("qupai://content/detail?cid="),//单条内容
	SEARCH_KEY("qupai://search_key?key="),//搜索结果页面
	WAP_PAGE("qupai://wap?url="),//wap页面
	TAG("qupai://tag?tag="),//标签内容列表页面
	TOPIC_INDEX("qupai://topic_index"),//话题列表页面
	TOPIC_LIST("qupai://topci_list?tid="),//话题内容列表页
	INVITE("qupai://invite"); // 邀请好友
	
	String url;
	private JumpUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}

}
