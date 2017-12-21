package com.unisharing.hoola.hoolaclient.search;

import java.util.Map;

public interface IESSearcher {
	
	Map<String ,Object> searchUser(long uid, String key, int start, int size);

	Map<String ,Object> searchContent(long uid, String key, int start, int size);

	Map<String ,Object> searchOpenUser(long uid, String key, int openType, int start, int size);
}
