package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EUser;


public interface EUserService extends Service<EUser,String>{
	EUser getByUserName(EUser euser);
	EUser getByEmail(EUser euser);
	EUser getByEmail(String email);
	EUser getByMobile(EUser euser);
	List<EUser> getUserByObj(EUser euser);
	void updateEUserShopper(EUser euser);
}
