package com.unisharing.hoola.hooladatacenter.service.comment;

public interface ICommentService {
	/**
	 * 处理发表评论
	 */
	public void handleCommentAdd();
	
	/**
	 * 删除评论
	 */
	public void handelCommentDelete(long id);
}
