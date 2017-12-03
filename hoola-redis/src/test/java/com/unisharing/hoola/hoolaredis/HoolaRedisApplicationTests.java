package com.unisharing.hoola.hoolaredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//spring test use springbootTest classes to load configuration files
@SpringBootTest(classes = com.unisharing.hoola.hoolaredis.configuration.RedisConfig.class)
public class HoolaRedisApplicationTests {
	@Autowired
    @Qualifier("userRedisTemplate")
    private RedisTemplate userRedisTemplate;
	@Test
	public void contextLoads() {
	}
    @Test
    public void testSet() {
        userRedisTemplate.boundValueOps("test").set("bad");
	    String str = (String) userRedisTemplate.boundValueOps("lqn").get();
	    System.out.println(str);
    }

}
