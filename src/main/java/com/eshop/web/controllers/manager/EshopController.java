package com.eshop.web.controllers.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.model.mongodb.EUser;
import com.eshop.service.mongodb.EUserService;

@Controller
@RequestMapping("/eshop")
public class EshopController extends BaseController {
	
	@Autowired
	private EUserService euserService;

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
	public ModelAndView startShop() {
		ModelAndView mav = new ModelAndView("startShop.httl");
		return mav;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView login(EUser euser, HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("redirect:/eshop/index");
		EUser user = euserService.getByUserName(euser);
		String password = MD5.getMD5(euser.getPassword());
		if(user==null){
			mav.addObject("user",euser);
			mav.setViewName("login.httl");
			mav.addObject("name_error", "用户不存在");
		}else if(!password.equals(user.getPassword())){
			mav.addObject("user",euser);
			mav.setViewName("login.httl");
			mav.addObject("password_error", "用户密码错误");
		}else if(user!=null&&password.equals(user.getPassword())){
			this.setSessionAttribute(request, response,"USER_SESSION_NAME", user);
			mav.addObject("user",user);
			return mav;
		}
		return mav;
	}
}
