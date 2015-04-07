package com.eshop.model.mongodb;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_cart_item")
@Data
@EqualsAndHashCode(callSuper = false)
public class ECartItem extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="cart_user_id",direction=IndexDirection.ASCENDING)
	private String userId;
	
	@Indexed(unique=false,name="cart_shop_id",direction=IndexDirection.ASCENDING)
	private String shopId;
	
	@Indexed(unique=false,name="cart_goods_id",direction=IndexDirection.ASCENDING)
	private String goodsId;
	
	@Indexed(unique=false,name="cart_goods_count",direction=IndexDirection.ASCENDING)
	private Integer goodsCount;
	
	private Date addTime;
	
	@Indexed(unique=false,name="cart_status",direction=IndexDirection.ASCENDING)
	private Integer status;
	
	private ShopAndGoods goods;
	
	private EShop eshop;
}
