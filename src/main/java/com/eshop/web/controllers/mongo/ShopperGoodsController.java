package com.eshop.web.controllers.mongo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.params.GoodsParams;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.User;
import com.eshop.model.mongodb.EUser;
import com.eshop.model.mongodb.GoodType;
import com.eshop.model.mongodb.Goods;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.GoodTypeService;
import com.eshop.service.mongodb.GoodsService;
import com.eshop.service.mongodb.ShopAndGoodsService;

@Controller
@RequestMapping("/manager/shopperGoods")
public class ShopperGoodsController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(ShopperGoodsController.class);

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ShopAndGoodsService shopAndGoodsService;
	
	@Autowired
	private GoodTypeService goodTypeService;

	// 路径
	private String toList = "/manager/shop_goods/mygoods_list.httl";// 产品表页
	private String toAdd = "/manager/shop_goods/mygoods_add.httl";// 添加页面
	private String toEdit = "/manager/shop_goods/mygoods_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request,
			HttpServletResponse response, ShopAndGoods query,
			@ModelAttribute("page") PageEntity page) {
		EUser user= (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new ShopAndGoods();
			}
			List<ShopAndGoods> list = shopAndGoodsService.getShopperGoods(user.getId(),
					query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("shopperGoods", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("GoodsController.listShopperGoodsAll", e);
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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(ShopAndGoods sGoods, HttpServletRequest request) {
		EUser user = (EUser) sessionProvider.getAttribute(request,
				"USER_SESSION_NAME");
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			shopAndGoodsService.insertShopAndGoods(sGoods);
			PageEntity page = new PageEntity();
			this.setPage(page);
			this.getPage().setPageSize(20);
			ShopAndGoods query = new ShopAndGoods();
			List<ShopAndGoods> list = shopAndGoodsService.getShopperGoods(user.getId(),
					query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("shopperGoods", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("GoodsController.add", e);
		}
		return modelAndView;
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

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
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

	@RequestMapping("/search")
	public ModelAndView searchGoods(HttpServletRequest request,
			HttpServletResponse response, Goods query,
			@ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new Goods();
			}
			List<Goods> list = goodsService.searchGoods(query.getCode(),
					query.getName(), page);
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
	@RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
	public String insertBatch(@RequestBody GoodsParams goodsParams,HttpServletRequest request,
			HttpServletResponse response) {
		List<ShopAndGoods> sgoods = goodsParams.getSgoods();
		try {
			if(sgoods.size()>0){
				shopAndGoodsService.insertBatch(sgoods, ShopAndGoods.class);
			}
		} catch (Exception e) {
			logger.error("GoodsController.listAll", e);
			return "FAILE";
		}
		return "SUCCESS";
	}
	
	@RequestMapping("/goodsManage")
	public ModelAndView goodsManage(HttpServletRequest request,
			HttpServletResponse response, ShopAndGoods query,
			@ModelAttribute("page") PageEntity page) {
		ModelAndView mav = new ModelAndView("/eshop/euser/goodsManage.httl");
		
		try{
			EUser user= (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
			if(user==null){
				return new ModelAndView("login.httl");
			}
			mav.addObject("user", user);
			this.setPage(page);
			this.getPage().setPageSize(20);
			if(query == null){
				query = new ShopAndGoods();
			}
			List<ShopAndGoods> list = shopAndGoodsService.getShopperGoods(user.getId(), query, page);
			mav.addObject("query", query);
			mav.addObject("goodsList", list);
			mav.addObject("page", this.getPage());
		}catch(Exception e){
			logger.error("EUserController.goodsManage", e);
		}
		return mav;
	}
}
