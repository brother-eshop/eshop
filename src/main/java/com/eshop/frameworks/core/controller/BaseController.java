package com.eshop.frameworks.core.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.eshop.cache.SessionProvider;
import com.eshop.common.util.date.CustomTimestampEditor;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.spring.StringEscapeEditor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;


public class BaseController {

	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static JsonParser jsonParser = new JsonParser();
	private PageEntity page;
	@Autowired
	protected SessionProvider sessionProvider;
	public Map<String, Object> json = new HashMap<String, Object>();

	public void setJson(boolean success, String message, Object entity) {
		json.put("success", success);
		json.put("message", message);
		json.put("entity", entity);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		datetimeFormat.setLenient(false);
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(java.sql.Timestamp.class, new CustomTimestampEditor(datetimeFormat, true));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(Double.class, null, true));
		binder.registerCustomEditor(double.class, null, new CustomNumberEditor(Double.class, null, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(true));
		binder.registerCustomEditor(String.class, new StringEscapeEditor(false, false, false));
	}

	@InitBinder("page")
	public void initBinderPage(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("page.");
	}

	public String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @return the page
	 */
	public PageEntity getPage() {
		if (page == null) {
			page = new PageEntity();
		}
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(PageEntity page) {
		if (page != null) {
			this.page = page;
		}
	}
	
	
	public void clear(HttpServletRequest request, HttpServletResponse response) {
		sessionProvider.logout(request, response);
	}


	public void setSessionAttribute(HttpServletRequest request, HttpServletResponse response, String name, Serializable v) {
		sessionProvider.setAttribute(request, response, name, v);
		
	}

	public Object getSessionAttribute(HttpServletRequest request, String name) {
		return sessionProvider.getAttribute(request, name);
		
	}

}
