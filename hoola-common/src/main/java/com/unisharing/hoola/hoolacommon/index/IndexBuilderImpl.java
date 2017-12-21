package com.unisharing.hoola.hoolacommon.index;


import com.unisharing.hoola.hoolacommon.model.IndexContentModel;
import com.unisharing.hoola.hoolacommon.model.IndexOpenUserModel;
import com.unisharing.hoola.hoolacommon.model.IndexUserModel;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.io.IOException;

public class IndexBuilderImpl implements IIndexBuilder {
/*	private static String host = "192.168.10.205";
	private static int port = 9200;*/
	
	private String indexName;
	private String userType;
	private String contentType;
	private String openUserType;
	private String searchClusterName;
	private String searchServiceIp;
	private int searchServicePort;

	public void buildConentIndex(IndexContentModel content) {
		Settings settings = ImmutableSettings.settingsBuilder().put(
				"cluster.name", this.searchClusterName).build();
		Client client = new TransportClient(settings)   
        .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
		try {
			XContentBuilder builder = JsonXContent.contentBuilder().startObject()
				.field("cid",content.getCid())
			    .field("tags", content.getTags())
			    .field("description", content.getDescription())
			    .field("time", content.getTime())
			.endObject();
			
			client.prepareIndex(this.indexName, this.contentType,
					String.valueOf(content.getCid())).setSource(builder)
					.execute().actionGet();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	public void buildOpenUserIndex(IndexOpenUserModel openUser) {
		Settings settings = ImmutableSettings.settingsBuilder().put(
				"cluster.name", this.searchClusterName).build();
		Client client = new TransportClient(settings)   
        .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
		try {
			XContentBuilder builder = JsonXContent.contentBuilder().startObject()
				.field("openUserId",openUser.getOpenUserId())
			    .field("openNickName", openUser.getOpenNickName())
			    .field("uid", openUser.getUid())
			    .field("openType", openUser.getOpenType())
			.endObject();
			client.prepareIndex(this.indexName, this.openUserType, openUser.getId()).setSource(builder)
					.execute().actionGet();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}

	}


	public void buildUserIndex(IndexUserModel user) {
		Settings settings = ImmutableSettings.settingsBuilder().put(
				"cluster.name", this.searchClusterName).build();
		Client client = new TransportClient(settings)   
        .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ; 
		try {
			XContentBuilder builder = JsonXContent.contentBuilder().startObject()
				.field("uid",user.getUid())
			    .field("nickName", user.getNickName())
			    .field("signature", user.getSignature())
			    .field("time", user.getTime())
			.endObject();
			client.prepareIndex(this.indexName, this.userType,
					String.valueOf(user.getUid())).setSource(builder).execute()
					.actionGet();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	public void deleteContentIndex(long id) {
		Settings settings = ImmutableSettings.settingsBuilder().put(
				"cluster.name", this.searchClusterName).build();
		Client client = new TransportClient(settings)   
        .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
		try {
			client.prepareDelete(this.indexName, this.contentType,
					String.valueOf(id)).execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.close();
		}

	}

	public void deleteUserIndex(long id) {
		Settings settings = ImmutableSettings.settingsBuilder().put(
				"cluster.name", this.searchClusterName).build();
		Client client = new TransportClient(settings)   
        .addTransportAddress(new InetSocketTransportAddress(this.searchServiceIp, this.searchServicePort)) ;  
		try {
			client.prepareDelete(this.indexName, this.userType,
					String.valueOf(id)).execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
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
