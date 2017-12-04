package com.unisharing.hoola.hoolacommon.submit;

import com.unisharing.hoola.hoolacommon.model.CommentModel;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;

import java.io.Serializable;

public class CommentSubmit implements Serializable {
	
	private static final long serialVersionUID = 8578526551139913511L;
	
	private long id;//评论ID 自赠
	private long cid;//内容ID
	private String commentText;//评论内容
	private long parentId;//父评论Id
	private long rootId;//根评论ID
	private long replyUid;//回复用户ID
	public long getId() {
		return id;
	}
	public long getCid() {
		return cid;
	}
	public String getCommentText() {
		return commentText;
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
	public void setCid(long cid) {
		this.cid = cid;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public void setRootId(long rootId) {
		this.rootId = rootId;
	}
	public long getReplyUid() {
		return replyUid;
	}
	public void setReplyUid(long replyUid) {
		this.replyUid = replyUid;
	}
	public CommentModel asCommentModel(){
		CommentModel model = new CommentModel();
		model.setCid(cid);
		model.setCommentText(EmojiUtils.encodeEmoji(commentText));
		model.setCommentUrl(this.parentId+"/"+this.id);
		model.setCreateTime(System.currentTimeMillis());
		model.setParentId(this.parentId);
		model.setReplyUid(replyUid);
		model.setRootId(rootId);
		return model;
	}
	@Override
	public String toString() {
		return "CommentSubmit [cid=" + cid + ", commentText=" + commentText
				+ ", id=" + id + ", parentId=" + parentId + ", rootId="
				+ rootId + "]";
	}

}
