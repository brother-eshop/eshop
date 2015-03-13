package com.eshop.web.intercepters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eshop.common.util.string.XSS;


public class XSSIntercepter extends HandlerInterceptorAdapter {

	private static final String REDIRECT_404 = "http://www.yunmar.com.cn/404";// 登录页面


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 访问的路径
		String targetURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
		if (XSS.hasXss(targetURL)) {
			response.sendRedirect(REDIRECT_404);// 未登录状态跳转到登录页面
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}
