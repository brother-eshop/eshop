package com.eshop.web.controllers.mongo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.model.mongodb.EUser;
import com.eshop.service.mongodb.EUserService;

@Controller
@RequestMapping("/eshop/euser")
public class EUserController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(EUserController.class);

	@Autowired
	private EUserService euserService;
	@RequestMapping("/ucenter")
	public ModelAndView ucenter() {
		ModelAndView mav = new ModelAndView("/eshop/euser/ucenter.httl");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/getByUserName",method=RequestMethod.POST)
	public EUser getByUserName(EUser euser, HttpServletRequest request){
		return euserService.getByUserName(euser);
	}
	
	@ResponseBody
	@RequestMapping(value="/getByEmail",method=RequestMethod.POST)
	public EUser getByEmail(EUser euser, HttpServletRequest request){
		return euserService.getByEmail(euser);
	}
	
	@ResponseBody
	@RequestMapping(value="/getByMobile",method=RequestMethod.POST)
	public EUser getByMobile(EUser euser, HttpServletRequest request){
		return euserService.getByMobile(euser);
	}
	
//	@ResponseBody
//	@RequestMapping(value="/login",method=RequestMethod.POST)
//	public ModelAndView login(EUser euser, HttpServletRequest request,HttpServletResponse response){
//		ModelAndView mav = new ModelAndView("redirect:/eshop/euser/ucenter");
//		EUser user = euserService.getByUserName(euser);
//		String password = MD5.getMD5(euser.getPassword());
//		if(user==null){
//			mav.addObject("user",euser);
//			mav.setViewName("login.httl");
//			mav.addObject("name_error", true);
//		}else if(!password.equals(user.getPassword())){
//			mav.addObject("user",euser);
//			mav.setViewName("login.httl");
//			mav.addObject("password_error", true);
//		}else if(user!=null&&password.equals(user.getPassword())){
//			this.setSessionAttribute(request, response,"USER_SESSION_NAME", user);
//			mav.addObject("user",user);
//			return mav;
//		}
//		return mav;
//	}
}
