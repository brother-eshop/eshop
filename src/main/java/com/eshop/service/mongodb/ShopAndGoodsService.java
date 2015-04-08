package com.eshop.service.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.ShopAndGoods;


public interface ShopAndGoodsService extends Service<ShopAndGoods,String>{
	public void insertShopAndGoods(ShopAndGoods sad);
	public List<ShopAndGoods> getShopperGoods(String userId,ShopAndGoods sGoods,PageEntity page);
	public long getShopperGoodsCount(Query query);
	public void changeOutPrice(ShopAndGoods sad);
	public void batchInSale(String[] ids);
	public void batchOutSale(String[] ids);
	public List<ShopAndGoods> getGoodsByIds(String[] ids);
}
