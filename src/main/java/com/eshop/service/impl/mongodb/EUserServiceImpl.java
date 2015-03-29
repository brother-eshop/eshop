package com.eshop.service.impl.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EUserDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EUser;
import com.eshop.service.mongodb.EUserService;

@Service("euserService")
public class EUserServiceImpl extends AbstractService<EUser, String> implements
		EUserService {

	@Autowired
	private EUserDao euserDao;

	@Override
	public DAO<EUser, String> getDao() {
		return euserDao;
	}
}
