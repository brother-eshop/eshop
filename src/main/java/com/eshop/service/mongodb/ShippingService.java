package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.Shipping;


public interface ShippingService extends Service<Shipping,String>{
	List<Shipping> getShippingByUser(String userId);
	
}
