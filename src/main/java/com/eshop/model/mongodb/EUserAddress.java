package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_user_address")
@Data
@EqualsAndHashCode(callSuper = false)
public class EUserAddress extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="address_userid",direction=IndexDirection.ASCENDING)
	private String userId;
	
	@Indexed(unique=false,name="address_receiver",direction=IndexDirection.ASCENDING)
	private String receiver;
	
	private String address;
	
	@Indexed(unique=false,name="address_mobile",direction=IndexDirection.ASCENDING)
	private String mobile;
	//可用此bean代替下面经纬度，加快搜索
	//@GeoSpatialIndexed//声明该字段为地理信息的索引
	//private LatLonPoint latlon;
	@Indexed(unique=false,name="address_lng",direction=IndexDirection.ASCENDING)
	private Double lng;//经度
	
	@Indexed(unique=false,name="address_lat",direction=IndexDirection.ASCENDING)
	private Double lat;//纬度
	
}
