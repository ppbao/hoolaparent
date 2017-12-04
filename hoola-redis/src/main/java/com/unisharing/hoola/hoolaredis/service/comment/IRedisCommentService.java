package com.unisharing.hoola.hoolaredis.service.comment;



import com.unisharing.hoola.hoolacommon.model.CommentModel;
import com.unisharing.hoola.hoolacommon.vo.CommentForm;

import java.util.List;

public interface IRedisCommentService {
	
	/**
	 * 取得内容评论列表
	 * @param cid
	 * @return
	 */
	public List<CommentForm> loadComments(long cid, int page, int pageSize);
	
	/**
	 * 取得评论的子评论
	 * @param id
	 * @return
	 */
	//public List<CommentForm> loadSubComments(long id);
	
	/**
	 * 发表评论
	 * @param comment
	 */
	public CommentModel addComment(CommentModel comment);
	
	/**
	 * 取得评论信息
	 * @param id
	 * @return
	 */
	public CommentModel getComment(long id);
	
	/**
	 * 删除评论
	 * @param comment
	 * @return 删除的条数
	 */
	public int deleteComment(CommentModel comment);
	
	/**
	 * 统计内容的评论数（主评论）
	 * @param cid
	 * @return
	 */
	public int countComments(long cid);
	
	/**
	 * 添加用户收到的评论信息
	 * @param commentId
	 */
	public void insertUserCommentMessage(long commentId);
	
	/**
	 * 生成评论ID
	 * @return
	 */
	public long getCommentId();
}
