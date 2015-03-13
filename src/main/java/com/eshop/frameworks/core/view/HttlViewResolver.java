package com.eshop.frameworks.core.view;

import httl.web.WebEngine;

import java.util.Map;
import java.util.Properties;

/**
 * HTTL视图解析器。
 * 
 * @author yufeng
 * @since 5 5 2014
 * @see WebEngine
 * @see httl.web.springmvc.HttlViewResolver
 */
public class HttlViewResolver extends httl.web.springmvc.HttlViewResolver {

	private Properties httlProperties;

	public void setHttlProperties(Properties httlProperties) {
		this.httlProperties = httlProperties;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (httlProperties != null) {
			for (Map.Entry<Object, Object> entry : httlProperties.entrySet()) {
				WebEngine.setProperty((String) entry.getKey(), (String) entry.getValue());
			}
		}
		super.afterPropertiesSet();
	}
}
