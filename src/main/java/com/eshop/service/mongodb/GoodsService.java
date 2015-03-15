package com.eshop.service.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.Goods;


public interface GoodsService extends Service<Goods,String>{
	public String insertGoods(Goods goods);
	public List<Goods> getGoodsPage(Goods goods,PageEntity page);
	long getGoodsCount(Query query);
	Goods getByid(String id);
	void updateGoods(Goods goods);
	void deleteGoods(String id);
}
