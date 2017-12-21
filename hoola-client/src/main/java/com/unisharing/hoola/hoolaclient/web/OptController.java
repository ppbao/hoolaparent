package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OptController extends BaseController {
	
	private static int PAGESIZE = 20;
	
	@Resource
	IRedisContentService redisContentService;
	@Resource
	IRedisRelationshipService redisRelationshipService;
	
	@Resource
	IRedisTimelineService redisTimelineService;

	/**
	 * 2013-09-10  需要恢复喜欢+转发，决定修改服务端接口，opt/like 调用地址为 opt/like/bak,使得客户端直接调用 opt/like地址但是实际逻辑为 opt/like/forward
	 * @param cid
	 * @param token
	 * @return
	 */
	
	@RequestMapping(value = "/opt/like/only", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result likeContent(@RequestParam(value = "cid") long cid,
					   @RequestParam("token") String token) {
		Result result = new Result();
		UserModel user = getUser(token);
		if (user != null && user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		long uid = user.getUid();
		ActionModel action = new ActionModel();
		action.setCid(cid);
		action.setUid(uid);
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getCid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("该内容不存在");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			if (content.getcStatus() != 0){
				result.setCode(HoolaErrorCode.CONTENT_DELETE.getCode());
				result.setData("该内容被删除");
				result.setMessage(HoolaErrorCode.CONTENT_DELETE.getMessage());
				result.setTime(System.currentTimeMillis());
			}else{
				if (content.getUid() == uid){
					result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
					result.setData("自己不能喜欢自己的内容！");
					result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
					result.setTime(System.currentTimeMillis());
				}else{
					boolean isLiked = redisContentService.isLiked(cid, uid);
					if (!isLiked){
						redisContentService.likeContent(cid, uid);
						action.setAction(ActionModel.Action.LIKE.getMark());
					}else{
						redisContentService.disLikeContent(cid, uid);
						action.setAction(ActionModel.Action.DISLIKE.getMark());
					}
					result.setCode(200);
					result.setData("");
					result.setMessage("操作成功！");
					result.setTime(System.currentTimeMillis());
				}
			}
		}
		if (result.getCode() == 200){
			try{
				if (action.getAction() == ActionModel.Action.DISLIKE.getMark()){
					//尝试删除本地消息队列的数据防止重复操作
					ActionModel likeAction = new ActionModel();
					BeanUtils.copyProperties(action, likeAction);
					likeAction.setAction(ActionModel.Action.LIKE.getMark());
					Long count = redisJMSMessageService.deleteLikeMessageQueue(likeAction);
					likeAction = null;
					if (count == 0){
						redisJMSMessageService.insertDislikeMessageQueue(action);
						hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.DISLIKE));
					}
				}else{
					ActionModel dislikeAction = new ActionModel();
					BeanUtils.copyProperties(action, dislikeAction);
					dislikeAction.setAction(ActionModel.Action.DISLIKE.getMark());
					Long count = redisJMSMessageService.deleteDislikeMessageQueue(dislikeAction);
					dislikeAction = null;
					if (count == 0){
						redisJMSMessageService.insertLikeMessageQueue(action);
						hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.LIKE));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/opt/forward", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result forwardContent(@RequestParam(value = "cid") long cid,
			@RequestParam("token") String token) {
		Result result = new Result();
		UserModel user = getUser(token);
		if (user != null && user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		long uid = user.getUid();
		ActionModel action = new ActionModel();
		action.setCid(cid);
		action.setAction(ActionModel.Action.FORWARD.getMark());
		action.setUid(uid);
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getCid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("该内容不存在");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			if (content.getcStatus() != 0){
				result.setCode(HoolaErrorCode.CONTENT_DELETE.getCode());
				result.setData("该内容被删除");
				result.setMessage(HoolaErrorCode.CONTENT_DELETE.getMessage());
				result.setTime(System.currentTimeMillis());
			}else{
				if (content.getUid() == uid){
					result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
					result.setData("自己不能转发自己的内容！");
					result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
					result.setTime(System.currentTimeMillis());
				}else{
					boolean isForward = redisContentService.isForwarded(cid, uid);
					if (!isForward){
						redisContentService.forwardContent(cid, uid);
						result.setCode(200);
						result.setData("");
						result.setMessage("转发成功！");
						result.setTime(System.currentTimeMillis());
						try{
							//尝试删除本地消息队列的数据防止重复操作
							redisJMSMessageService.deleteCancelForwardMessageQueue(action);
							redisJMSMessageService.insertForwardMessageQueue(action);
							hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.FORWARD));
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						redisContentService.cancelForwardContent(cid, uid);
						result.setCode(200);
						result.setData("");
						result.setMessage("取消转发成功！");
						result.setTime(System.currentTimeMillis());
						//发送取消转发信息
						try{
							redisJMSMessageService.deleteForwardMessageQueue(action);
							redisJMSMessageService.insertCancelForwardMessageQueue(action);
							hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.CANCEL_FORWARD));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 2013-09-10  需要恢复喜欢+转发，决定修改服务端接口，opt/like/forward 调用地址为 opt/like,因为客户端调用的就是这个接口
	 * @param cid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/opt/like", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result likeAndForwardContent(@RequestParam(value = "cid") long cid,
			@RequestParam("token") String token) {
		Result result = new Result();
		UserModel user = getUser(token);
		if (user != null && user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		long uid = user.getUid();
		ActionModel action = new ActionModel();
		action.setCid(cid);
		//action.setAction(Action.FORWARD.getMark());
		action.setUid(uid);
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getCid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData("该内容不存在");
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			if (content.getcStatus() != 0){
				result.setCode(HoolaErrorCode.CONTENT_DELETE.getCode());
				result.setData("该内容被删除");
				result.setMessage(HoolaErrorCode.CONTENT_DELETE.getMessage());
				result.setTime(System.currentTimeMillis());
			}else{
				if (content.getUid() == uid){
					result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
					result.setData("自己不能转发自己的内容！");
					result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
					result.setTime(System.currentTimeMillis());
				}else{
					boolean isForward = redisContentService.isForwarded(cid, uid);
					boolean isLiked = redisContentService.isLiked(cid, uid);
					if (!isLiked && !isForward){
						//redisContentService.forwardContent(cid, uid); 
						//redisContentService.likeContent(cid, uid);
						redisContentService.likeAndForwardContent(uid, cid);
						action.setAction(ActionModel.Action.LIKE.getMark());
						result.setCode(200);
						result.setData("");
						result.setMessage("喜欢转发成功！");
						result.setTime(System.currentTimeMillis());
						try{
							//尝试删除本地消息队列的数据防止重复操作
							Long count = redisJMSMessageService.deleteCancelLikeForwardQueue(action);
							if (count == 0){
								redisJMSMessageService.insertLikeForwardQueue(action);
								hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.LIKE_FORWARD));
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						//redisContentService.cancelForwardContent(cid, uid);
						//redisContentService.disLikeContent(cid, uid);
						redisContentService.cancelLikeAndForwardContent(uid, cid);
						action.setAction(ActionModel.Action.LIKE.getMark());
						result.setCode(200);
						result.setData("");
						result.setMessage("取消喜欢转发成功！");
						result.setTime(System.currentTimeMillis());
						//发送取消喜欢转发信息
						try{
							//尝试删除本地消息队列的数据防止重复操作
							Long count = redisJMSMessageService.deleteLikeForwardQueue(action);
							if (count == 0){
								redisJMSMessageService.insertCancelLikeForwardQueue(action);
								hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.CANCEL_LIKE_FORWARD));
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/like/users", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadLikeUsers(@RequestParam(value = "cid") long cid,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		List<FriendForm> friends = redisContentService.loadLikeUsers(cid, (page - 1) * PAGESIZE, page * PAGESIZE - 1);
		for (FriendForm friend : friends){
			boolean isFollow = redisRelationshipService.isFollowed(uid, friend.getUser().getUid());
			boolean isFans = redisRelationshipService.isFollowed(friend.getUser().getUid(), uid);
			if (isFollow){
				friend.setIsFollow(1);
				if (isFans){
					friend.setIsFollow(2);
				}
			}
		}
		result.setCode(200);
		result.setData(friends);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		int total = redisContentService.countLikeUsers(cid);
		result.setPages(( total - 1)/PAGESIZE + 1);
		return result;
	}
	
	@RequestMapping(value = "/forward/users", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadForwardUsers(@RequestParam(value = "cid") long cid,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		List<FriendForm> friends = redisContentService.loadForwardUsers(cid, (page - 1) * PAGESIZE, page * PAGESIZE - 1);
		for (FriendForm friend : friends){
			boolean isFollow = redisRelationshipService.isFollowed(uid, friend.getUser().getUid());
			boolean isFans = redisRelationshipService.isFollowed(friend.getUser().getUid(), uid);
			if (isFollow){
				friend.setIsFollow(1);
				if (isFans){
					friend.setIsFollow(2);
				}
			}
		}
		result.setCode(200);
		result.setData(friends);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		int total = redisContentService.countForwardUsers(cid);
		result.setPages(( total - 1)/PAGESIZE + 1);
		return result;
	}
	
	
	@RequestMapping(value = "/opt/all/users", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadAllOptUsers(@RequestParam(value = "cid") long cid,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		List<FriendForm> friends = new ArrayList<FriendForm>();
		List<SimpleUserForm> users = redisTimelineService.loadOptUsers(cid, (page - 1) * PAGESIZE, page * PAGESIZE - 1);
		for (SimpleUserForm user : users){
			FriendForm friend = new FriendForm();
			boolean isFollow = redisRelationshipService.isFollowed(uid, user.getUid());
			boolean isFans = redisRelationshipService.isFollowed(user.getUid(), uid);
			if (isFollow){
				friend.setIsFollow(1);
				if (isFans){
					friend.setIsFollow(2);
				}
			}
			friend.setUser(user);
			friends.add(friend);
		}
		result.setCode(200);
		result.setData(friends);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		int total = redisContentService.countLikeUsers(cid);
		result.setPages(( total - 1)/PAGESIZE + 1);
		return result;
	}
	
	@RequestMapping(value = "/opt/play", method = RequestMethod.POST, produces = "application/json")
	public void playContent(@RequestParam(value = "cid") long cid,
			@RequestParam("token") String token) {
		redisContentService.updatePlayNum(cid);
		redisContentService.insertCidIntoQueue(cid);
	}
}
