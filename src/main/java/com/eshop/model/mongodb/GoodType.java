package com.eshop.model.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_good_type")
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodType extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	private String name;
	
	@Indexed(unique=false,name="type_pid",direction=IndexDirection.ASCENDING)
	private String pid;
	
	@Indexed(unique=false,name="type_path",direction=IndexDirection.ASCENDING)
	private String path;
	
	private String code;
}
