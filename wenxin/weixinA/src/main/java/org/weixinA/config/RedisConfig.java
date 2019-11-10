package org.weixinA.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.weixinA.config.util.MD5Kit;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.cacheName}")
	public String cacheNamespace;

	public String getCacheNamespace() {
		return cacheNamespace;
	}

	public void setCacheNamespace(String cacheNamespace) {
		this.cacheNamespace = cacheNamespace;
	}

	/**
	 * 默认过期时间为两个小时
	 */
	private static final int TTL = 2 * 60 * 60;

	/*
	 * @Bean public DistributedLock redisDistributedLock(RedisTemplate<String,
	 * String> redisTemplate){ return new RedisDistributedLock(redisTemplate); }
	 */

	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, objects) -> {
			StringBuilder sb = new StringBuilder(cacheNamespace);
			CacheConfig cacheConfig = target.getClass().getAnnotation(CacheConfig.class);
			Cacheable cacheable = method.getAnnotation(Cacheable.class);
			CachePut cachePut = method.getAnnotation(CachePut.class);
			CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
			if (cacheable != null && !StringUtils.isEmpty(cacheable.key())) {
				sb.append(":");
				sb.append(cacheable.key());
			} else {
				if (cacheable != null) {
					String[] cacheNames = cacheable.value();
					if (cacheNames != null && cacheNames.length > 0) {
						sb.append(cacheNames[0]);
						sb.append(":");
					}
				} else if (cachePut != null) {
					String[] cacheNames = cachePut.value();
					if (cacheNames != null && cacheNames.length > 0) {
						sb.append(cacheNames[0]);
						sb.append(":");
					}
				} else if (cacheEvict != null) {
					String[] cacheNames = cacheEvict.value();
					if (cacheNames != null && cacheNames.length > 0) {
						sb.append(cacheNames[0]);
						sb.append(":");
					}
				} else if (cacheConfig != null) {
					String[] cacheNames = cacheConfig.cacheNames();
					if (cacheNames != null && cacheNames.length > 0) {
						sb.append(cacheNames[0]);
						sb.append(":");
					}
				} else {
					sb.append(target.getClass().getSimpleName());
					sb.append(":");
				}
				sb.append(method.getName());
				sb.append(":");
				if (objects != null) {
					if (objects.length == 1) {
						if (objects[0] instanceof Number || objects[0] instanceof String
								|| objects[0] instanceof Boolean) {
							sb.append(objects[0]);
						} else {
							try {
								sb.append(objects[0].getClass().getMethod("getId").invoke(objects[0]));
							} catch (Exception e) {
								if (objects[0] instanceof Map && ((Map<?, ?>) objects[0]).get("id") != null) {
									sb.append(((Map<?, ?>) objects[0]).get("id"));
								} else {
									sb.append(MD5Kit.md5(JSON.toJSONString(objects[0])));
								}
							}
						}
					} else {
						sb.append(MD5Kit.md5(StringUtils.join(objects, ",")));
					}
				}
			}
			return sb.toString();
		};
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		defaultCacheConfig.entryTtl(Duration.of(TTL, ChronoUnit.SECONDS));
		RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
		return cacheManager;
	}

	@Bean
	public RedisSerializer<Object> redisSerializer() {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;
	}

	@Bean
	@Primary
	public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.setValueSerializer(redisSerializer());
		template.afterPropertiesSet();
		return template;
	}

}