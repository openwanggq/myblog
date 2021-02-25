package cn.dblearn.blog.common.util;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.config.BaseCacheConfiguration;
import org.ehcache.expiry.ExpiryPolicy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtils
 *
 * @author bobbi
 * @date 2018/10/19 21:51
 * @email 571002217@qq.com
 * @description redis工具类
 */
@Component
public class RedisUtils {


    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

    Cache<String, String> preConfigured;
    Cache<String, String> myCache;


    @PostConstruct
    public void init() {
//        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("preConfigured", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
//                        ResourcePoolsBuilder.heap(100))
//                        .withExpiry(ExpiryPolicyBuilder.expiry().create(Duration.ofHours(1)).build())
//                        .build())
//                .withCache("gao", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
//                        ResourcePoolsBuilder.heap(100))
//                        .withExpiry(ExpiryPolicyBuilder.expiry().create(Duration.ofMinutes(1)).build())
//                        .build())
//                .build(true);
//        cacheManager.init();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(ExpiryPolicyBuilder.expiry().create(Duration.ofHours(4)).build()))
                .build();
        cacheManager.init();
        preConfigured = cacheManager.getCache("preConfigured", String.class, String.class);

        myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
                .withExpiry(ExpiryPolicyBuilder.expiry().create(Duration.ofMinutes(5)).build()));

//        myCache.put(1L, "da one!");
//        String value = myCache.get(1L);
    }

    /**
     * 设置值与过期时间
     *
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, Object value, long expire) {
        preConfigured.put(key, JsonUtils.toJson(value));
//        cache.put(key, JsonUtils.toJson(value));
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
    }

    /**
     * 设置值，默认过期时间1天
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 获取对象，同时设置过期时间
     *
     * @param key
     * @param clazz
     * @param expire
     * @param <T>
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz, long expire) {
        String value = preConfigured.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
        return value == null ? null : JsonUtils.toObj(value, clazz);
    }

    /**
     * 获取对象，不设置过期时间
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz) {
        return getObj(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取值，同时设置过期时间
     *
     * @param key
     * @param expire
     * @return
     */
    public String get(String key, long expire) {
        String value = preConfigured.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
        return value;
    }

    /**
     * 获取值，不设置过期时间
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * 删除
     *
     * @param key
     */
    public void delete(String key) {
        preConfigured.remove(key);
    }

    /**
     * 更新过期时间
     *
     * @param key
     */
    public void updateExpire(String key) {
//        preConfigured.expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        preConfigured.replace(key, preConfigured.get(key));
    }


}
