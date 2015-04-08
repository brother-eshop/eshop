package com.eshop.service.impl.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EUserAddressDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EUserAddress;
import com.eshop.service.mongodb.EUserAddressService;

@Service("euserAddressService")
public class EUserAddressServiceImpl extends AbstractService<EUserAddress, String> implements
	EUserAddressService {

	@Autowired
	private EUserAddressDao euserAddressDao;

	@Override
	public DAO<EUserAddress, String> getDao() {
		return euserAddressDao;
	}

	@Override
	public List<EUserAddress> getAddressByUserId(String userId) {
		return euserAddressDao.findList(Criteria.where("userId").is(userId), EUserAddress.class);
	}

	@Override
	public EUserAddress getEUserAddressById(String id) {
		return euserAddressDao.findOne(Criteria.where("id").is(id), EUserAddress.class);
	}

}
