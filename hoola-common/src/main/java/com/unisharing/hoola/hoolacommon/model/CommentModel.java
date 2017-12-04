package com.unisharing.hoola.hoolacommon.model;

/**
 * 评论信息
 */



import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.vo.CommentForm;

import java.io.Serializable;

public class CommentModel implements Serializable {
	
	private static final long serialVersionUID = 6873338686362119147L;
	private long id;//评论ID 自赠
	private long uid;//评论用户
	private long replyUid;//回复用户
	private long cid;//内容ID
	private String commentText;//评论内容
	private long createTime;//评论时间
	private String commentUrl;//评论树路径 1/2/3 （由ID串组成）
	private long parentId;//父评论Id
	private long rootId;//根评论ID
	public long getId() {
		return id;
	}
	public long getUid() {
		return uid;
	}
	public long getReplyUid() {
		return replyUid;
	}
	public long getCid() {
		return cid;
	}
	public String getCommentText() {
		return commentText;
	}
	public long getCreateTime() {
		return createTime;
	}
	public String getCommentUrl() {
		return commentUrl;
	}
	public long getParentId() {
		return parentId;
	}
	public long getRootId() {
		return rootId;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setReplyUid(long replyUid) {
		this.replyUid = replyUid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public void setRootId(long rootId) {
		this.rootId = rootId;
	}
	
	public CommentForm asCommentForm(){
		CommentForm form = new CommentForm();
		form.setCommentText(EmojiUtils.decodeEmoji(commentText));
		form.setCreateTime(createTime);
		form.setId(id);
		return form;
	}

}
