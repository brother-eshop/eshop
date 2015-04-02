package com.eshop.service.mongodb;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;


public interface EShopService extends Service<EShop,String>{
	EShop getEShopByUser(EUser euser);
	void updateByObj(EShop eshop);
}
