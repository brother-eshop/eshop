package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_goods")
@Data
@EqualsAndHashCode(callSuper = false)
public class Goods extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	private String name;
	
	@Indexed(unique=false,name="type_code",direction=IndexDirection.ASCENDING)
	private String typeCode;
	
	private String code;
	
	private String standard;
	
	private String manufacturer;
	
	private String unit;
	
	@Indexed(unique=false,name="pic_path",direction=IndexDirection.ASCENDING)
	private String picPath;
	
	private String remark;
	
	private int status;
	
}
