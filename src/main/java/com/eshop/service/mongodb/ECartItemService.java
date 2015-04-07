package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.ECartItem;


public interface ECartItemService extends Service<ECartItem,String>{
	ECartItem getMyItem(String goodsId,String userId);
	List<ECartItem> getItems(String userId);
}
