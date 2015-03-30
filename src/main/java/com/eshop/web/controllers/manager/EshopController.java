package com.eshop.web.controllers.manager;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;
import com.eshop.service.mongodb.EShopService;
import com.eshop.service.mongodb.EUserService;
import com.eshop.web.controllers.mongo.EUserController;

@Controller
@RequestMapping("/eshop")
public class EshopController extends BaseController {
	
	private static final Logger logger = Logger
			.getLogger(EUserController.class);
	
	@Autowired
	private EUserService euserService;
	
	@Autowired
	private EShopService eshopService;

	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mav = new ModelAndView("regist.httl");
		return mav;
	}
	
	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("login.httl");
		return mav;
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index.httl");
		return mav;
	}
	
	@RequestMapping("/startShop")
	public ModelAndView startShop(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("startShop.httl");
		EUser user= (EUser) this.getSessionAttribute(request, CoreConstant.USER_SESSION_NAME);
		mav.addObject("user", user);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public ModelAndView regist(EUser euser, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("redirect:/eshop/index");
		String captcha = (String) this.getSessionAttribute(request, CoreConstant.RAND_CODE);
		try{
			if(!euser.getCaptcha().equals(captcha)){
				modelAndView.addObject("user",euser);
				modelAndView.setViewName("regsit.httl");
				modelAndView.addObject("captcha_error", true);
				return modelAndView;
			}
			euser.setPassword(MD5.getMD5(euser.getPassword()));
			euser.setRegTime(new Date());
			List<EUser> users = euserService.getUserByObj(euser);
			if(users.size()>0){
				return new ModelAndView("regsit.httl");
			}else{
				euserService.insert(euser);
			}
		} catch (Exception e) {
			logger.error("EUserController.insert", e);
		}
		return modelAndView;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView login(EUser euser, HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("redirect:/eshop/index");
		EUser user = euserService.getByUserName(euser);
		String password = MD5.getMD5(euser.getPassword());
		String captcha = (String) this.getSessionAttribute(request, CoreConstant.RAND_CODE);
		if(user==null){
			mav.addObject("user",euser);
			mav.setViewName("login.httl");
			mav.addObject("name_error", true);
		}else if(!password.equals(user.getPassword())){
			mav.addObject("user",euser);
			mav.setViewName("login.httl");
			mav.addObject("password_error", true);
		}else if(!euser.getCaptcha().equals(captcha)){
			mav.addObject("user",euser);
			mav.setViewName("login.httl");
			mav.addObject("captcha_error", true);
		}else if(user!=null&&password.equals(user.getPassword())){
			this.setSessionAttribute(request, response,CoreConstant.USER_SESSION_NAME, user);
			mav.addObject("user",user);
			return mav;
		}
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/shopSub", method = RequestMethod.POST)
	public ModelAndView startShop(EShop eshop, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("redirect:/eshop/index");
		try{
			eshopService.insert(eshop);
		} catch (Exception e) {
			logger.error("EshopController.insert", e);
		}
		return modelAndView;
	}
}
