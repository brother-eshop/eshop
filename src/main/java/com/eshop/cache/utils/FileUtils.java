package com.eshop.cache.utils;

import java.io.InputStream;

public class FileUtils {
	public static InputStream getClassInputStream(Class<?> clazz, String path) {
		InputStream is = clazz.getResourceAsStream(path);
		if (is == null) {
			is = Thread.currentThread().getClass().getResourceAsStream(path);
		}
		return is;
	}
}
