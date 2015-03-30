package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.EShop;

@Repository("eshopDao")
public class EShopDao extends AbstractBasicDAO<EShop, String> implements DAO<EShop, String>{

}
