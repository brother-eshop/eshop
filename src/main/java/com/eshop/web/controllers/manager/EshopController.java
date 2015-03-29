package com.eshop.web.controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.service.manager.UserService;

@Controller
@RequestMapping("/eshop")
public class EshopController extends BaseController {
	@Autowired
	private UserService userService;

	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mav = new ModelAndView("regist.httl");
		return mav;
	}
}
