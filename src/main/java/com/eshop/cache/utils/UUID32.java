package com.eshop.cache.utils;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * UUID 主键的生成
 * 
 */
public class UUID32 {
	private static Logger log = LoggerFactory.getLogger(UUID32.class);
	private static LinkedBlockingQueue<String> uuids = new LinkedBlockingQueue<String>();
	private static final int UUID_CACHE_NUMBERS = 100;

	/**
	 * 
	 * 
	 * @return:String
	 */
	public static String get() {
		String uuid = uuids.poll();
		if (uuid == null) {
			uuid = createUUID();
			createPool();
		}
		return uuid;
	}

	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replace("-", "");
		return id;
	}

	public static void createPool() {
		for (int i = 0; i < UUID_CACHE_NUMBERS; i++) {
			try {
				uuids.put(UUID32.createUUID());
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}
		log.info("uuid pool create success");
	}
}
