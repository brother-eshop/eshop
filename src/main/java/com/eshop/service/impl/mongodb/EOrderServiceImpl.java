package com.eshop.service.impl.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EOrderDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EOrder;
import com.eshop.service.mongodb.EOrderService;

@Service("eorderService")
public class EOrderServiceImpl extends AbstractService<EOrder, String>
		implements EOrderService {

	@Autowired
	private EOrderDao eorderDao;

	@Override
	public DAO<EOrder, String> getDao() {
		return eorderDao;
	}
}
