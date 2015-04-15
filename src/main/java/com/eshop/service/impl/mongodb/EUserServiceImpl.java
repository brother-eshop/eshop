package com.eshop.service.impl.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

	@Override
	public EUser getByUserName(EUser euser) {
		return euserDao.findOne(Criteria.where("username").is(euser.getUsername()), EUser.class);
	}
	
	@Override
	public EUser getByEmail(String email) {
		return euserDao.findOne(Criteria.where("email").is(email), EUser.class);
	}
	
	@Override
	public EUser getByEmail(EUser euser) {
		return getByEmail(euser.getEmail());
	}

	@Override
	public EUser getByMobile(EUser euser) {
		return euserDao.findOne(Criteria.where("mobile").is(euser.getMobile()), EUser.class);
	}

	@Override
	public List<EUser> getUserByObj(EUser euser) {
		return euserDao.findList(Criteria.where("email").is(euser.getEmail()).orOperator(Criteria.where("username").is(euser.getUsername())), EUser.class);
	}

	@Override
	public void updateEUserShopper(EUser euser) {
		update(new Query(Criteria.where("id").in(euser.getId())), Update.update("isShopper", euser.getIsShopper()).set("shopName", euser.getShopName()), EUser.class);
	}
}
