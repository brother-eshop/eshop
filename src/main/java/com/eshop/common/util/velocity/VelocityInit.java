package com.eshop.common.util.velocity;

import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;


public class VelocityInit {
	/**
	 * file方式加载模板
	 * @return
	 */
	public static VelocityEngine getInstance() {

    	//初始化参数
        Properties properties=new Properties();
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        properties.setProperty("userdirective", "com.yunxue.common.util.velocity.TrimHtmlDirective,com.yunxue.common.util.velocity.PageContentDirective,com.yunxue.common.util.velocity.LimitDirective");
        //设置velocity资源加载方式为file
        properties.setProperty("resource.loader", "file");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");  
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        return velocityEngine;
	}
	/**
	 * class方式加载模板
	 * @return
	 */
	public static VelocityEngine getInstanceByLoadClass() {

    	//初始化参数
        Properties properties=new Properties();
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        properties.setProperty("userdirective", "com.yunxue.common.util.velocity.TrimHtmlDirective,com.yunxue.common.util.velocity.PageContentDirective,com.yunxue.common.util.velocity.LimitDirective");
        //设置velocity资源加载方式为class
        properties.setProperty("resource.loader", "class");
        //设置velocity资源加载方式为class时的处理类
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");  
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        return velocityEngine;
	}
}
