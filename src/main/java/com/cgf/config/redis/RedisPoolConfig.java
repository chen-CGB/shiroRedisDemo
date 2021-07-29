package com.cgf.config.redis;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/***
 * @author cgf
 * @Description redis连接池配置
 * @date 2021/4/29
 */
@Configuration
public class RedisPoolConfig {

    //最大连接数
    @Value("${redis.pool.maxTotal:100}")
    private Integer maxTotal;

    //最大空闲连接数
    @Value("${redis.pool.maxIdle:5}")
    private Integer maxIdle;

    @Value("${redis.shard.timeout:5000}")
    private Integer timeout;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.pool.maxWaitMillis:60000}")
    private Integer maxWaitMillis;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.database:0}")
    private Integer database;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        return  jedisPoolConfig;
    }

    @Bean
    public RedisManager redisManager(JedisPoolConfig jedisPoolConfig) {
        RedisManager redisManager = new RedisManager();
        redisManager.setJedisPoolConfig(jedisPoolConfig);
        redisManager.setHost(host);
        redisManager.setPort(port);
        if (!StringUtils.isEmpty(password)) {
            redisManager.setPassword(password);
        }
        redisManager.setDatabase(database);
        redisManager.setTimeout(timeout);
        return redisManager;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory (JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(host);
        if (!StringUtils.isEmpty(password)) {
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setDatabase(database);
        return jedisConnectionFactory;
    }

    @Bean
    public HessianRedisTemplate hessianRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        HessianRedisTemplate hessianRedisTemplate = new HessianRedisTemplate();
        hessianRedisTemplate.setKeySerializer(new StringRedisSerializer());
        hessianRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        hessianRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        hessianRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        hessianRedisTemplate.setEnableTransactionSupport(true);
        hessianRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        hessianRedisTemplate.afterPropertiesSet();
        return  hessianRedisTemplate;
    }
}
