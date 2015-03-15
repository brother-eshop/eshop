package com.eshop.service.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.Service;
import com.eshop.model.mongodb.GoodType;


public interface GoodTypeService extends Service<GoodType,String>{
	public String insertGoodType(GoodType goodType);
	public List<GoodType> getGoodTyperPage(GoodType goodType,PageEntity page);
	long getGoodTypeCount(Query query);
	GoodType getByid(String id);
	void updateGoodType(GoodType goodType);
	void deleteGoodType(String id);
}
