package com.eshop.service.impl.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.GoodTypeDao;
import com.eshop.dao.mongodb.GoodsDao;
import com.eshop.dao.mongodb.ShopAndGoodsDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.Goods;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.ShopAndGoodsService;

@Service("shopAndGoodsService")
public class ShopAndGoodsServiceImpl extends AbstractService<ShopAndGoods, String> implements
		ShopAndGoodsService {

	@Autowired
	private ShopAndGoodsDao shopAndGoodsDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodTypeDao goodTypeDao;

	@Override
	public DAO<ShopAndGoods, String> getDao() {
		return shopAndGoodsDao;
	}

	@Override
	public void insertShopAndGoods(ShopAndGoods sad) {
		shopAndGoodsDao.insert(sad);
	}

	@Override
	public List<ShopAndGoods> getShopperGoods(String userId,ShopAndGoods sGoods,PageEntity page) {
		Query query = new Query(Criteria.where("userId").is(userId));
		String goodsName = sGoods.getGoodsName();
		String goodsCode = sGoods.getGoodsCode();
		String goodsManufacturer = sGoods.getManufacturer();
		String typeCode = sGoods.getTypeCode();
		if (!"".equals(goodsName) && goodsName != null) {
			query.addCriteria(Criteria.where("goodsName").regex(
					Pattern.compile("^.*" + goodsName + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(goodsCode) && goodsCode != null) {
			query.addCriteria(Criteria.where("goodsCode").regex(
					Pattern.compile("^" + goodsCode + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(goodsManufacturer) && goodsManufacturer != null) {
			query.addCriteria(Criteria.where("manufacturer").regex(
					Pattern.compile("^.*" + goodsManufacturer + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if(!"".equals(typeCode)&&typeCode!=null){
			List<String> codeList = getTypesByPath(typeCode);
			query.addCriteria(Criteria.where("typeCode").in(codeList));
		}
		int count = (int) this.getShopperGoodsCount(query);
		page.setTotalResultSize(count);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "code"));
		query.with(new Sort(orders));
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return shopAndGoodsDao.findList(query, ShopAndGoods.class);
	} 
	
	private List<String> getTypesByPath(String typeCode){
		Query query = new Query();
		query.addCriteria(Criteria.where("path").regex(
				Pattern.compile("^.*:" + typeCode + ":.*$",
						Pattern.CASE_INSENSITIVE)));
//		List<String> s = goodTypeDao.getMongoTemplate().getCollection("e_good_type").distinct("code",query.getQueryObject());
//		System.out.println(s);
//		System.out.println("______");
//		return goodTypeDao.findList(Criteria.where("path").regex(
//				Pattern.compile("^.*:" + typeCode + ":.*$",
//						Pattern.CASE_INSENSITIVE)), GoodType.class);
		return goodTypeDao.getMongoTemplate().getCollection("e_good_type").distinct("code",query.getQueryObject());
		
	}
	
	@Override
	public long getShopperGoodsCount(Query query) {
		return shopAndGoodsDao.size(query, ShopAndGoods.class);
	}

	@Override
	public void changeOutPrice(ShopAndGoods sad) {
		shopAndGoodsDao.update(new Query().addCriteria(Criteria.where("id").is(sad.getId())), Update.update("outPrice", sad.getOutPrice()), ShopAndGoods.class);
	}

	@Override
	public void batchInSale(String[] ids) {
		shopAndGoodsDao.update(new Query().addCriteria(Criteria.where("id").in(ids)),Update.update("status", 1), ShopAndGoods.class);
	}
	
	@Override
	public void batchOutSale(String[] ids) {
		shopAndGoodsDao.update(new Query().addCriteria(Criteria.where("id").in(ids)),Update.update("status", 2), ShopAndGoods.class);
	}
	
}
