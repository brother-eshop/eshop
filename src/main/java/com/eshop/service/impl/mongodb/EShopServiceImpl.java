package com.eshop.service.impl.mongodb;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EShopDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;
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

	@Override
	public EShop getEShopByUser(EUser euser) {
		return eshopDao.findOne(Criteria.where("userId").is(euser.getId()),EShop.class);
	}

	@Override
	public void updateByObj(EShop eshop) {
		Update update = new Update();
		update.set("isActivted", eshop.getIsActivted()).set("remark", eshop.getRemark());
		eshopDao.update(new Query(Criteria.where("id").is(eshop.getUserId())), update, EShop.class);
		
	}

}
