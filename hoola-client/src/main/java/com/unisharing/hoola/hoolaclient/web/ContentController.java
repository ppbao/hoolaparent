package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.bean.AtMessageBean;
import com.unisharing.hoola.hoolacommon.bean.ContentBean;
import com.unisharing.hoola.hoolacommon.bean.ShareBean;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.ReportModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.submit.ContentSubmit;
import com.unisharing.hoola.hoolacommon.utils.AliyunUploadUtils;
import com.unisharing.hoola.hoolacommon.utils.EmojiUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容相关
 * 
 * @author wanghaihua
 * 
 */
@Controller
public class ContentController extends BaseController {
	Log logger = LogFactory.getLog(ContentController.class);
	private static int PAGE_SIZE = 20;
	@Resource
	IRedisUserService redisUserService;
	
	@Resource
	IRedisContentService redisContentService;
	
	@Resource
	IRedisTimelineService redisTimelineService;
	
	@Resource
	IRedisRelationshipService redisRelationshipService;
	
	@RequestMapping(value = "/content/upload", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result saveContent(@ModelAttribute ContentSubmit contentSubmit, @RequestParam("token")String token) {
		Result result = new Result();
		UserModel user = super.getUser(token);
		if (user == null){
			result.setCode(HoolaErrorCode.TOKEN_ERROR.getCode());
			result.setData(token);
			result.setMessage(HoolaErrorCode.TOKEN_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		long uid = user.getUid();
		if (uid == 0) {
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		} else {
			if(!StringUtils.isEmpty(contentSubmit.getGroupNames())){
				contentSubmit.setIsPrivate(2);//组内分享
			}
			String videoPath = "";
			try{
				//HoolaUtils.writeFile(video, contentSubmit.getVideo().getBytes()); // 写本地文件
				videoPath = AliyunUploadUtils.uploadHDVideo(contentSubmit.getVideo()
						.getInputStream(),
						contentSubmit.getVideo().getBytes().length,
						contentSubmit.getVideo().getContentType());
			}catch(Exception e){
				result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
				result.setData(0);
				result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+"上传视频错误！"+e.getMessage());
				result.setTime(System.currentTimeMillis());
				return result;
			}
			String thumbnailsPath = "";

			try{
				//HoolaUtils.writeFile(thumbnails, contentSubmit.getThumbnails().getBytes()); //写文件
				thumbnailsPath = AliyunUploadUtils.uploadThumbnail(
						contentSubmit.getThumbnails().getInputStream(),
						contentSubmit.getThumbnails().getBytes().length,
						contentSubmit.getThumbnails().getContentType());
				
			}catch(Exception e){
				result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
				result.setData(0);
				result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage()+"上传封面错误！"+e.getMessage());
				result.setTime(System.currentTimeMillis());
				return result;
			}
			ContentModel model = contentSubmit.asContentModel();
			model.setKey(HoolaUtils.md5(System.currentTimeMillis()+""));
			model.setUid(uid);
			model.setThumbnailsUrl(thumbnailsPath);
			model.setVideoUrlHD(videoPath);
			model.setVideoUrl(videoPath);//处理前高清流畅使用同一个文件
			
			try{
				model.setDescription(EmojiUtils.encodeEmoji(new String(contentSubmit.getDescription().getBytes("UTF-8"))));//替换成<e></e>格式
				//编码Emoji
			}catch(Exception e){
				model.setDescription(contentSubmit.getDescription());
			}
			redisContentService.insertContent(model);
			result.setCode(200);
			result.setData(model.getCid());
			result.setMessage("上传成功！");
			result.setTime(System.currentTimeMillis());
			
			ContentBean bean = contentSubmit.asContentBean();
			bean.setThumbnailsUrl(thumbnailsPath);
			bean.setVideoUrl(videoPath);
			bean.setVideoUrlHD(videoPath);
			bean.setCid(model.getCid());
			bean.setUid(uid);
			bean.setKey(model.getKey());
			bean.setDescription(EmojiUtils.encodeEmoji(bean.getDescription()));//替换成<e></e>格式
			try{
				redisJMSMessageService.insertAddContentMessageQueue(bean);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.CONTENT_ADD));
			}catch(Exception e){
				logger.error("发送NoticeMessage.MessageType.CONTENT_ADD出错！"+e.getMessage()+","+e);
			}
			return result;
		}
	}
	
	
	@RequestMapping(value = "/content/detail", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result getContentDetail(
			@RequestParam(value = "cid", required = true, defaultValue = "0") long cid,
			@RequestParam("token") String token) {
		Result result = new Result();
		ActionModel action = new ActionModel();
		action.setAction(ActionModel.Action.CREATE.getMark());
		action.setCid(cid);
		action.setUid(0);
		ActionForm actionForm = redisTimelineService.getActionForm(action, super.getUid(token), 0,false);
		result.setCode(200);
		result.setData(actionForm);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	@RequestMapping(value = "/content/isdelete", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result isContentDelete(
			@RequestParam(value = "cid", required = true, defaultValue = "0") long cid,
			@RequestParam("token") String token) {
		Result result = new Result();
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getcStatus() != 0 || content.getCid() == 0){
			result.setCode(HoolaErrorCode.CONTENT_DELETE.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.CONTENT_DELETE.getMessage());
			result.setTime(System.currentTimeMillis());
		}else{
			result.setCode(200);
			result.setData(0);
			result.setMessage("视频正常！");
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	
	@RequestMapping(value = "/content/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result deleteContent(
			@RequestParam(value = "cid", required = true, defaultValue = "0") long cid,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getCid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		if (content.getUid() != uid){//判断是否是自己的内容
			result.setCode(HoolaErrorCode.RIGHT_ERROR.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.RIGHT_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		redisContentService.updateContentStatus(cid, ContentModel.Status.AUTHOR_DELETE.getMark());
		redisContentService.deleteContentFromList(uid, cid);
		try{
			NoticeMessage message = new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.CONTENT_DELETE);
			message.setObjId(cid);
			hoolaPublisher.publish(message);
		}catch(Exception e){
			e.printStackTrace();
		}
		result.setCode(200);
		result.setData("");
		result.setMessage("删除成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	@RequestMapping(value = "/content/new/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadNewContentList(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		int start = (page - 1) * PAGE_SIZE;
		int end = page * PAGE_SIZE - 1;
		List<ActionForm> contents = redisContentService.listContents(uid, start, end);
		result.setCode(200);
		result.setData(contents);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		result.setPages((redisContentService.countContentList() - 1)/PAGE_SIZE + 1);
		return result;
	}
	
	
	@RequestMapping(value = "/content/find/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadFindContentList(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = getUid(token);
		int start = (page - 1) * PAGE_SIZE;
		int end = page * PAGE_SIZE - 1;
		List<ActionForm> contents = redisContentService.loadFindContentList(uid, start, end);
		result.setCode(200);
		result.setData(contents);
		result.setMessage("获取成功！");
		result.setTime(System.currentTimeMillis());
		result.setPages((redisContentService.countFindContent() - 1)/PAGE_SIZE + 1);
		return result;
	}
	
	/**
	 * 分享
	 * @param cid
	 * @param openType
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/content/share", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result shareContent(@RequestParam("cid") long cid,
			@RequestParam(value="memo",required=false)String memo,
			@RequestParam("openType") int openType,
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
		ShareBean bean = new ShareBean();
		ContentModel content = redisContentService.getContent(cid);
		if (content != null && content.getcStatus() == 0){
			bean.setCid(cid);
			bean.setOpenType(openType);
			bean.setUid(uid);
			bean.setMemo(memo);
			//redisContentService.insertShareContentList(uid,cid);
			try{
				redisJMSMessageService.insertShareQueue(bean);
				hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.SHARE));
			}catch(Exception e){
				logger.error("发送NoticeMessage.MessageType.SHARE出错！"+e.getMessage()+","+e);
			}
			result.setCode(200);
			result.setData("");
			result.setMessage("分享成功！");
			result.setTime(System.currentTimeMillis());
		}else{
			result.setCode(HoolaErrorCode.CONTENT_DELETE.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.CONTENT_DELETE.getMessage());
			result.setTime(System.currentTimeMillis());
		}
		return result;
	}
	
	
	/**
	 * AT内容
	 * @param cid
	 * @param :openType
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/content/at", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result atContent(@RequestParam("cid") long cid,
			@RequestParam(value="atDuanquUsers",required=false,defaultValue="")String atDuanquUsers,
			@RequestParam(value="atSinaUsers",required=false,defaultValue="")String atSinaUsers,
			@RequestParam(value="atTencentUsers",required=false,defaultValue="")String atTencentUsers,
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
		
		ContentModel content = redisContentService.getContent(cid);
		if (content == null || content.getCid() == 0){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		if (content.getIsPrivate() != 0 ){
			result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage()+"私密内容不能At");
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		redisTimelineService.insertUserAtList(uid, cid);
		
		try{
			AtMessageBean bean = new AtMessageBean();
			bean.setAtDuanquUsers(atDuanquUsers);
			bean.setAtSinaUsers(atSinaUsers);
			bean.setAtTencentUsers(atTencentUsers);
			bean.setCid(cid);
			bean.setUid(uid);
			redisJMSMessageService.insertAtQueue(bean);
			hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.AT_MESSAGE));
		}catch(Exception e){
			logger.error("发送NoticeMessage.MessageType.AT_MESSAGEE出错！"+e.getMessage()+","+e);
		}
		result.setCode(200);
		result.setData("");
		result.setMessage("AT成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	/**
	 * 举报
	 * @param cid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/content/report", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result reportContent(@RequestParam("cid") long cid,
			@RequestParam("token") String token) {
		UserModel user = this.getUser(token);
		Result result = new Result();
		if (user != null && user.getStatus() == 0){
			result = new Result();
			result.setCode(HoolaErrorCode.USER_FORBID.getCode());
			result.setData("");
			result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		
		ReportModel report = new ReportModel();
		report.setCid(cid);
		report.setCreateTime(System.currentTimeMillis());
		report.setIsCheck(0);
		report.setUid(getUid(token));
		try{
			redisJMSMessageService.insertReportQueue(report);
			hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.REPORT));
		}catch(Exception e){
			logger.error("发送NoticeMessage.MessageType.REPORT出错！"+e.getMessage()+","+e);
		}
		result.setCode(200);
		result.setData("");
		result.setMessage("举报成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}

}
