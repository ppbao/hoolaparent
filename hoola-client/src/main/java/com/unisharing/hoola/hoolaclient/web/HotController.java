package com.unisharing.hoola.hoolaclient.web;


import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.model.TagHotModel;
import com.unisharing.hoola.hoolacommon.vo.*;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.hot.IRedisHotService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HotController extends BaseController {
	private static int PAGE_SIZE = 24;
	
	private static int CONTENT_PAGE_SIZE = 24;
	
	private static int CONTENT_PAGE_SIZE_V15 = 20;//V1.5版本热门榜单每页数据
	
	private static int TAG_PAGE_SIZE = 60;//标签每页数量
	
	@Resource
	IRedisHotService redisHotService;
	@Resource
	IRedisContentService redisContentService;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/hot/index", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotIndex(@RequestParam("token")String token,
					@RequestParam(value="page",required=false,defaultValue="1")int page) {
		Result result = new Result();
		Map hotIndexForm = new HashMap();
		List<TalentListForm> users = redisHotService.loadHotUser(0, 0, 4);
		hotIndexForm.put("hotUser", users);
		
		List<ActionForm> contents = redisHotService.loadHotContent(0, 0, 4,0);
		hotIndexForm.put("hotContent", contents);
		
		List<String> tags = redisHotService.loadTags(0, -1);
		hotIndexForm.put("hotTag", tags);
		
		result.setCode(200);
		result.setData(hotIndexForm);
		result.setMessage("获取成功！");
		result.setPages(1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 最火内容首页
	 * @param token
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/hot/content/index", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotContentIndex(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="1")int page) {
		Result result = new Result();
		long uid = super.getUid(token);
		Map<String,List<ActionForm>> hotContentIndexForm = new HashMap<String,List<ActionForm>>();
		List<ActionForm> allTop = redisHotService.loadHotContent(0, 0, 19,1);
		hotContentIndexForm.put("all", allTop);
		
		List<ActionForm> weekTop = redisHotService.loadHotContent(0, 0, 19,2);
		hotContentIndexForm.put("week", weekTop);
		
		List<ActionForm> recommend =  redisContentService.loadFindContentList(uid, 0, 19);
		hotContentIndexForm.put("recommend", recommend);
		
		result.setCode(200);
		result.setData(hotContentIndexForm);
		result.setMessage("获取成功！");
		result.setPages(1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@RequestMapping(value = "/hot/content/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotContentList(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="type",required=false,defaultValue="0")int type) {
		int pageSize = 0;
		if (type == 0){
			pageSize = CONTENT_PAGE_SIZE;
		}else{
			pageSize = CONTENT_PAGE_SIZE_V15;
		}
		int start = (page - 1) * pageSize;
		int end = page * pageSize - 1;
		Result result = new Result();
		List<ActionForm> contents = redisHotService.loadHotContent(getUid(token), start, end,type);
		int count = redisHotService.countHotContent(type);
		result.setCode(200);
		result.setData(contents);
		result.setMessage("获取成功！");
		result.setPages((count - 1) / pageSize + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	@RequestMapping(value = "/hot/user/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotUserList(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="1")int page) {
		int start = (page - 1) * PAGE_SIZE;
		int end = page * PAGE_SIZE - 1;
		Result result = new Result();
		List<TalentListForm> users = redisHotService.loadHotUser(getUid(token), start, end);
		int count = redisHotService.countHotUser();
		result.setCode(200);
		result.setData(users);
		result.setMessage("获取成功！");
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 达人列表
	 * @param token
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/hot/talent", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotTalentList(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="1")int page) {
		int start = (page - 1) * PAGE_SIZE;
		
		int end = page * PAGE_SIZE - 1 + 2;
		if (start > 0) {
			start += 2;
		}
		Result result = new Result();
		List<TalentForm> users = redisHotService.loadTalentList(getUid(token), start, end);
		int count = redisHotService.countHotUser() - 2;
		result.setCode(200);
		result.setData(users);
		result.setMessage("获取成功！");
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	/**
	 * 热门频道
	 * @param token
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/hot/channel", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotChannelList(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="0")int page) {
		Result result = new Result();
		Map map = new HashMap();
		List<TagHotModel> channels = redisHotService.loadChannelTags();
		map.put("channels", channels);
		List<String> tags = redisHotService.loadTags(0,-1);
		map.put("tags", tags);
		result.setCode(200);
		result.setData(map);
		result.setMessage("获取成功！");
		result.setPages(1);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 热门频道V1.5
	 * @param token
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/v15/hot/channel", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotChannelList_V15(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="0")int page) {
		Result result = new Result();
		Map map = new HashMap();
		List<String> tags = new ArrayList<String>();
		int tagCount = 0;
		if (page == 1) {
			List<TagHotModel> channels = redisHotService.loadChannelTags();
			map.put("channels", channels);
		}
		tagCount = redisHotService.countTags();
		if (tagCount > 0) {
			tags = redisHotService.loadTags((page - 1) * TAG_PAGE_SIZE,	page * TAG_PAGE_SIZE);
		}
		map.put("tags", tags);
		result.setCode(200);
		result.setData(map);
		result.setMessage("获取成功！");
		result.setPages((tagCount - 1) / TAG_PAGE_SIZE + 1);
		result.setTotal(tagCount);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	/**
	 * 首页运营头条
	 * @param token
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/hot/banner", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result hotBannerList(@RequestParam("token")String token,
			@RequestParam(value="page",required=false,defaultValue="1")int page) {
		Result result = new Result();
		List<BannerForm> banners = redisHotService.loadBanners();
		result.setCode(200);
		result.setData(banners);
		result.setMessage("获取成功！");
		result.setPages(0);
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	/**
	 * 
	 * @param location publish search
	 * @param token
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/tag/recommend", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result listRecommendTag(@RequestParam(value="location",required=false) String location,
			@RequestParam("token") String token) {
		Result result = new Result();
		List tags = null;
		if (location.equals("search")){//Search
			tags = redisHotService.loadSearchTags();
		}else{
			tags = redisHotService.loadPublishTags();
		}
		
		result.setCode(200);
		result.setData(tags);
		result.setMessage("获取数据成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
	
	/**
	 * 话题列表
	 * @param : location publish search
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/subject/list", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadSubjects(@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = this.getUid(token);
		List<SubjectForm> subjects = new ArrayList<SubjectForm>();
		int count = redisHotService.countSubject();
		page=page==0?1:page;
		if (count > 0){
			subjects = redisHotService.loadSubjects(uid, (page -1)*PAGE_SIZE , page * PAGE_SIZE);
		}
		result.setCode(200);
		result.setData(subjects);
		result.setTotal(count);
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setMessage("获取数据成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	

	/**
	 * 话题列表
	 * @param :location publish search
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/subject/contents", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Result loadSubjectContents(@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="sid",required=true)int sid,
			@RequestParam("token") String token) {
		Result result = new Result();
		long uid = this.getUid(token);
		List<ActionForm> actions = new ArrayList<ActionForm>();
		int count = redisHotService.countSubjectContents(sid);
		if (count > 0){
			actions = redisHotService.loadSubjectContents(uid, sid, (page -1)*PAGE_SIZE , page * PAGE_SIZE);
		}
		result.setCode(200);
		result.setData(actions);
		result.setTotal(count);
		result.setPages((count - 1) / PAGE_SIZE + 1);
		result.setMessage("获取数据成功！");
		result.setTime(System.currentTimeMillis());
		return result;
	}
	
}
