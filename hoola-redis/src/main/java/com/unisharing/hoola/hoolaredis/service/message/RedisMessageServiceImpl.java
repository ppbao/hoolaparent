package com.unisharing.hoola.hoolaredis.service.message;


import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.vo.*;
import com.unisharing.hoola.hoolaredis.key.CommentKeyManager;
import com.unisharing.hoola.hoolaredis.key.MessageKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import com.unisharing.hoola.hoolaredis.service.comment.IRedisCommentService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Service;

import java.util.*;
@Service("redisMessageService")
public class RedisMessageServiceImpl extends BaseRedisService implements IRedisMessageService {
	@Autowired
	IRedisUserService redisUserService;
	@Autowired
	IRedisContentService redisContentService;
	@Autowired
	IRedisCommentService redisCommentService;
	@Autowired
	IRedisTimelineService redisTimelineService;
	
	private final HashMapper<MessageModel, String, String> messageMapper = new DecoratingStringHashMapper<MessageModel>(
			new JacksonHashMapper<MessageModel>(MessageModel.class));
	
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<AtMessageForm> loadAtMessages(long uid, int start, int end) {
		int newCount = 0;
		if (start == 0){
			DefaultStringRedisConnection connection = new DefaultStringRedisConnection(
					messageTemplate.getConnectionFactory().getConnection());
			newCount = this.countNewAtMessage(uid);
			int [] weights = {1,1};
			connection.zUnionStore(MessageKeyManager.getUserAtMessageKey(uid),
					Aggregate.MAX, weights,
					MessageKeyManager.getUserAtMessageKey(uid),
					MessageKeyManager.getUserNewAtMessageKey(uid));
			messageTemplate.delete(MessageKeyManager.getUserNewAtMessageKey(uid));
			connection.close();
		}
		Set set = messageTemplate.boundZSetOps(
				MessageKeyManager.getUserAtMessageKey(uid))
				.reverseRangeWithScores(start, end);
		List<AtMessageForm> atMessages = new ArrayList<AtMessageForm>();
		// 循环取出内容详细信息
		if (set != null) {
			Iterator it = set.iterator();
			int loop = 0;
			while (it.hasNext()) {
				TypedTuple obj = (TypedTuple) it.next();
				String value = (String)obj.getValue();
				ActionModel messageAction = ActionModel.parse(value);
				UserModel user = redisUserService.getUser(messageAction.getUid());
				AtMessageForm message = new AtMessageForm();
				message.setActionType(messageAction.getAction());
				message.setUser(user.asSimpleUserForm());
				ActionModel contentAction = new ActionModel();
				contentAction.setAction(ActionModel.Action.CREATE.getMark());
				contentAction.setCid(messageAction.getCid());
				contentAction.setUid(uid);
				ActionForm action = redisTimelineService.getActionForm(contentAction, uid, 0,false);
				//特殊处理，
				action.setActionUser(action.getContent().getUser());
				
				message.setAction(action);
				message.setTime(obj.getScore().longValue());
				if (loop < newCount) {
					message.setIsNew(1);
				}
				loop ++;
				atMessages.add(message);
			}
		}
		return atMessages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendAtMessage(long uid, ActionModel action) {
		messageTemplate.boundZSetOps(MessageKeyManager.getUserNewAtMessageKey(uid)).add(action.toString(), System.currentTimeMillis());
	}
	
	@SuppressWarnings("unchecked")
	public void deleteAtMessage(long uid,ActionModel action){
		messageTemplate.boundZSetOps(MessageKeyManager.getUserAtMessageKey(uid)).remove(action.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countAtMessage(long uid) {
		Long size = messageTemplate.boundZSetOps(MessageKeyManager.getUserAtMessageKey(uid)).size();
		return size == null ? 0 : size.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countNewAtMessage(long uid) {
		Long size = messageTemplate.boundZSetOps(MessageKeyManager.getUserNewAtMessageKey(uid)).size();
		return size == null ? 0 : size.intValue();
	}
	
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countMessages(long uid, long dialogUid) {
		Long size = messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(uid, dialogUid)).size();
		if (size != null){
			return size.intValue();
		}
		return 0;
	}

	@Override
	public int countNewMessage(long uid) {
		int count = 0;
		Map<Long,Integer> map = loadDialogNewMessageNum(uid);
		for (Integer value :map.values()){
			try{
				count += value;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return count;
	}
	
	/**
	 * 取得用户对话列表中新消息数，Redis中存放HashMap，Filed = uid，value = 新新消息数
	 * @param uid
	 * @return
	 */
	private Map<Long ,Integer> loadDialogNewMessageNum(long uid){
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(messageTemplate.getConnectionFactory().getConnection());
		Map<String,String> results = connection.hGetAll(MessageKeyManager.getNewMessageKey(uid));
		connection.close();
		Map<Long,Integer> map = new HashMap<Long,Integer>() ;
		for (String key :results.keySet()){
			try{
				long dialogUid = Long.parseLong(key);
				int newNum = Integer.parseInt(results.get(key));
				map.put(dialogUid, newNum);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * 更新对话列表的新消息数；
	 * @param uid
	 * @return
	 */
	private void addDialogNewMessageNum(long uid,long dialogUid){
		DefaultStringRedisConnection connection = new DefaultStringRedisConnection(messageTemplate.getConnectionFactory().getConnection());
		String count = connection.hGet(MessageKeyManager.getNewMessageKey(dialogUid), String.valueOf(uid));
		int i = 0;// 新消息数
		if (count != null) {
			try {
				i = Integer.parseInt(count) + 1;
			} catch (Exception e) {

				i = 1;
			}

		} else {
			i = 1;
		}
		connection.hSet(MessageKeyManager.getNewMessageKey(dialogUid), String.valueOf(uid), String.valueOf(i));
		connection.close();
	}

	@Override
	public MessageModel getMessage(long msgId) {
		return messageMapper.fromHash(this.getMessageMap(msgId));
	}
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getMessageMap(long msgId) {
		return new DefaultRedisMap<String, String>(MessageKeyManager.getMessageInfoKey(msgId), messageTemplate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertDialog(long uid, long dialogUid) {
		//插入自己的对话列表
		messageTemplate.boundZSetOps(MessageKeyManager.getDialogUsers(uid)).add(String.valueOf(dialogUid), System.currentTimeMillis());
		//插入接收者的对话列表
		messageTemplate.boundZSetOps(MessageKeyManager.getDialogUsers(dialogUid)).add(String.valueOf(uid), System.currentTimeMillis());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertDialogMessage(RedisMessageModel message) {
		//插入自己的私信详细信息
		messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(message.getSendUid(),message.getRevUid())).add(message.toString(), System.currentTimeMillis());
		//插入接受者的私信详细信息
		messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(message.getRevUid(),message.getSendUid())).add(message.toString(), System.currentTimeMillis());
		//更新对话列表的新消息数
		this.addDialogNewMessageNum(message.getSendUid(), message.getRevUid());
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public MessageModel insertMessage(MessageModel message) {
		if (message.getId() == 0){
			RedisAtomicLong messageIdCounter = new RedisAtomicLong(MessageKeyManager.getMessageIdKey(),super.messageTemplate.getConnectionFactory());
			message.setId(messageIdCounter.incrementAndGet());
		}
		messageTemplate.boundHashOps(MessageKeyManager.getMessageInfoKey(message.getId())).putAll(messageMapper.toHash(message));
		return message;
	}
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<DialogForm> loadDialogs(long uid) {
		List<DialogForm> dialogs = new ArrayList<DialogForm>();
		Map<Long,Integer> newMessage = this.loadDialogNewMessageNum(uid);
		Set set = messageTemplate.boundZSetOps(MessageKeyManager.getDialogUsers(uid)).reverseRange(0, 100);
		if (set != null && set.size()>0){
			Iterator it = set.iterator();
			while (it.hasNext()){
				String dialogUidStr = (String)it.next();
				try{
					DialogForm dialog = new DialogForm();
					long dialogUid = Long.parseLong(dialogUidStr);
					SimpleUserForm dialogUser = redisUserService.getUser(dialogUid).asSimpleUserForm();
					dialog.setUser(dialogUser);
					MessageModel message = this.getLastMessage(uid, dialogUid);
					if (message != null){
						dialog.setLastMessage(message.getMessageText());
						dialog.setTime(message.getCreateTime());
					}
					dialog.setNewNum(newMessage.get(dialogUid)== null? 0:newMessage.get(dialogUid));
					dialogs.add(dialog);
				}catch (Exception e) {
					
				}
			}
		}
		return dialogs;
	}
	
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<MessageForm> loadMessages(long uid, long dialogUid,int start,int end) {
		if (start == 0){
			clearNewMessageNum(uid,dialogUid);
		}
		List<MessageForm> messages = new ArrayList<MessageForm>();
		Set set = messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(uid, dialogUid)).reverseRange(start, end);
		for (Object obj : set){
			String str = (String)obj;
			RedisMessageModel rModel = RedisMessageModel.parse(str);
			if (rModel != null){
				MessageForm message = this.getMessage(rModel.getMsgId()).asForm();
				SimpleUserForm user = redisUserService.getUser(rModel.getSendUid()).asSimpleUserForm();
				message.setUser(user);
				messages.add(message);
			}
		}
		return messages;
	}
	
	/**
	 * 清除当前对话的新消息
	 * @param uid
	 * @param dialogUid
	 */
	@SuppressWarnings("unchecked")
	private void clearNewMessageNum(long uid,long dialogUid){
		messageTemplate.boundHashOps(MessageKeyManager.getNewMessageKey(uid)).delete(String.valueOf(dialogUid));
	}
	

	@SuppressWarnings({ "unchecked" })
	private MessageModel getLastMessage(long uid,long dialogUid){
		MessageModel message = null;;
		Set set = messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(uid, dialogUid)).reverseRange(0, 1);
		for(Object obj : set){
			String value = (String)obj;
			RedisMessageModel model = RedisMessageModel.parse(value);
			if (model != null){
				message = this.getMessage(model.getMsgId());
			}
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteSingleMessage(RedisMessageModel message) {
		MessageModel model = this.getMessage(message.getMsgId());
		Long  result = messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(message.getSendUid(), message.getRevUid())).remove(model.toRedisString());
		//判断对方是否也删除了该条消息，如果也删除了直接删除私信消息
		if (result > 0){
			Long index = messageTemplate.boundZSetOps(MessageKeyManager.getDialogKey(message.getRevUid(),message.getSendUid())).rank(model.toRedisString());
			if (index == null){
				messageTemplate.delete(MessageKeyManager.getMessageInfoKey(message.getMsgId()));
			}
		}
		return (result >0 );
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteDialog(long uid, long revUid) {
		try{
			//删除详细信息
			messageTemplate.delete(MessageKeyManager.getDialogKey(uid,revUid));
			//删除对话信息
			messageTemplate.boundZSetOps(MessageKeyManager.getDialogUsers(uid)).remove(String.valueOf(revUid));
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDialogBefore(long uid, long recUid) {
		Long index = messageTemplate.boundZSetOps(MessageKeyManager.getDialogUsers(uid)).rank(String.valueOf(recUid));
		return index != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countNewCommentMessage(long uid) {
		Long count = messageTemplate.boundZSetOps(CommentKeyManager.getUserReceiveNewCommentsKey(uid)).size();
		if (count != null){
			return count.intValue() ;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countCommentMessage(long uid) {
		Long count = messageTemplate.boundZSetOps(CommentKeyManager.getUserReceiveCommentsKey(uid)).size();
		if (count != null){
			return count.intValue() ;
		}
		return 0;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<CommentMessageForm> loadCommentMessages(long uid, int start,
			int end) {
		List<CommentMessageForm> commentMessages = new ArrayList<CommentMessageForm>();
		int newCount = 0;
		if (start == 0){
			newCount = this.countNewCommentMessage(uid);
			//合并新的和老的数据
			messageTemplate
					.boundZSetOps(
							CommentKeyManager.getUserReceiveCommentsKey(uid))
					.unionAndStore(
							CommentKeyManager.getUserReceiveNewCommentsKey(uid),
							CommentKeyManager.getUserReceiveCommentsKey(uid));
			//删除新列表
			messageTemplate.delete(CommentKeyManager.getUserReceiveNewCommentsKey(uid));
		}
		
		Set set = messageTemplate.boundZSetOps(CommentKeyManager.getUserReceiveCommentsKey(uid)).reverseRange(start, end);
		Iterator it = set.iterator();
		int i = 0;//
		while (it.hasNext()){
			String commentIdStr = (String)it.next();
			CommentMessageForm commentMessage = new CommentMessageForm();
			try{
				long commentId = Long.parseLong(commentIdStr);
				CommentModel comment = redisCommentService.getComment(commentId);
				if (comment != null && comment.getCid() > 0){
					UserModel user = redisUserService.getUser(comment.getUid());
					commentMessage.setUser(user.asSimpleUserForm());
					ActionModel contentAction = new ActionModel();
					contentAction.setAction(ActionModel.Action.CREATE.getMark());
					contentAction.setCid(comment.getCid());
					contentAction.setUid(uid);
					commentMessage.setAction(redisTimelineService.getActionForm(contentAction, uid, 0,false));
					if (i < newCount ){ //倒序排列，序号小于新消息数的都为新消息
						commentMessage.setIsNew(1);
					}
					commentMessage.setTime(comment.getCreateTime());
					if (comment.getParentId() == 0){//直接评论
						commentMessage.setMemo("评论了这条内容！");
					}else{
						//回复内容
						CommentModel parentComment = redisCommentService.getComment(comment.getParentId());
						commentMessage.setMemo("回复了我的评论："+ EmojiUtils.decodeEmoji(parentComment.getCommentText()));
					}
					commentMessage.setCommentText(EmojiUtils.decodeEmoji(comment.getCommentText()));
					commentMessage.setCommentId(commentId);
					commentMessages.add(commentMessage);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return commentMessages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteCommentMessage(long uid, long commentId) {
		Long result = messageTemplate.boundZSetOps(CommentKeyManager.getUserReceiveCommentsKey(uid)).remove(String.valueOf(commentId));
	    return (result > 0);
	}

	public void setRedisCommentService(IRedisCommentService redisCommentService) {
		this.redisCommentService = redisCommentService;
	}

	@Override
	public List<MessageForm> loadNewMessages(long uid, long dialogUid) {
		Map<Long,Integer> newMessage = this.loadDialogNewMessageNum(uid);
		int count = newMessage.get(dialogUid);
		if (count >0){
			return this.loadMessages(uid, dialogUid, 0, count);
		}
		return null;
	}

	@Override
	public int countTotalNewMessage(long uid) {
		int at = this.countNewAtMessage(uid);
		int comment = this.countNewCommentMessage(uid);
		int message = this.countNewMessage(uid);
		return at + comment + message;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	@Override
	public long getMessageId() {
		RedisAtomicLong messageIdCounter = new RedisAtomicLong(MessageKeyManager.getMessageIdKey(),super.messageTemplate.getConnectionFactory());
		return messageIdCounter.incrementAndGet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasMessageRight(long uid) {
		return messageTemplate.boundSetOps(MessageKeyManager.getMessageRightUser()).isMember(String.valueOf(uid));
	}
	
	
}
