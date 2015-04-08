package com.eshop.service.impl.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
		Query query = new Query(Criteria.where("userId").is(userId));
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "range"));
		query.with(new Sort(orders));
		return shippingDao.findList(query, Shipping.class);
	}
}
