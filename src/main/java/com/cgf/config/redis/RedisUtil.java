package com.cgf.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/***
 * @author cgf
 * @Description redis操作工具类
 * @date 2021/4/29
 */
@Slf4j
@Component
public class RedisUtil{

    private static HessianRedisTemplate hessianRedisTemplate;

    @Autowired
    private HessianRedisTemplate redisTemplate;

    @PostConstruct
    public void beforeInit(){
        hessianRedisTemplate = redisTemplate;
    }

    /**
     * @Description: 设置过期时间
     * @author cgf
     * @date 2021/4/13 10:05
    */
    public static boolean expire(String key, long timeout) {
        try {
            if (timeout > 0){
                hessianRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Set<Object> getHashValues(String key){
        return hessianRedisTemplate.opsForSet().members(key);
    }

    public static Long pushHashValue(String key, Object value) {
        return hessianRedisTemplate.opsForSet().add(key, value);
    }

    public static Long rmHashValue(String key, Object value) {
        return hessianRedisTemplate.opsForSet().remove(key, value);
    }

    public static Long getExpire(String key){
        return hessianRedisTemplate.getExpire(key);
    }

    public static boolean hasKey(String key) {
        try {
            return hessianRedisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean set(String key, Object value) {
        try {
            hessianRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            hessianRedisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        try {
            hessianRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getAndSet(String key, Object value, long timeout, TimeUnit unit) {
        try {
            hessianRedisTemplate.opsForValue().getAndSet(key, value).toString();
            if (timeout > 0) {
                hessianRedisTemplate.expire(key, timeout, unit);
            }
            return value.toString();
        } catch (Exception e) {
            return "0";
        }
    }

    public static Long increment(String key) {
        try {
            return hessianRedisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Object get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return hessianRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取Hash key对应的item的值
     *
     * @param key  键，不能为空
     * @param item 项，不能为空
     * @return
     */
    public static Object hget(String key, String item) {
        return hessianRedisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取Hash key对应的所有键值
     *
     * @param key 键，不能为空
     * @return
     */
    public static Map<Object, Object> hgetMap(String key) {
        return hessianRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置Hash key对应的map
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public static boolean hsetMap(String key, Map<String, Object> map) {
        try {
            hessianRedisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置Hash key对应的map并且有效时间为time
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 有效时间(秒)
     * @return
     */
    public static boolean hsetMapTime(String key, Map<String, Object> map, long time) {
        try {
            hessianRedisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置Hash key对应的键值key和value
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            hessianRedisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置Hash key对应的键值key和value，并且有效时间为time
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  有效时间(秒)
     * @return
     */
    public static boolean hset(String key, String item, Object value, long time) {
        try {
            hessianRedisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除Hash中key对应的item项
     *
     * @param key  键，不能为空
     * @param item 项，可以多个，不能为空
     * @return
     */
    public static boolean hdel(String key, Object... item) {
        try {
            hessianRedisTemplate.opsForHash().delete(key, item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hash中对应key是否有这个item
     *
     * @param key  键，不能为空
     * @param item 项，不能为空
     * @return
     */
    public static boolean hHasKey(String key, String item) {
        return hessianRedisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 模糊查询key
     */
    public static Set<String> keys(String key) {
        return hessianRedisTemplate.keys(key);
    }

    /**
     * 删除keys
     */
    public static boolean delete(Set<String> keys) {
        try {
            hessianRedisTemplate.delete(keys);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除keys
     */
    public static boolean delete(String keys) {
        try {
            if (keys.contains("*")) {
                delete(keys(keys));
            } else {
                hessianRedisTemplate.delete(keys);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean pipeline(List<Map<String, Object>> list, String prefix, String key, String value) {
        //批量set数据
        hessianRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (Map<String, Object> map : list) {
                String redisKey = prefix + map.get(key).toString();
                String redisValue = map.get(value).toString();
                connection.set(redisKey.getBytes(), redisValue.getBytes());
            }
            return null;
        });
        return true;
    }


    private static void resetTemplate() {
        hessianRedisTemplate = new HessianRedisTemplate(new JedisConnectionFactory());
    }
}
