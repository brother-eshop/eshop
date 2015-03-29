package com.eshop.dao.mongodb;

import org.springframework.stereotype.Repository;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.dao.impl.common.AbstractBasicDAO;
import com.eshop.model.mongodb.EUser;

@Repository("euserDao")
public class EUserDao extends AbstractBasicDAO<EUser, String> implements DAO<EUser, String>{

}
