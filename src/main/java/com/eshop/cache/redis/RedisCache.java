package com.eshop.cache.redis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.ShardedJedis;

import com.eshop.cache.SessionCache;


public class RedisCache implements SessionCache {
	ShardedJedis jedis;

	@SuppressWarnings("unchecked")
	public HashMap<String, Serializable> getSession(String root) {
		jedis = RedisPool.getResource();
		HashMap<String, Serializable> session = (HashMap<String, Serializable>) jedis.hmget(root);
		RedisPool.returnResource(jedis);
		return session;
	}

	public void setSession(String root, Map<String, Serializable> session, int exp) {
		jedis = RedisPool.getResource();
		//jedis.hmset(root, session);
		jedis.expire(root, exp * 60);
		RedisPool.returnResource(jedis);
	}

	public Serializable getAttribute(String root, String name) {
		HashMap<String, Serializable> session = getSession(root);
		return session != null ? session.get(name) : null;
	}

	public void setAttribute(String root, String name, Serializable value, int exp) {
		jedis = RedisPool.getResource();
		HashMap<String, Serializable> session = getSession(root);
		if (session == null) {
			session = new HashMap<String, Serializable>();
		}
		session.put(name, value);
		//jedis.hmset(root, session);
		jedis.expire(root, exp * 60);
		RedisPool.returnResource(jedis);
	}

	public void clear(String root) {
		jedis = RedisPool.getResource();
		jedis.del(root);
		RedisPool.returnResource(jedis);
	}

	public boolean exist(String root) {
		jedis = RedisPool.getResource();
		boolean exist = jedis.get(root) != null;
		RedisPool.returnResource(jedis);
		return exist;
	}

}
