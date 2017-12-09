package com.unisharing.hoola.hoolaclient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
@Configuration
@SpringBootApplication
@ComponentScan(value = {"com.unisharing.hoola.hoolaredis","com.unisharing.hoola.hoolaclient"})


public class HoolaClientApplication {


	public static void main(String[] args) {
		SpringApplication.run(HoolaClientApplication.class, args);
	}
}
