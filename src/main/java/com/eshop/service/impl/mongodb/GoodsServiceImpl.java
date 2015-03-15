package com.eshop.service.impl.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.GoodsDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.Goods;
import com.eshop.service.mongodb.GoodsService;

@Service("goodsService")
public class GoodsServiceImpl extends AbstractService<Goods, String> implements
		GoodsService {

	@Autowired
	private GoodsDao goodsDao;

	@Override
	public DAO<Goods, String> getDao() {
		return goodsDao;
	}

	@Override
	public String insertGoods(Goods goods) {
		goodsDao.insert(goods);
		return goods.getId();
	}

	@Override
	public List<Goods> getGoodsPage(Goods goods, PageEntity page) {
		Query query = new Query();
		int count = (int) this.getGoodsCount(query);
		page.setTotalResultSize(count);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "code"));
		query.with(new Sort(orders));
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return goodsDao.findList(query, Goods.class);
	}

	@Override
	public long getGoodsCount(Query query) {
		return goodsDao.size(query, Goods.class);
	}

	@Override
	public Goods getByid(String id) {
		return goodsDao.findOne(Criteria.where("id").is(id), Goods.class);
	}

	@Override
	public void updateGoods(Goods goods) {
		Update update = new Update();
		update.inc("reNum", 1).set("name", goods.getName())
				.set("type_code", goods.getTypeCode())
				.set("picPath", goods.getPicPath())
				.set("code", goods.getCode())
				.set("standard", goods.getStandard());
		update(new Query(Criteria.where("id").is(goods.getId())), update,
				Goods.class);
	}

	@Override
	public void deleteGoods(String id) {
		removeById(id, Goods.class);
	}
}
