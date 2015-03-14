package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.GoodType;

@Repository("goodTypeDao")
public class GoodTypeDao extends AbstractBasicDAO<GoodType, String> implements DAO<GoodType, String>{

}
