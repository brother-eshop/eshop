package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;


public interface EShopService extends Service<EShop,String>{
	EShop getEShopByUser(String userId);
	void updateByObj(EShop eshop);
	List<EShop> searchShopps(Double lng,Double lat);
}
