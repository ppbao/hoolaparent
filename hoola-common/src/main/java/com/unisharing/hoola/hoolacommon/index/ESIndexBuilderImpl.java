package com.unisharing.hoola.hoolacommon.index;


import com.unisharing.hoola.hoolacommon.model.IndexContentModel;
import com.unisharing.hoola.hoolacommon.model.IndexOpenUserModel;
import com.unisharing.hoola.hoolacommon.model.IndexUserModel;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

public class ESIndexBuilderImpl implements IESIndexBuilder {
	
	private JestClientFactory jestClientFactory;
	
	private String indexName;
	
	private String userType;
	
	private String contentType;
	
	private String openUserType;

	@Override
	public void buildUserIndex(IndexUserModel user) {
		JestClient client = jestClientFactory.getObject();
		try {
			client.execute(new Index.Builder(user).index(indexName).type(userType).build());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.shutdownClient();
			client=null;
		}
	}


	@Override
	public void buildConentIndex(IndexContentModel content) {
		JestClient client = jestClientFactory.getObject();
		try {
			client.execute(new Index.Builder(content).index(indexName)
						.type(contentType).build());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.shutdownClient();
			client=null;
		}
	}
	
	@Override
	public void buildOpenUserIndex(IndexOpenUserModel openUser) {
		JestClient client = jestClientFactory.getObject();
		try {
			client.execute(new Index.Builder(openUser).index(indexName)
						.type(openUserType).build());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.shutdownClient();
			client=null;
		}
	}

	@Override
	public void deleteUserIndex(long uid) {
		
		JestClient client = jestClientFactory.getObject();
		try {
			client.execute(new Delete.Builder(String.valueOf(uid)).index(indexName).type(userType).build());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.shutdownClient();
			client=null;
		}
	}


	@Override
	public void deleteContentIndex(long cid) {
		JestClient client = jestClientFactory.getObject();
		try {
			client.execute(new Delete.Builder(String.valueOf(cid)).index(indexName).type(userType).build());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.shutdownClient();
			client=null;
		}
		
	}


	public void setJestClientFactory(JestClientFactory jestClientFactory) {
		this.jestClientFactory = jestClientFactory;
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


}
