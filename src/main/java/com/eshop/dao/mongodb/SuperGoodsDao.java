package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.SuperGoods;

@Repository("superGoodsDao")
public class SuperGoodsDao extends AbstractBasicDAO<SuperGoods, String> implements DAO<SuperGoods, String>{

}
