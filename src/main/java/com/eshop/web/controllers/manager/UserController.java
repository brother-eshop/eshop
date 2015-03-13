package com.eshop.web.controllers.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.User;
import com.eshop.service.manager.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/manager/user")
public class UserController extends BaseController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	// 路径
	private String toList = "/manager/user/user_list.httl";// 产品表页
	private String toAdd = "/manager/user/user_add.httl";// 添加页面
	private String toEdit = "/manager/user/user_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request,
			HttpServletResponse response, User query,
			@ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new User();
			}
			List<User> list = userService.getUserPage(query, this.getPage());
			modelAndView.addObject("query", query);
			modelAndView.addObject("userList", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("UserController.listAll", e);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		try {
		} catch (Exception e) {
			logger.error("UserController.toAdd", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public RedirectView add(User user, HttpServletRequest request) {
		try {
			user.setPassword(MD5.getMD5(user.getPassword()));
			userService.addUser(user);
		} catch (Exception e) {
			logger.error("UserController.add", e);
		}
		return new RedirectView("/manager/user/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView toEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		try {
			User user = userService.getUserById(id);
			modelAndView.addObject(user);
		} catch (Exception e) {
			logger.error("UserController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public RedirectView edit(User user, HttpServletRequest request) {
		try {
			user.setPassword(MD5.getMD5(user.getPassword()));
			userService.updateUserByObj(user);
		} catch (Exception e) {
			logger.error("UserController.edit", e);
		}
		return new RedirectView("/manager/user/list");
	}

	@RequestMapping("/delete")
	public RedirectView delete(String ids, HttpServletRequest request,
			User query, @ModelAttribute("page") PageEntity page,
			RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/user/list");
		String[] idArray = ids.split(",");
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					this.userService.deleteUserById(Long.parseLong(id));
				}
			}
		} catch (Exception e) {
			logger.error("UserController.delete", e);
		}
		return rv;
	}
}
