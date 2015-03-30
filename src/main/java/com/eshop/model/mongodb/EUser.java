package com.eshop.model.mongodb;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eshop.frameworks.core.entity.MongoEntity;


@Document(collection="e_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class EUser extends MongoEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827485253454394482L;

	@Id
	private String id;
	
	@Indexed(unique=false,name="user_name",direction=IndexDirection.ASCENDING)
	private String username;
	
	@Indexed(unique=false,name="user_password",direction=IndexDirection.ASCENDING)
	private String password;
	
	private String email;
	
	@Indexed(unique=false,name="user_mobile",direction=IndexDirection.ASCENDING)
	private String mobile;
	
	private Integer isMobileVerified;
	
	private Integer isEmailVerified;
	
	private String shopName;
	
	private String cityCode;
	
	private String  lastLoginIP;
	
	private Date lastLoginTime;
	
	private Long score;
	
	private Date regTime;
	
	private String captcha;
	
}
