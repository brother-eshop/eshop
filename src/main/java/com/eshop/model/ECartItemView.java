package com.eshop.model;

import java.util.Date;

import com.eshop.frameworks.core.entity.MongoEntity;


public class ECartItemView extends MongoEntity{

	private static final long serialVersionUID = -1827485253454394482L;
	
	private String id;
	
	private String userId;
	
	private String shopId;
	
	private String goodsId;
	
	private Integer goodsCount;
	
	private Date addTime;
	
	private Integer status;
	
}
