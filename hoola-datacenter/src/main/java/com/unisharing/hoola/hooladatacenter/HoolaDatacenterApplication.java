package com.unisharing.hoola.hooladatacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:redis/redis.properties")
@ComponentScan(value ={"com.unisharing.hoola.hoolaclient.*","com.unisharing.hoola.hoolaredis.*"})
        public class HoolaDatacenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoolaDatacenterApplication.class, args);
	}
}
/*
/C:/hoolafinal/hoolaparent/hoola-datacenter/src/main/java/com/unisharing/hoola/hooladatacenter/service/message/MessageServiceImpl.java:
        [4,56]
        package com.unisharing.hoola.hoolaclient.service.message does not exist
@ComponentScan(value = {"com.unisharing.hoola.hoolaredis",
        "com.unisharing.hoola.hoolacommon","com.unisharing.hoola.hoolaclient.service"})

*/