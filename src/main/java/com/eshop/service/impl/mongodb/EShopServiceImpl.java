package com.eshop.service.impl.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.eshop.dao.mongodb.EShopDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;
import com.eshop.service.mongodb.EShopService;

@Service("eshopService")
public class EShopServiceImpl extends AbstractService<EShop, String> implements
		EShopService {

	@Autowired
	private EShopDao eshopDao;

	@Override
	public DAO<EShop, String> getDao() {
		return eshopDao;
	}

	@Override
	public EShop getEShopByUser(String userId) {
		return eshopDao.findOne(Criteria.where("userId").is(userId),
				EShop.class);
	}

	@Override
	public void updateByObj(EShop eshop) {
		Update update = new Update();
		update.set("isActivted", eshop.getIsActivted()).set("remark",
				eshop.getRemark());
		eshopDao.update(new Query(Criteria.where("id").is(eshop.getUserId())),
				update, EShop.class);

	}

	//查询满足条件的店铺，默认先查询出2000米以内的所有店铺，2000米可以改。
	@Override
	public List<EShop> searchShopps(Double ulng, Double ulat) {
		double[] params = this.getAround(ulng, ulat, 2000);
		double minLng = params[0];
		double minLat = params[1];
		double maxLng = params[2];
		double maxLat = params[3];
		Query query = new Query();
		query.addCriteria(Criteria.where("lng").gte(minLng).andOperator(Criteria.where("lng").lte(maxLng)));
		query.addCriteria(Criteria.where("lat").gte(minLat).andOperator(Criteria.where("lat").lte(maxLat)));
		List<EShop> shopps = eshopDao.findList(query, EShop.class);
		List<EShop> satisfyShopps = new ArrayList<EShop>();
		for(EShop eshop : shopps){
			double slng = eshop.getLng();
			double slat = eshop.getLat();
			Double devliverScop = eshop.getDevliverScope();
			double distance = this.distance(ulng, ulat, slng, slat);
			if(distance <= devliverScop){
				satisfyShopps.add(eshop);
			}
		}
		return satisfyShopps;
	}

	/**
	 * 
	 * 百度地图
	 * 
	 * 获取给定经纬度和半径距离的经纬度范围
	 * 
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 * @param raidus
	 *            单位:m
	 * @return 数组 minLng, minLat, maxLng, maxLat
	 */
	private double[] getAround(double lon, double lat, int raidus) {
		Double latitude = lat;
		Double longitude = lon;
		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;
		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;
		Double mpdLng = Math.abs(degree * Math.cos(latitude * (Math.PI / 180)));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLng, minLat, maxLng, maxLat };
	}

	//获取两个百度坐标点之间的距离
	private double distance(double centerLon, double centerLat,
			double targetLon, double targetLat) {
		double jl_jd = 102834.74258026089786013677476285;// 每经度单位米;
		double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米;
		double b = Math.abs((centerLat - targetLat) * jl_jd);
		double a = Math.abs((centerLon - targetLon) * jl_wd);
		return Math.sqrt((a * a + b * b));
	}
}
