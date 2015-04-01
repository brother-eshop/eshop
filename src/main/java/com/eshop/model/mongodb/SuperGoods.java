package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_super_goods")
@Data
@EqualsAndHashCode(callSuper = false)
public class SuperGoods extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="super_goods_name",direction=IndexDirection.ASCENDING)
	private String name;
	
	@Indexed(unique=false,name="super_type_code",direction=IndexDirection.ASCENDING)
	private String typeCode;
	
	@Indexed(unique=false,name="super_goods_code",direction=IndexDirection.ASCENDING)
	private String code;
	
	private String standard;
	
	private String manufacturer;
	
	private String unit;
	
	@Indexed(unique=false,name="super_pic_path",direction=IndexDirection.ASCENDING)
	private String picPath;
	
	private String remark;
	
	private Double price;
	
	private Integer status;
	
	private String userId;
	
}
