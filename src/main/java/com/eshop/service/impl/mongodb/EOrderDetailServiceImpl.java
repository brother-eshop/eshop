package com.eshop.service.impl.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EOrderDetailDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EOrderDetail;
import com.eshop.service.mongodb.EOrderDetailService;

@Service("eorderDetailDService")
public class EOrderDetailServiceImpl extends AbstractService<EOrderDetail, String>
		implements EOrderDetailService {

	@Autowired
	private EOrderDetailDao eorderDetailDao;

	@Override
	public DAO<EOrderDetail, String> getDao() {
		return eorderDetailDao;
	}

	@Override
	public List<EOrderDetail> getOrderDetail(String orderId) {
		return eorderDetailDao.findList(Criteria.where("orderId").is(orderId), EOrderDetail.class);
	}
}
