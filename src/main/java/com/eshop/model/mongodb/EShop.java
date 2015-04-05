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
	private Double devliverScope;
	
	//TODO 已经删除本字段，考虑到同步代码时需要同步数据库，你们那边会报错，所以本字段暂时未删除
	@Indexed(unique=false,name="shopPoint",direction=IndexDirection.ASCENDING)
	private String shopPoint;
	
	@Indexed(unique=false,name="shop_lng",direction=IndexDirection.ASCENDING)
	private Double lng;//经度
	
	@Indexed(unique=false,name="shop_lat",direction=IndexDirection.ASCENDING)
	private Double lat;//纬度
	
	
	private String remark;

	
	
}
