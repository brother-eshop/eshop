package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_order_detail")
@Data
@EqualsAndHashCode(callSuper = false)
public class EOrderDetail extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="detail_orderid",direction=IndexDirection.ASCENDING)
	private String orderId;
	
	@Indexed(unique=false,name="detail_ordernumber",direction=IndexDirection.ASCENDING)
	private String orderNumber;
	
	@Indexed(unique=false,name="detail_goodsId",direction=IndexDirection.ASCENDING)
	private String goodsId;
	
	@Indexed(unique=false,name="detail_goodsname",direction=IndexDirection.ASCENDING)
	private String goodsName;
	
	@Indexed(unique=false,name="detail_outprice",direction=IndexDirection.ASCENDING)
	private Double outPirce;
	
	@Indexed(unique=false,name="detail_goodscount",direction=IndexDirection.ASCENDING)
	private Integer goodsCount;
	
	@Indexed(unique=false,name="detail_subtotal",direction=IndexDirection.ASCENDING)
	private Double subtotal;//小计
	
}
