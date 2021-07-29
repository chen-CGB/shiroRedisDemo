package com.cgf.config.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/***
 * @author cgf
 * @Description redis模板工具
 * @date 2021/4/29
 */
public class HessianRedisTemplate extends RedisTemplate<String, Object>{

    public HessianRedisTemplate(){
        RedisSerializer<String> stringRedisSerializer = this.getStringSerializer();
        HessianRedisSerializer hessianRedisSerializer = new HessianRedisSerializer();
        this.setKeySerializer(stringRedisSerializer);
        this.setValueSerializer(hessianRedisSerializer);
        this.setHashKeySerializer(stringRedisSerializer);
        this.setHashValueSerializer(hessianRedisSerializer);
        this.setEnableTransactionSupport(true);
    }

    public HessianRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        this();
        this.setConnectionFactory(jedisConnectionFactory);
        this.afterPropertiesSet();
    }


    /**
     * @Author cgf
     * @Description //TODO string类型获取对应的自增值
     * @Date 21:48 2021/5/9
     **/
    public long getIncrValue(final String key){
        return this.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = HessianRedisTemplate.this.getStringSerializer();
            byte[] rowkey = serializer.serialize(key);
            byte[] rowval = connection.get(rowkey);
            String val = serializer.deserialize(rowval);
            return  Long.parseLong(val);
        });
    }

    /**
     * @Author cgf
     * @Description //TODO hash类型获取对应的自增值
     * @Date 21:48 2021/5/9
     **/
    public Long getIncrValue(final String key, final String hashKey) {

        return this.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = HessianRedisTemplate.this.getStringSerializer();
            byte[] rowkey = serializer.serialize(key);
            byte[] rowhashKey = serializer.serialize(hashKey);
            byte[] rowval = connection.hGet(rowkey, rowhashKey);
            String val = serializer.deserialize(rowval);
            return Long.parseLong(val);
        });
    }
}
