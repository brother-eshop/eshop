package com.eshop.cache.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eshop.cache.SessionProvider;

public class Sessions implements SessionProvider {
	public Serializable getAttribute(HttpServletRequest request, String name) {
		return (Serializable) request.getSession().getAttribute(name);
	}

	public void setAttribute(HttpServletRequest request,
			HttpServletResponse response, String name, Serializable value) {
		request.getSession().setAttribute(name, value);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
	}

	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		return request.getSession(true).getId();
	}

}
