package com.eshop.cache.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.eshop.cache.memcached.MemcachedSessionProvider;

public class RequestUtils {
	public static Map<String, String[]> parseQueryString(String s) {
		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = (String) st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (ht.containsKey(key)) {
				String oldVals[] = (String[]) ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++) {
					valArray[i] = oldVals[i];
				}
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}
		return ht;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getRequestMap(HttpServletRequest request, String prefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				request.getParameterValues(name);
				map.put(name.substring(prefix.length()), StringUtils.join(request.getParameterValues(name), ','));
			}
		}
		return map;
	}

	/**
	 * 获取访问者IP
	 *
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。
	 * 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 *
	 * @see HttpServletRequest#getRequestedSessionId()
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {

		// 手动从cookie获取
		Cookie cookie = CookieUtils.getCookie(request, MemcachedSessionProvider.JSESSION_COOKIE);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			cookie = CookieUtils.getCookie(request, "JSESSIONID");
			if (cookie != null) {
				return cookie.getValue();
			}
			return null;
		}
	}

}
