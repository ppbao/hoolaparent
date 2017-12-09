package com.unisharing.hoola.hooladatacenter.service.content;

public interface IContentService extends Runnable {
	
	/**
	 * 处理内容添加
	 */
	public void handleContentAdd(long cid);
	
	/**
	 * 处理喜欢内容
	 */
	public void handelContentLike();
	
	/**
	 * 处理不喜欢内容
	 */
	public void handelContentDislike();
	
	/**
	 * 处理删除内容操作
	 * @param cid
	 */
	public void handleContentDelete(long cid);
	
	/**
	 * 分享内容
	 */
	public void handleShareContent();
	
	/**
	 * 转发内容
	 */
	public void handleForwardContent();
	
	/**
	 * 取消转发内容
	 */
	public void handleCancelForwardContent();
	
	/**
	 * At内容
	 */
	public void handleAtContent();
	
	/**
	 * 处理喜欢并转发消息
	 */
	
	public void handleLikeAndForwardContent();
	
	/**
	 * 处理取消喜欢并转发消息
	 */
	public void handleCancelLikeAndForwardContent();
}
