package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.EOrderDetail;

@Repository("eorderDetailDao")
public class EOrderDetailDao extends AbstractBasicDAO<EOrderDetail, String> implements DAO<EOrderDetail, String>{

}
