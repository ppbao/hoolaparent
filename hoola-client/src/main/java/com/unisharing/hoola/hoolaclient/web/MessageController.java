package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.bean.DialogBean;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.submit.MessageSubmit;
import com.unisharing.hoola.hoolacommon.utils.HoolaStringUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolacommon.vo.*;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class MessageController extends BaseController {
	
	@Resource
	IRedisMessageService redisMessageService;
	@Resource
	IRedisRelationshipService redisRelationshipService;
	@Resource
	IRedisTimelineService redisTimelineService;

	private static int PAGE_SIZE = 20;

	@RequestMapping(value = "/message/at", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result sendAtMessage(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("token") String token) {
		UserModel user = getUser(token);
		Result result = new Result();
		
		int start = (page - 1) * PAGE_SIZE;
		int end = page * PAGE_SIZE - 1;
		
		List<AtMessageForm> messages = redisMessageService.loadAtMessages(user.getUid(), start, end);
		
		int count = redisMessageService.countAtMessage(user.getUid());
		result.setCode(200);
		result.setData(messages);
		result.setMessage("获取成功！");
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@RequestMapping(value = "/message/at/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result deleteAtMessage(
			@RequestParam("cid")int cid,
			@RequestParam("action")int action,
			@RequestParam("uid") long uid,
			@RequestParam("token") String token) {
		UserModel user = this.getUser(token);
		Result result = new Result();
		ActionModel model = new ActionModel();
		model.setAction(action);
		model.setCid(cid);
		model.setUid(uid);
		redisMessageService.deleteAtMessage(user.getUid(), model);
		result.setCode(200);
		result.setData("");
		result.setMessage("删除成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 发送私信
	 * @param message
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/message/send", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result sendMessage(@ModelAttribute MessageSubmit message,
			@RequestParam("token") String token) {
		UserModel sendUser = this.getUser(token);
		Result result = valid(sendUser.getUid(),message);
		if (result != null){
			return result;
		}
		result = new Result();
		
		if (canSendTo(sendUser.getUid(), message.getRecUid())){
			try{
				MessageModel model = new MessageModel();
				model.setCreateTime(System.currentTimeMillis());
				model.setIsDelete(0);
				model.setIsNew(1);
				model.setMessageText(message.getMessageText());
				model.setRecUid(message.getRecUid());
				model.setUid(sendUser.getUid());
				//插入私信详细信息
				model = redisMessageService.insertMessage(model);
				//插入对话列表
				redisMessageService.insertDialog(sendUser.getUid(), message.getRecUid());
				//插入对话详细列表
				RedisMessageModel redisModel = model.asRedisForm();
				redisMessageService.insertDialogMessage(redisModel);
				
				result.setCode(200);
				result.setData(model.getId());
				result.setMessage("发送成功!");
				result.setTime(System.currentTimeMillis());
				//发送消息给消息处理中心
				try{
					redisJMSMessageService.insertSendMessageQueue(model);
					hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.SEND_MESSAGE));
				}catch(Exception e){
					e.printStackTrace();
				}
				return result;
			} catch (Exception e) {
				result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
				result.setData(0);
				result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+":"+e.getMessage()+";"+e.getLocalizedMessage());
				result.setTime(System.currentTimeMillis());
				return result;
			}
			
		} else {
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage()+":接收者不是你粉丝所以不能给ta发私信！");
			result.setTime(System.currentTimeMillis());
			return result;
		}
	}
	
	@RequestMapping(value = "/message/dialog", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadDialogs(@RequestParam("token") String token) {
		UserModel user = this.getUser(token);
		Result result = new Result();
		if (user != null){
			
			List<DialogForm> dialogs = redisMessageService.loadDialogs(user.getUid());
			result.setCode(200);
			result.setData(dialogs);
			result.setMessage("获取成功！");
			result.setPages(1);
			result.setTime(System.currentTimeMillis());
		}else{
			result.setCode(HoolaErrorCode.TOKEN_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.TOKEN_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/message/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadMessages(@RequestParam("uid") long uid,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam("token") String token) {
		
		int start = (page - 1) * PAGE_SIZE;
		int end = page * PAGE_SIZE - 1;
		
		UserModel user = this.getUser(token);
		Result result = new Result();
		List<MessageForm> messages = redisMessageService.loadMessages(user.getUid(), uid, start, end);
		result.setCode(200);
		result.setData(messages);
		result.setMessage("获取成功！");
		result.setPages(1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@RequestMapping(value = "/message/list/refresh", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadMessages(@RequestParam("uid") long uid,
			@RequestParam("token") String token) {
		UserModel user = this.getUser(token);
		Result result = new Result();
		List<MessageForm> messages = redisMessageService.loadNewMessages(user.getUid(), uid);
		result.setCode(200);
		result.setData(messages);
		result.setMessage("获取成功！");
		result.setPages(1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	@RequestMapping(value = "/message/single/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result deleteSingleMessage(@RequestParam("uid") long uid,
			@RequestParam("msgId") long msgId,
			@RequestParam("token") String token) {

		UserModel user = this.getUser(token);
		Result result = new Result();
		try {
			RedisMessageModel message = new RedisMessageModel();
			message.setMsgId(msgId);
			message.setRevUid(uid);
			message.setSendUid(user.getUid());
			redisMessageService.deleteSingleMessage(message);
			result.setCode(200);
			result.setData("");
			result.setMessage("删除成功！");
			result.setPages(1);
			result.setTime(System.currentTimeMillis());
		} catch (Exception e) {
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+",Message="+e.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		
		if (result.getCode() == 200){
			//发送消息给消息处理中心
			try{
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.DELETE_MESSAGE,msgId));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	@RequestMapping(value = "/message/dialog/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result deleteSingleMessage(@RequestParam("uid") long uid,
			@RequestParam("token") String token) {

		UserModel user = this.getUser(token);
		Result result = new Result();
		try{
			redisMessageService.deleteDialog(user.getUid(), uid);
			result.setCode(200);
			result.setData("");
			result.setMessage("删除成功！");
			result.setPages(1);
			result.setTime(System.currentTimeMillis());
		}catch (Exception e) {
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+",Message="+e.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		
		if (result.getCode() == 200){
			//发送消息给消息处理中心
			try{
				DialogBean bean = new DialogBean();
				bean.setDialogUid(uid);
				bean.setUid(user.getUid());
				redisJMSMessageService.insertDeleteDialogQueue(bean);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.DELETE_DIALOG));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/message/comments", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadCommentMessage(@RequestParam(value="page",defaultValue="1",required=false) int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		UserModel user = this.getUser(token);
		int count = redisMessageService.countCommentMessage(user.getUid());
		int newCount = redisMessageService.countNewCommentMessage(user.getUid());
		int start = (page - 1) * PAGE_SIZE;
		start = start < 0 ? 0 : start;
		int end = page * PAGE_SIZE - 1;
		
		try{
			List<CommentMessageForm> commentMessages = redisMessageService.loadCommentMessages(user.getUid(), start, end);
			result.setCode(200);
			result.setData(commentMessages);
			result.setMessage("获取成功！");
			result.setPages((count + newCount - 1)/PAGE_SIZE + 1 );
			result.setTime(System.currentTimeMillis());
		}catch (Exception e) {
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+",Message="+e.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	@RequestMapping(value = "/message/comment/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result deleteCommentMessage(@RequestParam("commentId")long commentId,
			@RequestParam("token") String token) {
		Result result = new Result();
		UserModel user = this.getUser(token);
		boolean bool = redisMessageService.deleteCommentMessage(user.getUid(), commentId);
		if (bool){
			result.setCode(200);
			result.setData("");
			result.setMessage("删除成功！");
			result.setPages(0);
			result.setTime(System.currentTimeMillis());
		}else{
			result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	@RequestMapping(value = "/newmessage/notification", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result newMessageNotification(
			@RequestParam(value="newListLastVisit",required=false)Long newLastVisitTime,
			@RequestParam(value="hotListLastVisit",required=false)Long hotLastVisitTime,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		int at = redisMessageService.countNewAtMessage(uid);
		int comment = redisMessageService.countNewCommentMessage(uid);
		int message = redisMessageService.countNewMessage(uid);
		int timeline = redisTimelineService.countNewTimeline(uid);
		int friendNum = redisRelationshipService.countNewFans(uid); //新粉丝数
		int newSinaRecNum = redisRelationshipService.countNewRecommendFriendsNum(uid);//新的第三方平台推荐数
		SettingModel setting = redisUserService.getUserSetting(uid);
		if (setting == null){
			setting = new SettingModel();
		}
		int isCamera = setting.getIsCamera();
		NotificationForm notice = new NotificationForm();
		notice.setIsCamera(isCamera);//是否直接进入拍摄
		notice.setAtMessageNum(at);
		notice.setCommentMessageNum(comment);
		notice.setMessageNum(message);
		notice.setHotNum(0);
		notice.setNewNum(0);
		notice.setTimelineNum(timeline);
		notice.setFriendNum(friendNum);
		notice.setRecFriendNum(newSinaRecNum);
		result.setCode(200);
		result.setData(notice);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	private Result valid(long sendUid,MessageSubmit message){
		Result result = new Result();
		UserModel sendUser = redisUserService.getUser(sendUid);
		if (sendUser != null && sendUser.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		if (StringUtils.hasText(message.getMessageText())){
			String fw = HoolaStringUtils.getForbiddenWord(message.getMessageText());
			if (fw != null){
				result.setCode(HoolaErrorCode.SENSITIVE.getCode());
				result.setData("");
				result.setMessage(HoolaErrorCode.SENSITIVE.getMessage()+",敏感词为："+fw);
				result.setTime(System.currentTimeMillis());
				return result;
			}
		}else{
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage()+":消息内容不能为空！");
			result.setTime(System.currentTimeMillis());
			return result;
		}
		UserModel revUser = redisUserService.getUser(message.getRecUid());
		
		if (revUser == null || revUser.getUid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage()+":接收者ID有误！");
			result.setTime(System.currentTimeMillis());
			return result;
		}
		//是否有权限发送私信
		
		return null;
	}
	
	/**
	 * 判断是否可以给他发私信
	 * @param uid //发送者
	 * @param recUid // 接受者
	 * @return
	 */
	private boolean canSendTo(long uid,long recUid){
		//判断接受者之前跟你是否有对话关系
		//boolean hasDialogBefore = redisMessageService.hasDialogBefore(recUid, uid);
		
		//判断接受者是否已经关注你了
		//boolean isFollowed = redisRelationshipService.isFollowed(recUid, uid);
		
		//是否有特权
	//	boolean hasRight = redisMessageService.hasMessageRight(uid);
		
		//判断发送者是不是接收者的黑名单成员
		boolean isBlack = redisRelationshipService.isBlackUser(recUid, uid);
		
		return (!isBlack);
	}

}
