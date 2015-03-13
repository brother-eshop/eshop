package com.eshop.frameworks.core.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.eshop.frameworks.core.mongod.CascadeType;

@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.FIELD}) 
public @interface Cascade {
	CascadeType cascadeType() default CascadeType.NONE;
}
