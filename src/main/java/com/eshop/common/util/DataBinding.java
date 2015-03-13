package com.eshop.common.util;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.eshop.common.util.date.CustomTimestampEditor;

public class DataBinding implements WebBindingInitializer {
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.setConversionService(new DefaultFormattingConversionService());
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
		// binder.setConversionService(new DefaultConversionService());
	}

}
