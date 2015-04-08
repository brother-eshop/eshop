package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.EOrder;

@Repository("eorderDao")
public class EOrderDao extends AbstractBasicDAO<EOrder, String> implements DAO<EOrder, String>{

}
