package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.Shipping;

@Repository("shippingDao")
public class ShippingDao extends AbstractBasicDAO<Shipping, String> implements DAO<Shipping, String>{

}
