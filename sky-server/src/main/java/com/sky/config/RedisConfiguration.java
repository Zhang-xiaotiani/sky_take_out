package com.sky.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RedisConfiguration.java
 * @Description Redis配置类
 * @createTime 2023年07月23日 14:34:00
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建Redis模板对象..");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置redis 的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        设置Redis key 的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
