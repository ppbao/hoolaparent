package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShareController {
	
	/**
	 * 朋友圈
	 * @param cid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/share/weixin/friends", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result shareContentToWeixinFriends(@RequestParam("cid") long cid,
									   @RequestParam("token") String token) {
		
		
		
		return null;
	}
	
	/**
	 * 微信好友
	 * @param cid
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/share/weixin/friend", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result shareContentToWeixinFriend(@RequestParam("cid") long cid,
			@RequestParam("token") String token) {
		
		return null;
	}

}
