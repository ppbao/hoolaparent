package com.unisharing.hoola.hoolacommon.index;


import com.unisharing.hoola.hoolacommon.model.IndexContentModel;
import com.unisharing.hoola.hoolacommon.model.IndexOpenUserModel;
import com.unisharing.hoola.hoolacommon.model.IndexUserModel;

public interface IESIndexBuilder {
	
	void buildUserIndex(IndexUserModel user);
	
	void buildConentIndex(IndexContentModel content);
	
	void buildOpenUserIndex(IndexOpenUserModel openUser);
	
	void deleteUserIndex(long id);
	
	void deleteContentIndex(long id);
}
