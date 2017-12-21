package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.model.FeedBackModel;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolaredis.service.badword.IBadwordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class FeedbackController extends BaseController {
	
	@Resource
	IBadwordService badwordService;
	
	@RequestMapping(value = "/feedback", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result feedback(@RequestParam("content")String content,
					@RequestParam("token") String token) {
		Result result = new Result();
		FeedBackModel feedBack = new FeedBackModel();
		feedBack.setCreateTime(System.currentTimeMillis());
		feedBack.setFeedbackText(content);
		feedBack.setIsCheck(0);
		feedBack.setUid(getUid(token));
		try{
			redisJMSMessageService.insertFeedBackQueue(feedBack);
			hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.FEEDBACK));
		}catch(Exception e){
			e.printStackTrace();
		}
		result.setCode(200);
		result.setData("");
		result.setMessage("提交成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@RequestMapping(value = "/version/tips", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result getNewVersionTips() {
		Result result = new Result();
		String tips = badwordService.getNewVersionTips();
		result.setCode(200);
		result.setData(tips);
		result.setMessage("提交成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
}
