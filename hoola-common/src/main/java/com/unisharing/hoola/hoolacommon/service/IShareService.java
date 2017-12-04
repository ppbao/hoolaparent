package com.unisharing.hoola.hoolacommon.service;



import com.unisharing.hoola.hoolacommon.model.OpenFriend;

import java.util.List;

public interface IShareService {

	/**
	 * 分享到新浪
	 */
	public boolean shareToSina(String images, String content, String accessToken);

	/**
	 * 抓取新浪好友
	 */
	public List<OpenFriend> loadSinaFollows(String openUserId, String accessToken);

	/**
	 * 抓取qq好友
	 */
	public List<OpenFriend> loadQQFollows(String openUserId, String token);

	/**
	 * 分享信息到腾讯微博
	 */

	public void shareToQQWeibo(String imagePath, String content, String token, String openUserId);


	public void shareToQZone(String title, String content, String url, String playUrl, String imageUrl, String token, String openUserId);
	
	/**
	 * 关注新浪趣拍APP
	 * @param accessToken
	 */
	public void sinaFollow(String accessToken);

}
