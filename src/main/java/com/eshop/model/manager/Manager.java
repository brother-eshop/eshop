package com.eshop.model.manager;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection="e_manager")
public class Manager implements Serializable{
	private static final long serialVersionUID = -1827485253454394482L;
	public static final int DEFAULT_STATUS = 0;// 用户默认状态
	public static final int FREEZE_STATUS = 1;// 冻结
	public static final int DELETE_STATUS = 2;// 伪删除
	@Id
	private String id;
	@Indexed(unique=true,name="login_name",direction=IndexDirection.ASCENDING)
    private String loginName;
    
    private String loginPwd;
    
    private String userName;
    
    private Integer status;
    
    private java.util.Date lastLoginTime;
    
    private String lastLoginIp;
    
    private String email;
    
    private String tel;
    
    private Long groupId;
    
    private Integer userType;
    
    private java.util.Date createTime;
}
