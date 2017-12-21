package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.vo.SysResourceForm;
import com.unisharing.hoola.hoolaredis.service.resource.IRedisResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ResourceController extends BaseController {
	
	private static int PAGE_SIZE = 20; 
	
	@Resource
	IRedisResourceService redisResourceService;
	
	@RequestMapping(value = "/res/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadResList(
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "type",defaultValue = "1",required = true) int type,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = super.getUid(token);
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		int count = redisResourceService.countSysResources(type);
		List<SysResourceForm> reses = redisResourceService.loadSysResources(uid, type, (page -1) * PAGE_SIZE, page * PAGE_SIZE);
		result.setCode(200);
		result.setData(reses);
		result.setMessage("获取成功！");
		result.setTotal(count);
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	/**判断是否解锁
	 * @param id
	 * @param type
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/res/stat/check", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result checkResouce(
			@RequestParam(value = "id",required = true) long id,
			@RequestParam(value ="type",defaultValue="2",required=false) int type,
			@RequestParam(value="token") String token
			){
		Result result=new Result();
		long uid=super.getUid(token);
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		boolean flag=redisResourceService.checkResource(uid, type, id);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("stat", flag);
		if(!flag){
			result=new Result();
			result.setCode(200);
			result.setMessage("未解锁");
			result.setData(map);
			result.setTime(System.currentTimeMillis());
			return result;
		}
		result.setCode(200);
		result.setMessage("已解锁");
		result.setData(map);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	/**解锁状态保存
	 * @param id
	 * @param type
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/res/stat/save", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result saveResouce(
			@RequestParam(value = "id",required = true) long id,
			@RequestParam(value ="type",defaultValue="2",required=false) int type,
			@RequestParam(value="token") String token
			){
		Result result=new Result();
		long uid=super.getUid(token);
		if (uid == 0){
			result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
			result.setData(0);
			result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
			result.setTime(System.currentTimeMillis());
			return result;
		}
		redisResourceService.saveResource(uid, type, id);
		result.setCode(200);
		result.setData("");
		result.setMessage("操作成功");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	

}
