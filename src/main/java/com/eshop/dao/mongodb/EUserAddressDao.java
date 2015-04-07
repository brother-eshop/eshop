package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.EUserAddress;

@Repository("euserAddressDao")
public class EUserAddressDao extends AbstractBasicDAO<EUserAddress, String> implements DAO<EUserAddress, String>{

}
