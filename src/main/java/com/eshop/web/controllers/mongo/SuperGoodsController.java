package com.eshop.web.controllers.mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.User;
import com.eshop.model.mongodb.EUser;
import com.eshop.model.mongodb.GoodType;
import com.eshop.model.mongodb.SuperGoods;
import com.eshop.service.mongodb.GoodTypeService;
import com.eshop.service.mongodb.SuperGoodsService;

@Controller
@RequestMapping("/manager/super_goods")
public class SuperGoodsController extends BaseController {

	private static final Logger logger = Logger.getLogger(SuperGoodsController.class);

	@Autowired
	private SuperGoodsService superGoodsService;

	@Autowired
	private GoodTypeService goodTypeService;

	// 路径
	private String toList = "/manager/super_goods/goods_list.httl";// 产品表页
	private String toAdd = "/manager/super_goods/goods_add.httl";// 添加页面
	private String toEdit = "/manager/super_goods/goods_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request, HttpServletResponse response, SuperGoods query, @ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		setVar(modelAndView);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new SuperGoods();
			}
			List<SuperGoods> list = superGoodsService.getGoodsPage(query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodsList", list);
			modelAndView.addObject("page", this.getPage());
			modelAndView.addObject("search_code", query.getCode());
			modelAndView.addObject("search_name", query.getName());
		} catch (Exception e) {
			logger.error("GoodsController.listAll", e);
		}

		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/getGoods")
	public List<SuperGoods> getGoods(SuperGoods query, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("page") PageEntity page) {
		EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
		List<SuperGoods> list = new ArrayList<SuperGoods>();
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new SuperGoods();
			}
			query.setUserId(user.getId());
			list = superGoodsService.getGoodsPage(query, page);
		} catch (Exception e) {
			logger.error("GoodsController.listAll", e);
		}

		return list;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		setVar(modelAndView);
		try {
		} catch (Exception e) {
			logger.error("GoodsController.toAdd", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public ModelAndView toImport() {
		ModelAndView modelAndView = new ModelAndView("/manager/goods/goods_import.httl");
		setVar(modelAndView);
		try {
		} catch (Exception e) {
			logger.error("GoodsController.toImport", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public ModelAndView importExcel(MultipartFile file) {
		try {
			superGoodsService.importExcel(file);
		} catch (IOException e) {
			logger.error("GoodsController.importExcel", e);
		}

		return new ModelAndView(toList);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(SuperGoods goods, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(toList);
		setVar(modelAndView);
		try {
			superGoodsService.insert(goods);
			PageEntity page = new PageEntity();
			this.setPage(page);
			this.getPage().setPageSize(20);
			SuperGoods query = new SuperGoods();
			List<SuperGoods> list = superGoodsService.getGoodsPage(query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodsList", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("GoodsController.add", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView toEdit(String id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		setVar(modelAndView);
		try {
			SuperGoods goods = superGoodsService.getByid(id);
			modelAndView.addObject(goods);
		} catch (Exception e) {
			logger.error("GoodsController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public RedirectView edit(SuperGoods goods, HttpServletRequest request) {
		try {
			superGoodsService.updateGoods(goods);
		} catch (Exception e) {
			logger.error("GoodsController.edit", e);
		}
		return new RedirectView("/manager/goods/list");
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public RedirectView delete(String ids, HttpServletRequest request, User query, @ModelAttribute("page") PageEntity page, RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/goods/list");
		String[] idArray = ids.split(",");
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					superGoodsService.deleteGoods(id);
				}
			}
		} catch (Exception e) {
			logger.error("GoodsController.delete", e);
		}
		return rv;
	}

	@RequestMapping("/search")
	public ModelAndView searchGoods(HttpServletRequest request, HttpServletResponse response, SuperGoods query, @ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		setVar(modelAndView);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new SuperGoods();
			}
			List<SuperGoods> list = superGoodsService.searchGoods(query.getCode(), query.getName(), page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodsList", list);
			modelAndView.addObject("page", this.getPage());
			modelAndView.addObject("search_code", query.getCode());
			modelAndView.addObject("search_name", query.getName());
		} catch (Exception e) {
			logger.error("GoodsController.listAll", e);
		}

		return modelAndView;
	}

	// @RequestMapping(value="/search_types",method=RequestMethod.POST)
	// public void searchTypes(HttpServletRequest request){
	// File file = new FIle
	// }
	// 文件所在的层数

	@RequestMapping(value = "/insertSuperGoods", method = RequestMethod.POST)
	public void insertSuperGoods(HttpServletRequest request) {
		System.out.println("go-------------------------!");
		File file = new File("E:\\goods_type\\");
		insertGoods(file);
	}

	public static void main(String[] args) {
		File file = new File("E:\\goods_type\\");

		// ------------------导入商品
		// insertGoods(file);
	}

	private void insertGoods(File file) {
		if (file.isFile()) {// 如果是文件
			String parengName = file.getParentFile().getName();
			String goodsName = file.getName();
			String fileName = System.currentTimeMillis() + ".jpg";
			GoodType type = goodTypeService.getByName(parengName);
			File target = new File("E:\\goods_pic\\" + fileName);
			SuperGoods g = new SuperGoods();
			g.setName(goodsName);
			g.setPicPath(target.getAbsolutePath());
			g.setTypeCode(type.getCode());
			if (superGoodsService.getByName(goodsName) == null) {
				fileChannelCopy(file, target);
				System.out.println("insert:::::" + goodsName + "==" + target.getAbsolutePath() + "==" + type.getCode());
				superGoodsService.insert(g);
			} else {
				System.out.println("have");
			}
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				insertGoods(f);
			}
		}
	}

	public static void getGoods(File file) {
		if (file.isFile()) {// 如果是文件
			String parengName = file.getParentFile().getName();
			String goodsName = file.getName();
			if (goodsName.lastIndexOf(".") > 0) {
				goodsName = goodsName.substring(0, goodsName.lastIndexOf("."));
			}
			// Stirng picName = now.t
			// 手动创建文件夹吧

			File target = new File("E:\\goods_pic\\" + goodsName + ".jpg");
			// fileChannelCopy(file,target);
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				getGoods(f);
			}
		}

	}

	public void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {

			fi = new FileInputStream(s);

			fo = new FileOutputStream(t);

			in = fi.getChannel();// 得到对应的文件通道

			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				fi.close();

				in.close();

				fo.close();

				out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

}
