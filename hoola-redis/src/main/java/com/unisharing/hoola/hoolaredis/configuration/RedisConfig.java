package com.unisharing.hoola.hoolaredis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:redis/redis.properties")
public class RedisConfig {
    // Jredis pool config
    @Value("${spring.redis.maxIdle}")
    private int maxIdle;

    @Value("${spring.redis.maxActive}")
    private int maxActive;

    @Value("${spring.redis.maxWait}")
    private long maxWait;
    @Value("${spring.redis.host}")
    private String hostName;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(hostName);

        return jedisConnectionFactory;
    }
    @Bean
    public RedisTemplate userTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate userRedisTemplate = new RedisTemplate();
        userRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        userRedisTemplate.setKeySerializer(new StringRedisSerializer());
        userRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  userRedisTemplate;
    }
    @Bean
    public RedisTemplate commentTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate commentRedisTemplate = new RedisTemplate();
        commentRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        commentRedisTemplate.setKeySerializer(new StringRedisSerializer());
        commentRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  commentRedisTemplate;
    }

    @Bean
    public RedisTemplate contentTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate contentRedisTemplate = new RedisTemplate();
        contentRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        contentRedisTemplate.setKeySerializer(new StringRedisSerializer());
        contentRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  contentRedisTemplate;
    }
    @Bean
    public RedisTemplate relationTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate relationRedisTemplate = new RedisTemplate();
        relationRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        relationRedisTemplate.setKeySerializer(new StringRedisSerializer());
        relationRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  relationRedisTemplate;
    }


    @Bean
    public RedisTemplate reportTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate reportRedisTemplate = new RedisTemplate();
        reportRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        reportRedisTemplate.setKeySerializer(new StringRedisSerializer());
        reportRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  reportRedisTemplate;
    }

    @Bean
    public RedisTemplate jmsTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate jmsRedisTemplate = new RedisTemplate();
        jmsRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        jmsRedisTemplate.setKeySerializer(new StringRedisSerializer());
        jmsRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  jmsRedisTemplate;
    }

    @Bean
    public RedisTemplate userRelationTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate userRelationRedisTemplate = new RedisTemplate();
        userRelationRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        userRelationRedisTemplate.setKeySerializer(new StringRedisSerializer());
        userRelationRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  userRelationRedisTemplate;
    }
    @Bean
    public RedisTemplate messageTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate messageRedisTemplate = new RedisTemplate();
        messageRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        messageRedisTemplate.setKeySerializer(new StringRedisSerializer());
        messageRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  messageRedisTemplate;
    }

    @Bean
    public RedisTemplate openUserTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate openUserRedisTemplate = new RedisTemplate();
        openUserRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        openUserRedisTemplate.setKeySerializer(new StringRedisSerializer());
        openUserRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return  openUserRedisTemplate;
    }





}
