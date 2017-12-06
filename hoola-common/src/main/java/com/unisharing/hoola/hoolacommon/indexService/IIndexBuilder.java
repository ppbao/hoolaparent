package com.unisharing.hoola.hoolacommon.indexService;


import com.unisharing.hoola.hoolacommon.model.IndexContentModel;
import com.unisharing.hoola.hoolacommon.model.IndexOpenUserModel;
import com.unisharing.hoola.hoolacommon.model.IndexUserModel;

public interface IIndexBuilder {
	
    void buildUserIndex(IndexUserModel user);
	
	void buildConentIndex(IndexContentModel content);
	
	void buildOpenUserIndex(IndexOpenUserModel openUser);
	
	void deleteUserIndex(long id);
	
	void deleteContentIndex(long id);

}
