package com.eshop.service.impl.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EShopDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EShop;
import com.eshop.service.mongodb.EShopService;

@Service("eshopService")
public class EShopServiceImpl extends AbstractService<EShop, String> implements
		EShopService {

	@Autowired
	private EShopDao eshopDao;

	@Override
	public DAO<EShop, String> getDao() {
		return eshopDao;
	}

}
