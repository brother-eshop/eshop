package com.eshop.service.mongodb;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EUser;


public interface EUserService extends Service<EUser,String>{
	EUser getByUserName(EUser euser);
	EUser getByEmail(EUser euser);
	EUser getByMobile(EUser euser);
}
