package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.Goods;

@Repository("goodDao")
public class GoodsDao extends AbstractBasicDAO<Goods, String> implements DAO<Goods, String>{

}
