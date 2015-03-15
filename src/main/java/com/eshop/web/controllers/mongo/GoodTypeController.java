package com.eshop.web.controllers.mongo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.User;
import com.eshop.model.mongodb.GoodType;
import com.eshop.service.mongodb.GoodTypeService;

@Controller
@RequestMapping("/manager/good_type")
public class GoodTypeController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(GoodTypeController.class);

	@Autowired
	private GoodTypeService goodTypeService;

	// 路径
	private String toList = "/manager/good_type/good_type_list.httl";// 产品表页
	private String toAdd = "/manager/good_type/good_type_add.httl";// 添加页面
	private String toEdit = "/manager/good_type/good_type_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request,
			HttpServletResponse response, GoodType query,
			@ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new GoodType();
			}
			List<GoodType> list = goodTypeService.getGoodTyperPage(query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodTypeList", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("GoodTypeController.listAll", e);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		try {
		} catch (Exception e) {
			logger.error("GoodTypeController.toAdd", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public RedirectView add(GoodType goodType, HttpServletRequest request) {
		try {
			goodTypeService.insert(goodType);
		} catch (Exception e) {
			logger.error("GoodTypeController.add", e);
		}
		return new RedirectView("/manager/good_type/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView toEdit(String id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		try {
			GoodType goodType = goodTypeService.getByid(id);
			modelAndView.addObject(goodType);
		} catch (Exception e) {
			logger.error("UserController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public RedirectView edit(GoodType goodType, HttpServletRequest request) {
		try {
			goodTypeService.updateGoodType(goodType);
		} catch (Exception e) {
			logger.error("GoodTypeController.edit", e);
		}
		return new RedirectView("/manager/good_type/list");
	}

	@RequestMapping("/delete")
	public RedirectView delete(String ids, HttpServletRequest request,
			User query, @ModelAttribute("page") PageEntity page,
			RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/good_type/list");
		String[] idArray = ids.split(",");
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					goodTypeService.deleteGoodType(id);
				}
			}
		} catch (Exception e) {
			logger.error("GoodTypeController.delete", e);
		}
		return rv;
	}
}
