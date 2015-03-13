package com.eshop.cache.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	// ---1天过期
	private static final int COOKIE_USER_TIME = 3600 * 24;
	private static final String COOKIE_DOMAIN = ".yunmar.com.cn";
	public static final String COOKIE_KEY = "^%$#@!yunma";
	public static final String USER_INFO = "yunmar_userInfo";
	public static final String USER_NAME = "yunmar_userName";
	public static final String USER_MOBILE = "yunmar_userMobile";
	public static final String KEY = "yunmar_key";

	/**
	 * 
	 * 将cookie转化为map
	 * 
	 * @return:Map<String,Object>
	 */
	public static Map<String, Object> getMaps(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());

			}
		}
		return map;
	}

	/**
	 * 
	 * 清除本站所有cookie
	 * 
	 * @return:void
	 */
	public static void clear(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setDomain(COOKIE_DOMAIN);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	/**
	 * 
	 * 获取cookie的某一个值
	 * 
	 * @return:Object
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// return URLDecoder.decode(cookie.getValue());
				}
			}
		}
		return null;
	}

	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static void remove(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		cookie.setDomain(COOKIE_DOMAIN);
		response.addCookie(cookie);
	}

	public static void add(HttpServletResponse rsp, Cookie cookie) {
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setPath("/");
		cookie.setMaxAge(COOKIE_USER_TIME);
		rsp.addCookie(cookie);
	}

	public static void del(HttpServletResponse rsp, Cookie cookie) {
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setPath("/");
		rsp.addCookie(cookie);
	}

	public static void add(HttpServletResponse rsp, Cookie cookie, int maxTime) {
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setPath("/");
		cookie.setMaxAge(maxTime);
		rsp.addCookie(cookie);
	}

}
