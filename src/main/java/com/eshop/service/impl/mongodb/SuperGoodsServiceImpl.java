package com.eshop.service.impl.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eshop.dao.mongodb.GoodTypeDao;
import com.eshop.dao.mongodb.SuperGoodsDao;
import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.impl.AbstractService;
import com.eshop.model.mongodb.SuperGoods;
import com.eshop.service.mongodb.SuperGoodsService;

@Service("superGoodsService")
public class SuperGoodsServiceImpl extends AbstractService<SuperGoods, String> implements
		SuperGoodsService {

	@Autowired
	private SuperGoodsDao superGoodsDao;
	
	@Autowired
	private GoodTypeDao goodTypeDao;

	@Override
	public DAO<SuperGoods, String> getDao() {
		return superGoodsDao;
	}

	@Override
	public String insertGoods(SuperGoods goods) {
		superGoodsDao.insert(goods);
		return goods.getId();
	}

	@Override
	public List<SuperGoods> getGoodsPage(SuperGoods goods, PageEntity page) {
		String code = goods.getCode();
		String name = goods.getName();
		String typeCode = goods.getTypeCode();
		Criteria criteria = new Criteria();
		Query query = new Query();
		if (!"".equals(code) && code != null) {
			query.addCriteria(Criteria.where("code").regex(
					Pattern.compile("^" + code + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(name) && name != null) {
			query.addCriteria(Criteria.where("name").regex(
					Pattern.compile("^.*" + name + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if(!"".equals(typeCode)&&typeCode!=null){
			List<String> codeList = getTypesByPath(typeCode);
			query.addCriteria(Criteria.where("typeCode").in(codeList));
//			if(codes.size()==1){
//				query.addCriteria(Criteria.where("typeCode").is(typeCode));
//			}else if(codes.size()>1){
//				Criteria[] criterialist = new Criteria[codes.size()-1];
//				int i=0;
//					for(GoodType goodType : codes){
//						if(goodType.getCode().equals(typeCode)){
//							continue;
//						}
//						Criteria criteriaor = new Criteria();
//						criteriaor.orOperator(Criteria.where("typeCode").is(goodType.getCode()));
//						criterialist[i] = criteriaor;
//						i++;
//					}
//					criteria.orOperator(criterialist);
//					query.addCriteria(criteria);
//					
//				}
		}
//		System.out.println(query.getQueryObject().toString());
		int count = (int) this.getGoodsCount(query);
		page.setTotalResultSize(count);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "code"));
		query.with(new Sort(orders));
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return superGoodsDao.findList(query, SuperGoods.class);
	}

	@Override
	public long getGoodsCount(Query query) {
		return superGoodsDao.size(query, SuperGoods.class);
	}

	@Override
	public SuperGoods getByid(String id) {
		return superGoodsDao.findOne(Criteria.where("id").is(id), SuperGoods.class);
	}

	@Override
	public void updateGoods(SuperGoods goods) {
		Update update = new Update();
		update.inc("reNum", 1).set("name", goods.getName())
				.set("type_code", goods.getTypeCode())
				.set("picPath", goods.getPicPath())
				.set("code", goods.getCode())
				.set("standard", goods.getStandard());
		update(new Query(Criteria.where("id").is(goods.getId())), update,
				SuperGoods.class);
	}

	@Override
	public void deleteGoods(String id) {
		removeById(id, SuperGoods.class);
	}

	@Override
	public void importExcel(MultipartFile file) throws IOException {
		List<SuperGoods> goodsList = readGoodsExcel(file.getInputStream());
		int i = 0;
		for (SuperGoods goods : goodsList) {
			i++;
			// goodsDao.insertBatch(goodsList, Goods.class);
			superGoodsDao.insert(goods);
			// 后台会 一直跑着，做一个提示！
			System.out.println(i);
		}
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

	private List<SuperGoods> readGoodsExcel(InputStream ins) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(ins);
		SuperGoods goods = null;
		List<SuperGoods> list = new ArrayList<SuperGoods>();
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			System.out.println(hssfSheet.getLastRowNum());
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				goods = new SuperGoods();
				HSSFCell code = hssfRow.getCell(1);
				if (code == null) {
					continue;
				}
				goods.setCode(getValue(code));

				HSSFCell name = hssfRow.getCell(2);
				if (name == null) {
					continue;
				}
				goods.setName(getValue(name));

				HSSFCell standared = hssfRow.getCell(3);
				if (standared != null) {
					goods.setStandard(getValue(standared));
				}
				HSSFCell unit = hssfRow.getCell(4);
				if (unit != null) {
					goods.setUnit(getValue(unit));
				}

				list.add(goods);
			}

		}
		return list;
	}

	private String getValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());

		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}

	}

	@Override
	public SuperGoods getByCode(String code) {
		return superGoodsDao.findOne(Criteria.where("code").is(code), SuperGoods.class);
	}

	@Override
	public List<SuperGoods> searchGoods(String code, String name, PageEntity page) {
		Query query = new Query();
		if (!"".equals(code) && code != null) {
			query.addCriteria(Criteria.where("code").regex(
					Pattern.compile("^" + code + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		if (!"".equals(name) && name != null) {
			query.addCriteria(Criteria.where("name").regex(
					Pattern.compile("^.*" + name + ".*$",
							Pattern.CASE_INSENSITIVE)));
		}
		int count = (int) this.getGoodsCount(query);
		page.setTotalResultSize(count);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "code"));
		query.with(new Sort(orders));
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return superGoodsDao.findList(query, SuperGoods.class);
	}

	@Override
	public SuperGoods getByName(String goodsName) {
		return superGoodsDao.findOne(Criteria.where("name").is(goodsName), SuperGoods.class);
	}
}
