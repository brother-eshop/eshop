package com.eshop.service.mongodb;

import java.io.IOException;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

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
	void importExcel(MultipartFile file) throws IOException;
	Goods getByCode(String code);
	List<Goods> searchGoods(String code,String name,PageEntity page);
}
