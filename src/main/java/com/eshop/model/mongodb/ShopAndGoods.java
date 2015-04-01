package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;

@Document(collection = "e_shop_goods")
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopAndGoods extends MongoEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;

	@Indexed(unique = false, name = "user_id", direction = IndexDirection.ASCENDING)
	private String userId;

	@Indexed(unique = false, name = "goods_id", direction = IndexDirection.ASCENDING)
	private String goodsId;// 商品id

	@Indexed(unique = false, name = "goods_code", direction = IndexDirection.ASCENDING)
	private String goodsCode;// 商品条码

	@Indexed(unique = false, name = "goods_name", direction = IndexDirection.ASCENDING)
	private String goodsName;// 商品名称

	@Indexed(unique = false, name = "other_name", direction = IndexDirection.ASCENDING)
	private String otherName;// 商品别名

	@Indexed(unique = false, name = "goods_manufacturer", direction = IndexDirection.ASCENDING)
	private String manufacturer;// 商品制造商

	@Indexed(unique = false, name = "type_code", direction = IndexDirection.ASCENDING)
	private String typeCode;// 商品分类号

	private String standard;// 规格
	
	private String unit;// 单位
	
	@Indexed(unique=false,name="pic_path",direction=IndexDirection.ASCENDING)
	private String picPath;//图片路径
	
	private Double inPrice;// 进价

	private Double outPrice;// 售价

	private Integer count;// 库存数量

	private Double sale;// 折扣
	
	private Integer status;//状态

}
