package com.unisharing.hoola.hoolaredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserRedisService {
    @Autowired
    private RedisTemplate userRedisTemplate;

    public void setUser(String key,String value){
        userRedisTemplate.boundValueOps(key).set(value);
    }
}
