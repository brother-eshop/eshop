package com.eshop.service.mongodb;

import java.io.IOException;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.SuperGoods;


public interface SuperGoodsService extends Service<SuperGoods,String>{
	public String insertGoods(SuperGoods goods);
	public List<SuperGoods> getGoodsPage(SuperGoods goods,PageEntity page);
	long getGoodsCount(Query query);
	SuperGoods getByid(String id);
	void updateGoods(SuperGoods goods);
	void deleteGoods(String id);
	void importExcel(MultipartFile file) throws IOException;
	SuperGoods getByCode(String code);
	List<SuperGoods> searchGoods(String code,String name,PageEntity page);
	SuperGoods getByName(String goodsName);
}
