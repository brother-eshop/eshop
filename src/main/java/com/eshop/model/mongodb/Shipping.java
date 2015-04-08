package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_shipping")
@Data
@EqualsAndHashCode(callSuper = false)
public class Shipping extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="shipping_userid",direction=IndexDirection.ASCENDING)
	private String userId;
	
	@Indexed(unique=false,name="shipping_range",direction=IndexDirection.ASCENDING)
	private Double range;//距离
	
	@Indexed(unique=false,name="shipping_freeprice",direction=IndexDirection.ASCENDING)
	private Double freePrice;//满多少元免费配送
	
	@Indexed(unique=false,name="shipping_price",direction=IndexDirection.ASCENDING)
	private Double shippingPrice;//配送费用
	
}
