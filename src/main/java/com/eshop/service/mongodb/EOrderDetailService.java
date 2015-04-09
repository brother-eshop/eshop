package com.eshop.service.mongodb;

import java.util.List;

import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.EOrderDetail;


public interface EOrderDetailService extends Service<EOrderDetail,String>{
	List<EOrderDetail> getOrderDetail(String orderId);
}
