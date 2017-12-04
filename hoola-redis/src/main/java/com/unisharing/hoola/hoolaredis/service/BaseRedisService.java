package com.unisharing.hoola.hoolaredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

public class BaseRedisService {
	/**
	 * 内容ContentTemplate
	 */
	@Autowired
    @Qualifier(value = "contentTemplate")
	protected RedisTemplate contentTemplate;

	/**
	 * 用户ContentTemplate
	 */
    @Autowired
    @Qualifier(value = "userTemplate")
	protected RedisTemplate userTemplate;
	
	/**
	 * 关系Template
	 */
    @Autowired
    @Qualifier(value = "relationTemplate")
	protected RedisTemplate relationTemplate;
	
	/**
	 * 搜索template
	 */
    @Autowired
    @Qualifier(value = "reportTemplate")
	protected RedisTemplate reportTemplate;
	
	/**
	 * 消息
	 */
    @Autowired
    @Qualifier(value = "jmsTemplate")
	protected RedisTemplate jmsTemplate;
	
	/**
	 * 用户关系数据
	 */
    @Autowired
    @Qualifier(value = "userRelationTemplate")
	protected RedisTemplate userRelationTemplate;
	/**
	 * 评论数据
	 */
    @Autowired
    @Qualifier(value = "commentTemplate")
	protected RedisTemplate commentTemplate;
	
	/**
	 * 消息私信数据
	 */
    @Autowired
    @Qualifier(value = "messageTemplate")
	protected RedisTemplate messageTemplate;
	
	/**
	 * 第三方平台用户信息
	 */
    @Autowired
    @Qualifier(value = "openUserTemplate")
	protected RedisTemplate openUserTemplate;
	
	
	
	public void setContentTemplate(RedisTemplate contentTemplate) {
		this.contentTemplate = contentTemplate;
	}

	public void setUserTemplate(RedisTemplate userTemplate) {
		this.userTemplate = userTemplate;
	}

	public void setRelationTemplate(RedisTemplate relationTemplate) {
		this.relationTemplate = relationTemplate;
	}

	public RedisTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(RedisTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public void setJmsTemplate(RedisTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setUserRelationTemplate(RedisTemplate userRelationTemplate) {
		this.userRelationTemplate = userRelationTemplate;
	}
	
	public void setCommentTemplate(RedisTemplate commentTemplate) {
		this.commentTemplate = commentTemplate;
	}
	public void setMessageTemplate(RedisTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	public DefaultStringRedisConnection getDefaultStringRedisConnection(){
		return new DefaultStringRedisConnection(relationTemplate.getConnectionFactory().getConnection());
	}

	public void setOpenUserTemplate(RedisTemplate openUserTemplate) {
		this.openUserTemplate = openUserTemplate;
	}


}
