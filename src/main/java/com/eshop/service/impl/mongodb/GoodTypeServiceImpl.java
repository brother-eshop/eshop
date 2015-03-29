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

import com.eshop.dao.mongodb.GoodTypeDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.GoodType;
import com.eshop.service.mongodb.GoodTypeService;

@Service("goodTypeService")
public class GoodTypeServiceImpl extends AbstractService<GoodType,String> implements GoodTypeService{

	@Autowired
	private GoodTypeDao goodTypeDao;
	@Override
	public DAO<GoodType, String> getDao() {
		return goodTypeDao;
	}
	@Override
	public String insertGoodType(GoodType goodType) {
		goodTypeDao.insert(goodType);
		return goodType.getId();
	}
	@Override
	public List<GoodType> getGoodTyperPage(GoodType goodType) {
		Query query = new Query(Criteria.where("pid").is("0"));
		int count = (int) this.getGoodTypeCount(query);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "sort"));
		query.with(new Sort(orders));
		return goodTypeDao.findList(query, GoodType.class);
	}
	
	@Override
	public long getGoodTypeCount(Query query) {
		return goodTypeDao.size(query, GoodType.class);
	}
	
	@Override
	public GoodType getByid(String id) {
		return goodTypeDao.findOne(Criteria.where("id").is(id), GoodType.class);
	}
	@Override
	public void updateGoodType(GoodType goodType) {
		Update update = new Update();
		update.inc("reNum", 1).set("pid", goodType.getPid()).set("name",goodType.getName()).set("path", goodType.getPath()).set("code", goodType.getCode());
		update(new Query(Criteria.where("id").is(goodType.getId())), update, GoodType.class);
	}
	@Override
	public void deleteGoodType(String id) {
		removeById(id, GoodType.class);
	}
	@Override
	public List<GoodType> getGoodTypeChildren(GoodType goodType) {
		Query query = new Query(Criteria.where("pid").is(goodType.getPid()));
		return goodTypeDao.findList(query, GoodType.class);
	}
	@Override
	public GoodType getByName(String name) {
		return goodTypeDao.findOne(Criteria.where("name").is(name), GoodType.class);
	}
}
