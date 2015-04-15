package com.eshop.frameworks.core.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @Create Date : 2014-3-23 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailParam implements Serializable{
	private static final long serialVersionUID = -2852422334723322023L;
	
	private Long id;//id
	
	private String subject;
	
	private String activeUrl;
	
	private String url;
	
	private String userName;
	
	private String nickName;

	// 用户类型，1 需求商 买家， 2 供应商  卖家
	private Integer userType;
	
	private String mobile;
	
	private String toEmail;
	
	private String templateName;
	
	private String email;
	
	private String company;

	private java.util.Date activeTime;
    
	
}