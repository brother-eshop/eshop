package com.eshop.cache.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Properties {
	private static Logger log = LoggerFactory.getLogger(Properties.class);
	private static final Map<String, Map<String, String>> propertiesMap = new HashMap<String, Map<String, String>>();
	/**
	 * is init
	 */
	private static boolean isInit = false;
	/**
	 *
	 */
	private static final String PROPERTIES_INDEX_NAME = "/project.properties";
	/**
	 * 属性文件索引key
	 */
	private static final String INDEX_KEY = "index";

	/**
	 *
	 * 初始化怎个属性索引文件
	 *
	 * @return:void
	 */
	public static boolean init() {
		java.util.Properties properties = new java.util.Properties();
		try {
			InputStream is = FileUtils.getClassInputStream(Properties.class,
					PROPERTIES_INDEX_NAME);
			if (is == null) {
				log.warn("not found properties file");
				return false;
			}
			properties.load(is);
			String indexs = properties.getProperty(INDEX_KEY);
			if (indexs == null) {
				log.warn("not found properties index values");
				return false;
			}
			String[] files = indexs.split(",");
			for (String file : files) {
				is = FileUtils.getClassInputStream(Properties.class, "/" + file
						+ ".properties");
				if (is == null) {
					log.warn(file + "-----file not founds");
					continue;
				}
				properties.clear();
				properties.load(is);
				HashMap<String, String> map = new HashMap<String, String>();
				Set<Entry<Object, Object>> sets = properties.entrySet();
				for (Entry<Object, Object> en : sets) {
					if (en.getKey() == null) {
						continue;
					}
					map.put(en.getKey().toString(), en.getValue().toString());
				}
				propertiesMap.put(file, map);
			}
			log.info("init properties index is success");
			isInit = true;
			return true;
		} catch (IOException e) {
			log.warn("init properties index is error");

		}
		return false;
	}

	public static void destory() {
		propertiesMap.clear();
		isInit = false;
	}

	public static boolean reLoad() {
		destory();
		return init();
	}

	/**
	 *
	 * 通过文件获取相应的属性值
	 *
	 * @return:String
	 */
	public static String getProperties(String file, String key) {
		if (!isInit) {
			init();
		}
		Map<String, String> property = propertiesMap.get(file);
		if (property == null) {
			return null;
		}
		String value = property.get(key);
		return value != null ? value.trim() : value;
	}

	/**
	 *
	 *
	 *
	 * @return:Map<String,String>
	 */
	public static Map<String, String> getProperties(String file) {
		return propertiesMap.get(file);
	}
}
