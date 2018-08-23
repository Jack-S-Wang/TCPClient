package com.dascom.operation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/** * @author  作者 E-mail: 
* @date 创建时间：2018年8月24日 上午6:28:04 
* @version 1.0 
* @parameter  
* @since  
* @return  */

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	
	private static final Logger logger = LogManager.getLogger(RedisConfig.class);

	@Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;
    
    @Bean
    public JedisPool redisPoolFactory() {
    	logger.info("------JedisPool注入成功！------");
    	logger.info("------redis地址:"+host+":"+port+"------");
    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    	jedisPoolConfig.setMaxIdle(maxIdle);
    	jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
    	JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout);
    	return jedisPool;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
