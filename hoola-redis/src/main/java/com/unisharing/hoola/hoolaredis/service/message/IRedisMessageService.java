package com.unisharing.hoola.hoolaredis.service.message;


import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.MessageModel;
import com.unisharing.hoola.hoolacommon.model.RedisMessageModel;
import com.unisharing.hoola.hoolacommon.vo.AtMessageForm;
import com.unisharing.hoola.hoolacommon.vo.CommentMessageForm;
import com.unisharing.hoola.hoolacommon.vo.DialogForm;
import com.unisharing.hoola.hoolacommon.vo.MessageForm;

import java.util.List;

public interface IRedisMessageService {
	
	/**
	 * 获取MessageId; 
	 * @return
	 */
	long getMessageId();
	
	/**
	 * 获取用户At消息
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	List<AtMessageForm> loadAtMessages(long uid, int start, int end);

	/**
	 * 发送At信息
	 * @param uid
	 * @param action
	 */
	void sendAtMessage(long uid, ActionModel action);

	/**
	 * 删除At消息
	 * @param uid
	 * @param action
	 */
	public void deleteAtMessage(long uid, ActionModel action);

	/**
	 * 获取At消息总数
	 * @param uid
	 * @return
	 */
	public int countAtMessage(long uid);

	/**
	 * 获取新的At消息
	 * @param uid
	 * @return
	 */
	public int countNewAtMessage(long uid);

	//私信相关-----------------

	/**
	 * 插入私信信息
	 */
	public MessageModel insertMessage(MessageModel message);


	/**
	 * 获取私信详细信息
	 * @param msgId
	 * @return
	 */
	public MessageModel getMessage(long msgId);

	/**
	 * 插入私信对话列表
	 * @param message
	 */
	public void insertDialogMessage(RedisMessageModel message);

	/**
	 * 插入对话列表
	 * @param uid
	 * @param dialogUid
	 */
	public void insertDialog(long uid, long dialogUid);


	/**
	 * 取得用户对话列表
	 * @param uid
	 * @return
	 */
	public List<DialogForm> loadDialogs(long uid);

	/**
	 * 取得用户对话详细列表
	 * @param uid
	 * @param dialogUid
	 * @return
	 */
	public List<MessageForm> loadMessages(long uid, long dialogUid, int start, int end);

	/**
	 * 取得用户对话详细列表
	 * @param uid
	 * @param dialogUid
	 * @return
	 */
	public List<MessageForm> loadNewMessages(long uid, long dialogUid);

	/**
	 * 取得对话详细信息数量
	 * @param uid
	 * @param dialogUid
	 * @return
	 */
	public int countMessages(long uid, long dialogUid);

	/**
	 * 取得用户新私信数
	 * @param uid
	 * @return
	 */
	public int countNewMessage(long uid);

	/**
	 * 删除单条私信内容
	 * @param message
	 * @return
	 */
	public boolean deleteSingleMessage(RedisMessageModel message);

	/**
	 * 删除整个对话
	 * @param uid
	 * @param revUid
	 * @return
	 */
	public boolean deleteDialog(long uid, long revUid);

	/**
	 * 判断此前是否有过对话。
	 * @param uid
	 * @param revUid
	 * @return
	 */
	public boolean hasDialogBefore(long uid, long revUid);

	/**
	 * 统计新的评论消息
	 * @param uid
	 * @return
	 */
	public int countNewCommentMessage(long uid);

	/**
	 * 统计所有评论信息
	 * @param uid
	 * @return
	 */
	public int countCommentMessage(long uid);

	/**
	 * 取出评论信息列表
	 * @param uid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<CommentMessageForm> loadCommentMessages(long uid, int start, int end);

	/**
	 * 删除接受到的评论
	 * @param uid
	 * @param commentId
	 */
	public boolean deleteCommentMessage(long uid, long commentId);
	
	/**
	 * 获取总的新消息数
	 * @param uid
	 * @return
	 */
	public int countTotalNewMessage(long uid);
	
	/**
	 * 判断用户是否有私信特权
	 * @param uid
	 * @return
	 */
	public boolean hasMessageRight(long uid);
}
