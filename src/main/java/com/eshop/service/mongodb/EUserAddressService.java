package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EUserAddress;


public interface EUserAddressService extends Service<EUserAddress,String>{
	
	List<EUserAddress> getAddressByUserId(String userId);
	EUserAddress getEUserAddressById(String id);
}
