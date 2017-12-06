package com.unisharing.hoola.hoolaredis.service.comment;


import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.CommentModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.vo.CommentForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.key.CommentKeyManager;
import com.unisharing.hoola.hoolaredis.key.ContentKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Service;

import java.util.*;
@Service("redisCommentService")
public class RedisCommentServiceImpl extends BaseRedisService implements
		IRedisCommentService {
	Log logger = LogFactory.getLog(RedisCommentServiceImpl.class);
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisContentService redisContentService;
	
	private final HashMapper<CommentModel, String, String> commentMapper = new DecoratingStringHashMapper<CommentModel>(
			new JacksonHashMapper<CommentModel>(CommentModel.class));
	
	@Override
	public CommentModel getComment(long id) {
		return commentMapper.fromHash(getCommentMap(id));
	}

	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getCommentMap(long id) {
		return new DefaultRedisMap<String, String>(CommentKeyManager.getCommentInfoKey(id), commentTemplate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommentModel addComment(CommentModel comment) {
		//生成ID
		if (comment.getId() == 0){
			RedisAtomicLong commentIdCounter = new RedisAtomicLong(CommentKeyManager.getCommentIdKey(),super.commentTemplate.getConnectionFactory());
			comment.setId(commentIdCounter.incrementAndGet());
		}
		boolean bool = false;
		//插入评论主体信息
		commentTemplate.boundHashOps(CommentKeyManager.getCommentInfoKey(comment.getId())).putAll(commentMapper.toHash(comment));
		if (comment.getParentId() == 0){
			//内容的主评论
			bool = commentTemplate.boundZSetOps(CommentKeyManager.getContentCommentKey(comment.getCid())).add(String.valueOf(comment.getId()), System.currentTimeMillis());
		} else {
			//回复评论
			bool = commentTemplate.boundZSetOps(CommentKeyManager.getSubCommentKey(comment.getRootId())).add(String.valueOf(comment.getId()),System.currentTimeMillis());
		}
		if (bool){
			//添加评论用户列表
			relationTemplate.boundZSetOps(
					CommentKeyManager.getContentCommentUsersKey(comment.getCid()))
						.add(comment.getUid() + ":"+ ActionModel.Action.COMMENT.getMark(),
								System.currentTimeMillis());
			//更新评论数量
			updateCommentNum(comment.getCid(),1);
		}
		return comment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommentForm> loadComments(long cid, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		int end = page * pageSize -1;
		Set set = commentTemplate.boundZSetOps(CommentKeyManager.getContentCommentKey(cid)).reverseRange(start, end);
		List<CommentForm> comments = new ArrayList<CommentForm>();
		Iterator it = set.iterator();
		while (it.hasNext()){
			String commentId = (String)it.next();
			try{
				Long commentIdInt = Long.parseLong(commentId);
				CommentModel model = getComment(commentIdInt);
				if (model != null && model.getId() > 0) {
					CommentForm form = model.asCommentForm();
					SimpleUserForm sendUser = redisUserService.getUser(model.getUid()).asSimpleUserForm();
					form.setUser(sendUser);
					SimpleUserForm replyUser = redisUserService.getUser(model.getReplyUid()).asSimpleUserForm();
					form.setReplyUser(replyUser);
					form.setReplyComments(this.loadSubComments(model.getId()));
					comments.add(form);
				}
			}catch(Exception e){
				logger.error("params:cid=" + cid + ",page=" + page
						+ ",pageSize=" + pageSize + ";messsage="
						+ e.getMessage());
			}
		}
		return comments;
	}

	@SuppressWarnings("unchecked")
	private List<CommentForm> loadSubComments(long id) {
		Set set = commentTemplate.boundZSetOps(CommentKeyManager.getSubCommentKey(id)).range(0, -1);
		List<CommentForm> subComments = new ArrayList<CommentForm>();
		Iterator it = set.iterator();
		while (it.hasNext()){
			String commentId = (String)it.next();
			try{
				Long commentIdInt = Long.parseLong(commentId);
				CommentModel model = getComment(commentIdInt);
				if (model != null && model.getId() > 0) {
					CommentForm form = model.asCommentForm();
					SimpleUserForm sendUser = redisUserService.getUser(model.getUid()).asSimpleUserForm();
					form.setUser(sendUser);
					SimpleUserForm replyUser = redisUserService.getUser(model.getReplyUid()).asSimpleUserForm();
					form.setReplyUser(replyUser);
					subComments.add(form);
				}
			}catch(Exception e){
				logger.error("params:id="+id+";messsage="+e.getMessage());
			}
		}
		return subComments;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int deleteComment(CommentModel comment) {
		int count = 0;
		Long result = 0L;
		if (comment.getParentId()>0){
			//属于子评论
			result = commentTemplate.boundZSetOps(CommentKeyManager.getSubCommentKey(comment.getRootId())).remove(String.valueOf(comment.getId()));
			//删除用户操作区的用户信息
			deleteOptUserForComment(comment.getCid(),comment.getUid());
		}else{
			//主评论
			result = commentTemplate.boundZSetOps(CommentKeyManager.getContentCommentKey(comment.getCid())).remove(String.valueOf(comment.getId()));
			
			Set set = commentTemplate.boundZSetOps(CommentKeyManager.getSubCommentKey(comment.getId())).range(0, -1);
			//删除所有子评论信息
			Iterator it = set.iterator();
			Set<String> keys = new HashSet<String>();
			while (it.hasNext()){
				String comId = (String)it.next();
				keys.add(CommentKeyManager.getCommentInfoKey(Long.parseLong(comId)));
				//删除所有子评论用户操作区数据
				CommentModel subComment = this.getComment(Long.parseLong(comId));
				deleteOptUserForComment(comment.getCid(),subComment.getUid());
				
			}
			commentTemplate.delete(keys);
			count += keys.size();
			if (result > 0){
				//删除主评论用户操作区数据
				deleteOptUserForComment(comment.getCid(),comment.getUid());
				//删除所有子评论
				commentTemplate.delete(CommentKeyManager.getSubCommentKey(comment.getId()));
				
			}
		}
		if (result > 0){
			//删除评论信息
			commentTemplate.delete(CommentKeyManager.getCommentInfoKey(comment.getId()));
			count ++;
		}
		//更新内容评论数
		updateCommentNum(comment.getCid(), -count);
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	private void updateCommentNum(long cid,int count){
		Long num = contentTemplate.boundValueOps(ContentKeyManager.getContentCommentNumKey(cid)).increment(count);
		redisContentService.updateCommentNum(cid, num.intValue());
	}
	@SuppressWarnings("unchecked")
	private void deleteOptUserForComment(long cid,long uid){
		relationTemplate.boundZSetOps(
				CommentKeyManager.getContentCommentUsersKey(cid))
					.remove(uid + ":"+ ActionModel.Action.COMMENT.getMark());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countComments(long cid) {
		Long count = commentTemplate.boundZSetOps(CommentKeyManager.getContentCommentKey(cid)).size();
		return count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertUserCommentMessage(long commentId) {
		CommentModel comment = this.getComment(commentId);
		if (comment != null && comment.getId() > 0){
			ContentModel content = redisContentService.getContent(comment.getCid());
			if (content != null && content.getCid() > 0){
				if (content.getUid() != comment.getUid()){
					messageTemplate.boundZSetOps(
							CommentKeyManager.getUserReceiveNewCommentsKey(content
									.getUid())).add(String.valueOf(commentId),
							System.currentTimeMillis());
				}
			}
			
			if (comment.getReplyUid() > 0){
				messageTemplate.boundZSetOps(
						CommentKeyManager.getUserReceiveNewCommentsKey(comment
								.getReplyUid())).add(String.valueOf(commentId),
						System.currentTimeMillis());
			}
		}
	}
	
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	@Override
	public long getCommentId() {
		// 生成ID
		RedisAtomicLong commentIdCounter = new RedisAtomicLong(
				CommentKeyManager.getCommentIdKey(),
				super.commentTemplate.getConnectionFactory());
		return commentIdCounter.incrementAndGet();
	}

}
