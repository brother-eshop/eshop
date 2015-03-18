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
		String code = goods.getCode();
		String name = goods.getName();
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

	@Override
	public void importExcel(MultipartFile file) throws IOException {
		List<Goods> goodsList = readGoodsExcel(file.getInputStream());
		int i = 0;
		for (Goods goods : goodsList) {
			i++;
			// goodsDao.insertBatch(goodsList, Goods.class);
			goodsDao.insert(goods);
			// 后台会 一直跑着，做一个提示！
			System.out.println(i);
		}
	}

	private List<Goods> readGoodsExcel(InputStream ins) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(ins);
		Goods goods = null;
		List<Goods> list = new ArrayList<Goods>();
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
				goods = new Goods();
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
	public Goods getByCode(String code) {
		return goodsDao.findOne(Criteria.where("code").is(code), Goods.class);
	}

	@Override
	public List<Goods> searchGoods(String code, String name, PageEntity page) {
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
		return goodsDao.findList(query, Goods.class);
	}
}
