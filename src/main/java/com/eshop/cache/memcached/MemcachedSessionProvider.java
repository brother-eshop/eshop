package com.eshop.cache.memcached;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.eshop.cache.SessionCache;
import com.eshop.cache.SessionProvider;
import com.eshop.cache.utils.CookieUtils;
import com.eshop.cache.utils.FileUtils;
import com.eshop.cache.utils.RequestUtils;
import com.eshop.cache.utils.UUID32;


public class MemcachedSessionProvider implements SessionProvider, InitializingBean {
	public static final String CURRENT_SESSION = "_current_session";
	public static final String CURRENT_SESSION_ID = "_current_session_id";
	public static final String JSESSION_COOKIE = "MEM_JSESSIONID";
	private SessionCache sessionCache;
	private Boolean useMemcached = true;
	private int sessionTimeout = 10080;

	@SuppressWarnings("unchecked")
	public Serializable getAttribute(HttpServletRequest request, String name) {
		if (useMemcached) {
			// 为了避免同一个请求多次获取缓存session，所以将缓存session保存至request中。
			Map<String, Serializable> session = (Map<String, Serializable>) request.getAttribute(CURRENT_SESSION);
			if (session != null) {
				return session.get(name);
			}

			String root = (String) request.getAttribute(CURRENT_SESSION_ID);
			if (root == null) {
				root = RequestUtils.getRequestedSessionId(request);
			}
			if (StringUtils.isBlank(root)) {
				request.setAttribute(CURRENT_SESSION, new HashMap<String, Serializable>());
				return null;
			}
			session = sessionCache.getSession(root);
			if (session != null) {
				request.setAttribute(CURRENT_SESSION_ID, root);
				request.setAttribute(CURRENT_SESSION, session);
				return session.get(name);
			} else {
				return null;
			}
		} else {
			return (Serializable) request.getSession().getAttribute(name);
		}
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name, Serializable value) {
		if (useMemcached) {
			Map<String, Serializable> session = (Map<String, Serializable>) request.getAttribute(CURRENT_SESSION);
			String root;
			if (session == null) {
				root = RequestUtils.getRequestedSessionId(request);
				if (root != null && root.length() == 32) {
					session = sessionCache.getSession(root);
				}
				if (session == null) {
					session = new HashMap<String, Serializable>();
					do {
						root = UUID32.get();
					} while (sessionCache.exist(root));
					response.addCookie(createCookie(response, root));
				}
				request.setAttribute(CURRENT_SESSION, session);
				request.setAttribute(CURRENT_SESSION_ID, root);
			} else {
				root = (String) request.getAttribute(CURRENT_SESSION_ID);
				if (root == null) {
					do {
						root = UUID32.get();
					} while (sessionCache.exist(root));
					response.addCookie(createCookie(response, root));
					request.setAttribute(CURRENT_SESSION_ID, root);
				}
			}
			session.put(name, value);
			sessionCache.setSession(root, session, sessionTimeout);
		} else {
			request.getSession().setAttribute(name, value);
		}
	}

	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		String root = (String) request.getAttribute(CURRENT_SESSION_ID);
		if (root != null) {
			return root;
		}
		root = RequestUtils.getRequestedSessionId(request);
		if (root == null || root.length() != 32 || !sessionCache.exist(root)) {
			do {
				root = UUID32.get();
			} while (sessionCache.exist(root));
			sessionCache.setSession(root, new HashMap<String, Serializable>(), sessionTimeout);
			response.addCookie(createCookie(response, root));
		}
		request.setAttribute(CURRENT_SESSION_ID, root);
		return root;
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		if (useMemcached) {
			request.removeAttribute(CURRENT_SESSION);
			request.removeAttribute(CURRENT_SESSION_ID);
			String root = RequestUtils.getRequestedSessionId(request);
			if (!StringUtils.isBlank(root)) {
				sessionCache.clear(root);
				// Cookie cookie = createCookie(response, null);
				// cookie.setMaxAge(0);
				// response.addCookie(cookie);
				Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					CookieUtils.remove(response, cookie.getName());
				}
			}
		} else {
			request.getSession().invalidate();
		}
	}

	private Cookie createCookie(HttpServletResponse response, String value) {
		Cookie cookie = new Cookie(JSESSION_COOKIE, value);
		CookieUtils.add(response, cookie,60*sessionTimeout);
		return cookie;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(sessionCache);
		InputStream is = FileUtils.getClassInputStream(MemcachedSessionProvider.class, "/project.properties");
		java.util.Properties properties = new java.util.Properties();
		properties.load(is);
		String session = properties.getProperty("session");
		this.sessionTimeout = (session != null && !session.equals("")) ? Integer.parseInt(session) : 30;
		Boolean usecached = Boolean.valueOf(properties.getProperty("useMemcached"));
		if (usecached != null) {
			useMemcached = usecached;
		}
	}

	public void setSessionCache(SessionCache sessionCache) {
		this.sessionCache = sessionCache;
	}

	public void setUseMemcached(Boolean useMemcached) {
		this.useMemcached = useMemcached;
	}

	/**
	 * 设置session过期时间
	 * @param sessionTimeout
	 *            分钟
	 */
	public void setSessionTimeout(int sessionTimeout) {
		Assert.isTrue(sessionTimeout > 0);
		this.sessionTimeout = sessionTimeout;
	}
}
