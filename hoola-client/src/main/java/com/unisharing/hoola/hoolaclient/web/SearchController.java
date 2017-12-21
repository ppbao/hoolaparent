package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolaclient.search.IESSearcher;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import com.unisharing.hoola.hoolaredis.service.hot.IRedisHotService;
import com.unisharing.hoola.hoolaredis.service.search.ISearcher;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController extends BaseController {
	
	private static int PAGESIZE = 20;
	
	@Resource
	ISearcher searcher;
	@Resource
	IRedisRelationshipService redisRelationshipService;
	@Resource
    IESSearcher esSearcher;
	@Resource
	IRedisHotService redisHotService;

	/***
	 *
	 *
	 * @param tag
	 * @param page
	 * @param token
	 * @param orderBy
	 * @return
	 */
	@RequestMapping(value = "/search/tag", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result searchContentByTag(@RequestParam("tag") String tag,
							  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
							  @RequestParam("token") String token,
							  @RequestParam(value="by",defaultValue="time",required=false)String orderBy) {
		Result result = new Result();
		long uid = getUid(token);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		if (orderBy == null || orderBy.equals("time")){//最新
			actions = searcher.searchTagContentsOrderByUploadTime(uid,tag, (page - 1) * PAGESIZE, page * PAGESIZE - 1);
		}else{//最热
			actions = searcher.searchTagContentsOrderByLikeNum(uid,tag, (page - 1) * PAGESIZE, page * PAGESIZE - 1);
		}
		result.setCode(200);
		result.setData(actions);
		result.setMessage("获取数据成功！");
		result.setPages((searcher.countContent(tag) - 1) / PAGESIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@RequestMapping(value = "/search/key", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result searchContentByKey(@RequestParam("key") String key,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		Map searchResult = esSearcher.searchContent(getUid(token),key, (page - 1) * PAGESIZE,  PAGESIZE);
		List<ActionForm> actions =(List) searchResult.get("result");
		result.setCode(200);
		result.setData(actions);
		result.setMessage("获取数据成功！");
		try{
			int count = Integer.parseInt((String)searchResult.get("count"));
			result.setPages((count -1) /PAGESIZE +1);
		}catch(Exception e){
			result.setPages(0);
		}
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/search/user", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result searchUserByNickName(@RequestParam("name") String nickName,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		Map searchResult = esSearcher.searchUser(getUid(token),nickName, (page - 1) * PAGESIZE,  PAGESIZE);
		List<FriendForm> friends =(List<FriendForm>) searchResult.get("result");
		result.setCode(200);
		result.setData(friends);
		result.setMessage("获取数据成功！");
		try{
			int count = Integer.parseInt((String)searchResult.get("count"));
			result.setPages((count -1) /PAGESIZE +1);
		}catch(Exception e){
			result.setPages(0);
		}
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/search/openuser", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Result searchOpenUserByNickName(@RequestParam("name") String nickName,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("openType")int openType,
			@RequestParam("token") String token) {
		Result result = new Result();
		int begin = (page - 1) * PAGESIZE;
		begin = begin < 0 ? 0 : begin;
		Map searchResult = esSearcher.searchOpenUser(getUid(token),nickName,openType, begin,  PAGESIZE);
		List<OpenFriend> friends =(List<OpenFriend>) searchResult.get("result");
		result.setCode(200);
		result.setData(friends);
		result.setMessage("获取数据成功！");
		try{
			int count = Integer.parseInt((String)searchResult.get("count"));
			result.setPages((count -1) /PAGESIZE +1);
		}catch(Exception e){
			result.setPages(0);
		}
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
}
