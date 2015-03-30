package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.ShopAndGoods;

@Repository("shopAndGoodsDao")
public class ShopAndGoodsDao extends AbstractBasicDAO<ShopAndGoods, String> implements DAO<ShopAndGoods, String>{

}
