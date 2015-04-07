package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.ECartItem;

@Repository("ecartItemDao")
public class ECartItemDao extends AbstractBasicDAO<ECartItem, String> implements DAO<ECartItem, String>{

}
