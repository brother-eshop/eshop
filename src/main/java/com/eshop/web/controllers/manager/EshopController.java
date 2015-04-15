package com.eshop.web.controllers.manager;

import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.EShopService;
import com.eshop.service.mongodb.EUserService;
import com.eshop.service.mongodb.ShopAndGoodsService;
import com.eshop.web.controllers.mongo.EUserController;

@Controller
public class EshopController extends BaseController {

	private static final Logger logger = Logger.getLogger(EUserController.class);

	@Autowired
	private EUserService euserService;

	@Autowired
	private EShopService eshopService;

	@Autowired
	private ShopAndGoodsService shopAndGoodsService;

	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mav = new ModelAndView("regist.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("login.httl");
		setVar(mav);
		return mav;
	}
	
	@RequestMapping("/agreement")
	public ModelAndView agreement(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("agreement.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("index.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("index.httl");
			return mav;
		}
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/startShop")
	public ModelAndView startShop(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("startShop.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			return new ModelAndView("login.httl");
		}
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/enterShop")
	public ModelAndView enterShop(EShop eshop, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/shop.httl");
		setVar(mav);
		try {
			EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
			if (user == null) {
				mav.setViewName("login.httl");
				return mav;
			}
			EShop shop = eshopService.getEShopByUser(eshop.getUserId());
			PageEntity page = new PageEntity();
			this.setPage(page);
			this.getPage().setPageSize(20);
			ShopAndGoods query = new ShopAndGoods();
			query.setStatus(1);
			List<ShopAndGoods> list = shopAndGoodsService.getShopperGoods(eshop.getUserId(), query, page);
			mav.addObject("query", query);
			mav.addObject("shopperGoods", list);
			mav.addObject("page", this.getPage());
			mav.addObject("user", user);
			mav.addObject("shop", shop);
		} catch (Exception e) {
			logger.error("EshopController.enterShop", e);
		}
		return mav;
	}

	@RequestMapping("/listGoods")
	public ModelAndView listGoods(HttpServletRequest request, HttpServletResponse response, ShopAndGoods query, @ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView("/shop.httl");
		setVar(modelAndView);
		try {
			EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
			EShop shop = eshopService.getEShopByUser(query.getUserId());
			if (user == null) {
				modelAndView.setViewName("login.httl");
				return modelAndView;
			}
			this.setPage(page);
			this.getPage().setPageSize(20);
			query.setStatus(1);
			List<ShopAndGoods> list = shopAndGoodsService.getShopperGoods(query.getUserId(), query, page);
			modelAndView.addObject("query", query);
			modelAndView.addObject("shopperGoods", list);
			modelAndView.addObject("page", this.getPage());
			modelAndView.addObject("shop", shop);
			modelAndView.addObject("user", user);
		} catch (Exception e) {
			logger.error("EshopController.listGoods", e);
		}

		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public ModelAndView regist(EUser euser, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("redirect:/index");
		String captcha = (String) this.getSessionAttribute(request, CoreConstant.RAND_CODE);
		try {
			if (!euser.getCaptcha().equals(captcha)) {
				modelAndView.addObject("user", euser);
				modelAndView.setViewName("regsit.httl");
				modelAndView.addObject("captcha_error", true);
				return modelAndView;
			}
			euser.setPassword(MD5.getMD5(euser.getPassword()));
			euser.setRegTime(new Date());
			List<EUser> users = euserService.getUserByObj(euser);
			if (users.size() > 0) {
				return new ModelAndView("regsit.httl");
			} else {
				euserService.insert(euser);
				this.setSessionAttribute(request, response, CoreConstant.USER_SESSION_NAME, euserService.getByUserName(euser));
			}
		} catch (Exception e) {
			logger.error("EUserController.insert", e);
		}
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public ModelAndView doLogin(EUser euser, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("redirect:/index");
		EUser user = euserService.getByUserName(euser);
		String password = MD5.getMD5(euser.getPassword());
		String captcha = (String) this.getSessionAttribute(request, CoreConstant.RAND_CODE);
		if (user == null) {
			mav.addObject("user", euser);
			mav.setViewName("login.httl");
			mav.addObject("name_error", true);
		} else if (!password.equals(user.getPassword())) {
			mav.addObject("user", euser);
			mav.setViewName("login.httl");
			mav.addObject("password_error", true);
		} else if (!euser.getCaptcha().equals(captcha)) {
			mav.addObject("user", euser);
			mav.setViewName("login.httl");
			mav.addObject("captcha_error", true);
		} else if (user != null && password.equals(user.getPassword())) {
			this.setSessionAttribute(request, response, CoreConstant.USER_SESSION_NAME, user);
			if (user.getIsShopper() != null && user.getIsShopper() == 1) {
				mav.setViewName("redirect:/euser/ucenter");
			}
			mav.addObject("user", user);
			return mav;
		}
		return mav;
	}

	@RequestMapping("/logout")
	public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
		this.clear(request, response);
		RedirectView r = new RedirectView("/login");
		return r;
	}

	@ResponseBody
	@RequestMapping(value = "/shopSub", method = RequestMethod.POST)
	public ModelAndView startShop(EShop eshop, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("redirect:/euser/addGoods");
		try {
			EUser user = (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
			if (user == null) {
				return new ModelAndView("login.httl");
			}
			modelAndView.addObject("user", user);
			user.setIsShopper(1);
			user.setShopName(eshop.getShopName());
			euserService.updateEUserShopper(user);
			eshop.setRegTime(new Date());
			eshopService.insert(eshop);
		} catch (Exception e) {
			logger.error("EshopController.insert", e);
		}
		return modelAndView;
	}

}
