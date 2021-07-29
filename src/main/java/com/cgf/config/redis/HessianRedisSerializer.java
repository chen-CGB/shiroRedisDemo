package com.cgf.config.redis;

import com.jarvis.cache.serializer.HessianSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/***
 * @author cgf
 * @Description 重写redis序列化，提供自定义报错
 * @date 2021/4/29
 */
public class
HessianRedisSerializer implements RedisSerializer<Object> {

    private static HessianSerializer hessianSerializer = new HessianSerializer();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        try {
            return hessianSerializer.serialize(o);
        } catch (Exception e) {
            throw new SerializationException("",e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            return hessianSerializer.deserialize(bytes,null);
        } catch (Exception e) {
            throw new SerializationException("",e);
        }
    }
}
