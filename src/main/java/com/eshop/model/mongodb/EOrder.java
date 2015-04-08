package com.eshop.model.mongodb;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_order")
@Data
@EqualsAndHashCode(callSuper = false)
public class EOrder extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="order_userid",direction=IndexDirection.ASCENDING)
	private String userId;
	
	@Indexed(unique=false,name="order_number",direction=IndexDirection.ASCENDING)
	private String orderNumber;
	
	@Indexed(unique=false,name="order_time",direction=IndexDirection.ASCENDING)
	private Date orderTime;
	
	@Indexed(unique=false,name="order_shopperid",direction=IndexDirection.ASCENDING)
	private String shopperId;
	
	@Indexed(unique=false,name="order_shopname",direction=IndexDirection.ASCENDING)
	private String shopName;
	
	@Indexed(unique=false,name="order_shopaddress",direction=IndexDirection.ASCENDING)
	private String shopAddress;
	
	@Indexed(unique=false,name="order_totalPrice",direction=IndexDirection.ASCENDING)
	private Double totalPrice;
	
	@Indexed(unique=false,name="order_status",direction=IndexDirection.ASCENDING)
	private Integer status;
	
	@Indexed(unique=false,name="order_useraddress",direction=IndexDirection.ASCENDING)
	private String  orderAddress;
	
	@Indexed(unique=false,name="order_receiver",direction=IndexDirection.ASCENDING)
	private String orderReceiver;
	
	@Indexed(unique=false,name="order_receiverMobile",direction=IndexDirection.ASCENDING)
	private String receiverMobile;
	
	private Double userLng;//用户收货地址的经度
	
	private Double userLat;//用户收货地址的纬度
	
	private Double devliverPirce;//配送费
	
	
	
}
