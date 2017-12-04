package com.unisharing.hoola.hoolaredis.service;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RedisTestServiceImpl extends BaseRedisService implements IRedisTestService {

	@Override
	public void insertTestFans(String value) {
		relationTemplate.boundZSetOps("test:fans").add(value,System.currentTimeMillis());

	}

	@Override
	public void insertTestTimeline(String id, String value) {
		relationTemplate.boundZSetOps("user:"+id+":timeline").add(value, System.currentTimeMillis());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadTimeline(int start,int count) {
		DefaultStringRedisConnection connection = super.getDefaultStringRedisConnection();
		Map map = connection.hGetAll("test");
		Iterator ita = map.entrySet().iterator();
		while (ita.hasNext()){
			System.out.println(ita.next());
		}
		Set fans = relationTemplate.boundZSetOps("test:fans").rangeWithScores(start, start+count-1);
		Iterator it = fans.iterator();
		Set<String> keys = new HashSet<String>();
		while(it.hasNext()){
			TypedTuple fan = (TypedTuple)it.next();
			keys.add("user:"+fan.getValue()+":timeline");
		}
		
		relationTemplate.boundZSetOps("").unionAndStore(keys, "all:timeline");
		
	}

}
