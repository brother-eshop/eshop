package com.eshop.web.controllers.mongo;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam();
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.User;
import com.eshop.model.mongodb.Goods;
import com.eshop.service.mongodb.GoodsService;

@Controller
@RequestMapping("/manager/goods")
public class GoodsController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(GoodsController.class);

	@Autowired
	private GoodsService goodsService;

	// 路径
	private String toList = "/manager/goods/goods_list.httl";// 产品表页
	private String toAdd = "/manager/goods/goods_add.httl";// 添加页面
	private String toEdit = "/manager/goods/goods_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request,
			HttpServletResponse response, Goods query,
			@ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new Goods();
			}
			List<Goods> list = goodsService.getGoodsPage(query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodsList", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("GoodsController.listAll", e);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		try {
		} catch (Exception e) {
			logger.error("GoodsController.toAdd", e);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public ModelAndView toImport() {
		ModelAndView modelAndView = new ModelAndView("/manager/goods/goods_import.httl");
		try {
		} catch (Exception e) {
			logger.error("GoodsController.toImport", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public ModelAndView importExcel(MultipartFile file){
		try {
			goodsService.importExcel(file);
		} catch (IOException e) {
			logger.error("GoodsController.importExcel", e);
		}
		
		return new ModelAndView(toList);
	}

	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public RedirectView add(Goods goods, HttpServletRequest request) {
		try {
//			System.out.println("开始");
//			String path = request.getSession().getServletContext().getRealPath("goods_pics");
//			String fileName = file.getOriginalFilename();
//			System.out.println(path);
//			File targetFile = new File(path,fileName);
//			if(!targetFile.exists()){
//				targetFile.mkdirs();
//			}
//			file.transferTo(targetFile);
//			goods.setPicPath(request.getContextPath()+"/goods_pics/"+fileName);
			goodsService.insert(goods);
		} catch (Exception e) {
			logger.error("GoodsController.add", e);
		}
		return new RedirectView("/manager/goods/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView toEdit(String id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		try {
			Goods goods = goodsService.getByid(id);
			modelAndView.addObject(goods);
		} catch (Exception e) {
			logger.error("GoodsController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public RedirectView edit(Goods goods, HttpServletRequest request) {
		try {
			goodsService.updateGoods(goods);
		} catch (Exception e) {
			logger.error("GoodsController.edit", e);
		}
		return new RedirectView("/manager/goods/list");
	}

	@RequestMapping("/delete")
	public RedirectView delete(String ids, HttpServletRequest request,
			User query, @ModelAttribute("page") PageEntity page,
			RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/goods/list");
		String[] idArray = ids.split(",");
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					goodsService.deleteGoods(id);
				}
			}
		} catch (Exception e) {
			logger.error("GoodsController.delete", e);
		}
		return rv;
	}
}
