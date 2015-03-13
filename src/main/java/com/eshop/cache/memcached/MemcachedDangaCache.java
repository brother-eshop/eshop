package com.eshop.cache.memcached;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.eshop.cache.SessionCache;

public class MemcachedDangaCache implements SessionCache {
	
	private MemcachedClient client;

	public HashMap<String, Serializable> getSession(String root) {
		HashMap<String, Serializable> session = null;
		try {
			session = client.get(root);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
		}
		return session;
	}

	public void setSession(String root, Map<String, Serializable> session,
			int exp) {
		if (exist(root)) {
			try {
				client.replaceWithNoReply(root, exp, session);
			} catch (InterruptedException | MemcachedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				client.add(root, exp, session);
			} catch (TimeoutException | InterruptedException
					| MemcachedException e) {
				e.printStackTrace();
			}
		}
	}

	public Serializable getAttribute(String root, String name) {
		HashMap<String, Serializable> session = getSession(root);
		return session != null ? session.get(name) : null;
	}

	public void setAttribute(String root, String name, Serializable value,
			int exp) {
		HashMap<String, Serializable> session = getSession(root);
		if (session == null) {
			session = new HashMap<String, Serializable>();
		}
		session.put(name, value);
		try {
			client.set(root, exp, session);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
		}
	}

	public void clear(String root) {
		try {
			client.delete(root);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}

	public boolean exist(String root) {
		boolean existed = false;
		try {
			existed = client.get(root) != null;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return existed;
	}


	public void setClient(MemcachedClient client) {
		this.client = client;
	}
}
