package com.unisharing.hoola.hoolaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(value = {"com.unisharing.hoola.hoolaredis","com.unisharing.hoola.hoolaclient"})
@PropertySource("classpath:redis/redis.properties")
public class HoolaClientApplication {


	public static void main(String[] args) {
		SpringApplication.run(HoolaClientApplication.class, args);
	}
}
