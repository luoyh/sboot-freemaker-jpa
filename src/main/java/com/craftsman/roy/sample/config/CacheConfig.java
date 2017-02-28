package com.craftsman.roy.sample.config;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Conditional(StarterCacheCondition.class)
public class CacheConfig {

	@Value("${springext.cache.redis.topic:cache}")
	String topicName;

	@Bean
	public MyRedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
		MyRedisCacheManager cacheManager = new MyRedisCacheManager(redisTemplate);
		cacheManager.setUsePrefix(true);
		return cacheManager;
	}

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(topicName));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MyRedisCacheManager cacheManager) {
		return new MessageListenerAdapter(new MessageListener() {

			@Override
			public void onMessage(Message message, byte[] pattern) {
				byte[] bs = message.getChannel();
				try {
					String type = new String(bs, "UTF-8");
					cacheManager.receiver(type);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					// 不可能出错
				}

			}

		});
	}

	class MyRedisCacheManager extends RedisCacheManager {

		@SuppressWarnings("rawtypes")
		public MyRedisCacheManager(RedisOperations redisOperations) {
			super(redisOperations);

		}

		@SuppressWarnings("unchecked")
		@Override
		protected RedisCache createCache(String cacheName) {
			long expiration = computeExpiration(cacheName);
			return new MyRedisCache(this, cacheName,
					(this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null), this.getRedisOperations(),
					expiration);
		}

		/**
		 * get a messsage for update cache
		 * 
		 * @param cacheName
		 */
		public void receiver(String cacheName) {
			MyRedisCache cache = (MyRedisCache) this.getCache(cacheName);
			if (cache == null) {
				return;
			}
			cache.cacheUpdate();

		}

		// notify other redis clent to update cache( clear local cache in fact)
		public void publishMessage(String cacheName) {
			this.getRedisOperations().convertAndSend(topicName, cacheName);
		}

	}

	class MyRedisCache extends RedisCache {
		// local cache for performace
		ConcurrentHashMap<Object, ValueWrapper> local = new ConcurrentHashMap<>();
		MyRedisCacheManager cacheManager;

		public MyRedisCache(MyRedisCacheManager cacheManager, String name, byte[] prefix,
				RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
			super(name, prefix, redisOperations, expiration);
			this.cacheManager = cacheManager;
		}

		@Override
		public ValueWrapper get(Object key) {
			ValueWrapper wrapper = local.get(key);
			if (wrapper != null) {
				return wrapper;
			} else {
				wrapper = super.get(key);
				if (wrapper != null) {
					local.put(key, wrapper);
				}

				return wrapper;
			}

		}

		@Override
		public void put(final Object key, final Object value) {

			super.put(key, value);
			cacheManager.publishMessage(super.getName());
		}

		@Override
		public void evict(Object key) {
			super.evict(key);
			cacheManager.publishMessage(super.getName());
		}

		@Override
		public ValueWrapper putIfAbsent(Object key, final Object value) {
			ValueWrapper wrapper = super.putIfAbsent(key, value);
			cacheManager.publishMessage(super.getName());
			return wrapper;
		}

		public void cacheUpdate() {
			// clear all cache for simplification
			local.clear();
		}

	}

}

class StarterCacheCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "springext.cache.");

		String env = resolver.getProperty("type");
		if (env == null) {
			return false;
		}
		return "local2redis".equalsIgnoreCase(env.toLowerCase());

	}

}