package com.eshop.service.impl.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.GoodTypeDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.GoodType;
import com.eshop.service.mongodb.GoodTypeService;

@Service("goodTypeService")
public class GoodTypeServiceImpl extends AbstractService<GoodType,String> implements GoodTypeService{

	@Autowired
	private GoodTypeDao goodTypeDao;
	@Override
	public DAO<GoodType, String> getDao() {
		return goodTypeDao;
	}

}
