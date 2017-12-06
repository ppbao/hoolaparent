package com.unisharing.hoola.hoolaredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

@SpringBootApplication

public class HoolaRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoolaRedisApplication.class, args);
	}
 }
