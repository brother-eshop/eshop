package com.eshop.service.impl.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.ECartItemDao;
import com.eshop.dao.mongodb.EShopDao;
import com.eshop.dao.mongodb.ShopAndGoodsDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.ECartItem;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.ECartItemService;

@Service("ecartItemService")
public class ECartItemServiceImpl extends AbstractService<ECartItem, String> implements
		ECartItemService {

	@Autowired
	private ECartItemDao ecartItemDao;
	
	@Override
	public DAO<ECartItem, String> getDao() {
		return ecartItemDao;
	}

	@Override
	public ECartItem getMyItem(String goodsId,String userId) {
		return ecartItemDao.findOne(Criteria.where("goodsId").is(goodsId).andOperator(Criteria.where("userId").is(userId)), ECartItem.class);
	}

	@Override
	public List<ECartItem> getItems(String userId) {
		List<ECartItem> ecartItems =  ecartItemDao.findList(Criteria.where("userId").is(userId), ECartItem.class);
		return ecartItems;
	}

	@Override
	public List<ECartItem> getSubItems(String userId, String shopId) {
		return ecartItemDao.findList(Criteria.where("userId").is(userId).andOperator(Criteria.where("shopId").is(shopId)), ECartItem.class);
	}
}
