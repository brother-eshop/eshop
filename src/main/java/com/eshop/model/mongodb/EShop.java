package com.eshop.model.mongodb;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_shop")
@Data
@EqualsAndHashCode(callSuper = false)
public class EShop extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String userId;
	
	@Indexed(unique=false,name="shop_address",direction=IndexDirection.ASCENDING)
	private String shopAddress;
	
	@Indexed(unique=false,name="shop_name",direction=IndexDirection.ASCENDING)
	private String shopName;
	
	private Integer isActivted;
	
	@Indexed(unique=false,name="shop_photo",direction=IndexDirection.ASCENDING)
	private String shopPhoto;
	
	@Indexed(unique=false,name="city_code",direction=IndexDirection.ASCENDING)
	private String cityCode;
	
	private Date regTime;
	
	@Indexed(unique=false,name="is_online",direction=IndexDirection.ASCENDING)
	private Integer isOnline;
	
	private Integer verified;
	
	@Indexed(unique=false,name="devliver_scope",direction=IndexDirection.ASCENDING)
	private String devliverScope;
	
	@Indexed(unique=false,name="shopPoint",direction=IndexDirection.ASCENDING)
	private String shopPoint;
	
	private String remark;

	
	
}
