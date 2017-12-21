package com.unisharing.hoola.hoolaclient.search;


import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.ContentModel;
import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.FriendForm;
import com.unisharing.hoola.hoolaredis.key.FriendShipKeyManager;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component(value = "esSearcher")
public class ESSearcherImpl implements IESSearcher {
	private String searchClusterName;
	private String searchServiceIp;
	private int searchServicePort;
	
	IRedisUserService redisUserService;
	IRedisContentService redisContentService;
	IRedisRelationshipService redisRelationshipService;
	IRedisTimelineService redisTimelineService;
	//private JestClientFactory jestClientFactory;
	private String indexName;
	private String userType;
	private String contentType;
	private String openUserType;//第三方平台用户搜索

	@Override
	public Map<String ,Object> searchUser(long uid,String key,int start,int size) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<FriendForm> sUsers = new ArrayList<FriendForm>();
		try {
			Settings settings = ImmutableSettings.settingsBuilder().put(
					"cluster.name", this.searchClusterName).build();
			Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(
							this.searchServiceIp, this.searchServicePort));
			QueryBuilder tagQuery = QueryBuilders.fieldQuery("nickName", key);
			QueryBuilder descQuery = QueryBuilders.fieldQuery("signature", key);
			QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(
					descQuery).should(tagQuery);
			SearchResponse searchResponse = client
					.prepareSearch(this.indexName).setTypes(this.userType)
					.setQuery(queryBuilder).setFrom(start).setSize(size).execute().actionGet();
			SearchHits hits = searchResponse.getHits();
			client.close();
			resultMap.put("count", String.valueOf(hits.getTotalHits()));
			SearchHit[] searchHists = hits.getHits();
			if(searchHists.length>0){
		        for(SearchHit hit:searchHists){
		        	Integer resultUidStr = (Integer)hit.getSource().get("uid");
		        	long resultUid = Long.parseLong(resultUidStr.toString());
		        	FriendForm friend = new FriendForm();
					UserModel model = redisUserService.getUser(resultUid);
					if (model != null && model.getUid() > 0){
						friend.setUser(model.asSimpleUserForm());
						boolean isFollow = redisRelationshipService.isFollowed(uid, model.getUid());
						boolean isFans = redisRelationshipService.isFollowed(model.getUid(), uid);
						if (isFollow){
							friend.setIsFollow(1);
							if (isFans){
								friend.setIsFollow(2);
							}
						}
						sUsers.add(friend);
					}
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("count",0);
		}
		resultMap.put("result", sUsers);
		return resultMap;
	}
	
	@Override
	public Map<String ,Object> searchContent(long uid, String key,int start,int size) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ActionForm> actions = new ArrayList<ActionForm>();
		try{
			Settings settings = ImmutableSettings.settingsBuilder().put(
					"cluster.name", this.searchClusterName).build();
			Client client = new TransportClient(settings)   
		    .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
			QueryBuilder tagQuery = QueryBuilders.fieldQuery("tags", key);
			QueryBuilder descQuery = QueryBuilders.fieldQuery("description", key);
			QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(descQuery).should(tagQuery);
			SearchResponse searchResponse = client
					.prepareSearch(this.indexName).setTypes(this.contentType)
					.setQuery(queryBuilder).setFrom(start).setSize(size).addSort("time",SortOrder.DESC)
					.execute().actionGet();
		    SearchHits hits = searchResponse.getHits();
		    client.close();
		    resultMap.put("count", String.valueOf(hits.getTotalHits()));
		    SearchHit[] searchHists = hits.getHits();
		    if(searchHists.length>0){
		        for(SearchHit hit:searchHists){
		        	Integer resultCidStr = (Integer)hit.getSource().get("cid");
		        	long cid = Long.parseLong(resultCidStr.toString());
		        	ActionForm action = new ActionForm();
					ContentModel model = redisContentService.getContent(cid);
					if ((model != null && model.getcStatus()== ContentModel.Status.NORMAL.getMark())
							|| (model.getcStatus() == ContentModel.Status.SHIELDED.getMark() && model.getUid() == uid)){
						
						ActionModel actionModel = new ActionModel();
						actionModel.setAction(ActionModel.Action.CREATE.getMark());
						actionModel.setCid(model.getCid());
						actionModel.setUid(uid);
						action = redisTimelineService.getActionForm(actionModel, uid, 0, false);
						
						actions.add(action);
					}
		        }
		    }
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("count",0);
		}
	    resultMap.put("result", actions);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> searchOpenUser(long uid, String key,
			int openType, int start, int size) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<OpenFriend> friends = new ArrayList<OpenFriend>();
		try{
			Settings settings = ImmutableSettings.settingsBuilder().put(
					"cluster.name", this.searchClusterName).build();
			Client client = new TransportClient(settings)   
		    .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
			QueryBuilder nickNameQuery = QueryBuilders.fieldQuery("openNickName", key);
			QueryBuilder uidQuery = QueryBuilders.fieldQuery("uid", uid);
			QueryBuilder openTypeQuery = QueryBuilders.fieldQuery("openType", openType);
			QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(nickNameQuery).must(uidQuery).must(openTypeQuery);
			SearchResponse searchResponse = client
					.prepareSearch(this.indexName).setTypes(this.openUserType)
					.setQuery(queryBuilder).setFrom(start).setSize(size)
					.execute().actionGet();
		    SearchHits hits = searchResponse.getHits();
		    client.close();
		    resultMap.put("count", String.valueOf(hits.getTotalHits()));
		    SearchHit[] searchHists = hits.getHits();
		    if(searchHists.length>0){
		        for(SearchHit hit:searchHists){
		        	String openUserId = (String)hit.getSource().get("openUserId");
		        	String openFriendKey = this.getOpenFriendKey(openType, openUserId, uid);
		        	if (openFriendKey != null){
		        		OpenFriend openFriend = redisRelationshipService.getOpenFriend(openFriendKey);
		        		friends.add(openFriend);
		        	}
		        }
		    }
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("count",0);
		}
	    resultMap.put("result", friends);
		return resultMap;
	}
	
	private String getOpenFriendKey(int openType ,String openUserId,long uid){
		String key = null;
		switch(openType){
		case 1://新浪
			key = FriendShipKeyManager.getSinaUserInfoKey(openUserId);
			break;
		case 2://腾讯
			key = FriendShipKeyManager.getTencentUserInfoKey(openUserId);
			break;
		case 3://手机
			key = FriendShipKeyManager.getMobilesUserInfoKey(uid, openUserId);
			break;
		}
		return key;
	}
	
	public void setRedisUserService(IRedisUserService redisUserService) {
		this.redisUserService = redisUserService;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}

	public void setRedisRelationshipService(
			IRedisRelationshipService redisRelationshipService) {
		this.redisRelationshipService = redisRelationshipService;
	}

	public void setRedisTimelineService(IRedisTimelineService redisTimelineService) {
		this.redisTimelineService = redisTimelineService;
	}

	public void setOpenUserType(String openUserType) {
		this.openUserType = openUserType;
	}

	public void setSearchServiceIp(String searchServiceIp) {
		this.searchServiceIp = searchServiceIp;
	}

	public void setSearchServicePort(int searchServicePort) {
		this.searchServicePort = searchServicePort;
	}

	public void setSearchClusterName(String searchClusterName) {
		this.searchClusterName = searchClusterName;
	}
}
