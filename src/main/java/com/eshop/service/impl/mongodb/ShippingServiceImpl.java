package com.eshop.service.impl.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.ShippingDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.Shipping;
import com.eshop.service.mongodb.ShippingService;

@Service("shippingService")
public class ShippingServiceImpl extends AbstractService<Shipping, String> implements
		ShippingService {

	@Autowired
	private ShippingDao shippingDao;

	@Override
	public DAO<Shipping, String> getDao() {
		return shippingDao;
	}

	@Override
	public List<Shipping> getShippingByUser(String userId) {
		return shippingDao.findList(Criteria.where("userId").is(userId), Shipping.class);
	}
}
