package com.eshop.web.controllers.manager;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.model.manager.User;
import com.eshop.service.manager.UserService;

//@Controller
//@RequestMapping("/manager")
public class MainController extends BaseController {
	@Autowired
	private UserService userService;

	@RequestMapping("/index")
	public String main() {
		return "manager/layouts/main.httl";
	}

	@RequestMapping("/left")
	public ModelAndView left(HttpServletRequest request, @RequestParam("pid") Long pid) {
		ModelAndView mav = new ModelAndView("manager/layouts/left.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping("/index1")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("manager/index.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mav = new ModelAndView("regist.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("manager/layouts/top.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView tologin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("manager/login.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping(value = "/regsiter", method = RequestMethod.GET)
	public ModelAndView toregsiter(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("manager/regsiter.httl");
		setVar(mav);
		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, @ModelAttribute("user") User user, HttpServletResponse response) {
		user.setPassword(MD5.getMD5(user.getPassword()));
		user = userService.getUserByObj(user);
		System.out.println(user);
		ModelAndView mav = new ModelAndView("redirect:/manager/index");
		if (user != null) {
			// TODO CoreConstant.USER_SESSION_NAME 报错，所以直接填写了。
			this.setSessionAttribute(request, response, "USER_SESSION_NAME", user);
			mav.addObject(user);
			return mav;
		} else {
			mav.setViewName("manager/login.httl");
			setVar(mav);
			mav.addObject("error", "用户名或密码错误！");
		}
		return mav;
	}

	/**
	 * 退出系统操作
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
		this.clear(request, response);
		RedirectView r = new RedirectView("/manager/login");
		return r;
	}
}
