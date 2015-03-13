package com.eshop.model.manager;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable{
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String mobile;
    private Integer isMobileVerified;
    private Integer isEmailVerified;
    private String nickName;
    private Integer isActivated;
    private String avatar;
    private Long follower;
    private Long attention;
    private String rank;
    private String lastLoginIp;
    private java.util.Date lastLoginTime;
    private Long experience;
    private Long reputation;
    private Long score;
    private String sPhoto;
    private String mPhoto;
    private String lPhoto;
    private String certifyCode;
    private java.util.Date regTime;
    private String tPhoto;
    private Integer source;
    private String sourceName;
}
