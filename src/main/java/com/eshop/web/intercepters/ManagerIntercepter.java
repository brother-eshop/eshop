package com.eshop.web.intercepters;

import java.io.Writer;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eshop.cache.SessionProvider;
import com.eshop.common.constant.CoreConstant;
import com.eshop.model.mongodb.EUser;
import com.google.gson.JsonObject;

public class ManagerIntercepter extends HandlerInterceptorAdapter {

	private static String redirect_login = "/login";// 登录页面

	public String[] allowUrls;// 允许例外的url

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Autowired
	protected SessionProvider sessionProvider;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equals(header) ? true : false;
		return isAjax;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//		boolean flag = super.preHandle(request, response, handler);
		// 访问的路径
		String invokeUrl = request.getServletPath();
		// 先判断是否是允许例外的url
		if (null != allowUrls && allowUrls.length >= 1) {
			for (String url : allowUrls) {
				if (url.contains(invokeUrl)) {
					return true;
				}
			}
		}
		EUser user= (EUser) sessionProvider.getAttribute(request, CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			String succURL = URLEncoder.encode(request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""),
					request.getCharacterEncoding());
			if (isAjaxRequest(request)) {
				JsonObject json = new JsonObject();
				json.addProperty("success", false);
				json.addProperty("succ", false);
				json.addProperty("msg", false);
				json.addProperty("message", false);
				Writer out = response.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();
			} else {
				succURL = succURL.replace("%2F"+CoreConstant.propertyUtil.getProperty("projectName")+"%2F", "%2F");
				response.sendRedirect(redirect_login + "?succURL=" + succURL);// 未登录状态跳转到登录页面
			}
			return false;
		}
		return true;
	}
}
