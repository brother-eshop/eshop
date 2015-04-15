package com.eshop.web.controllers.mongo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.constant.UserConstants;
import com.eshop.common.params.ConfirmParam;
import com.eshop.common.params.ResetPwdParams;
import com.eshop.common.util.date.DateUtils;
import com.eshop.common.util.security.Base64;
import com.eshop.common.util.security.MD5;
import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.EmailParam;
import com.eshop.frameworks.core.mail.MailSenderService;
import com.eshop.model.mongodb.ECartItem;
import com.eshop.model.mongodb.EOrder;
import com.eshop.model.mongodb.EOrderDetail;
import com.eshop.model.mongodb.EShop;
import com.eshop.model.mongodb.EUser;
import com.eshop.model.mongodb.EUserAddress;
import com.eshop.model.mongodb.Shipping;
import com.eshop.model.mongodb.ShopAndGoods;
import com.eshop.service.mongodb.ECartItemService;
import com.eshop.service.mongodb.EOrderDetailService;
import com.eshop.service.mongodb.EOrderService;
import com.eshop.service.mongodb.EShopService;
import com.eshop.service.mongodb.EUserAddressService;
import com.eshop.service.mongodb.EUserService;
import com.eshop.service.mongodb.ShippingService;
import com.eshop.service.mongodb.ShopAndGoodsService;

@Controller
@RequestMapping("/euser")
public class EUserController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(EUserController.class);
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private EUserService euserService;

	@Autowired
	private EShopService eshopService;

	@Autowired
	private ShippingService shippingService;

	@Autowired
	private ShopAndGoodsService shopAndGoodsService;

	@Autowired
	private ECartItemService ecartItemService;

	@Autowired
	private EUserAddressService euserAddressService;

	@Autowired
	private EOrderService eorderService;

	@Autowired
	private EOrderDetailService eorderDetailService;

	private Map<String, EShop> shopMap = new HashMap<String, EShop>();

	@RequestMapping("/ucenter")
	public ModelAndView ucenter(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/ucenter.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			return new ModelAndView("login.httl");
		}
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/resetPwd")
	public ModelAndView resetPwd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/resetPwd.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/commitePwd")
	public ModelAndView commitePwd(HttpServletRequest request,
			HttpServletResponse response, ResetPwdParams pwd) {
		ModelAndView mav = new ModelAndView("redirect:/login");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			return mav;
		}
		EUser reUser = euserService.getByUserName(user);
		if (MD5.getMD5(pwd.getOldPassword()).equals(reUser.getPassword())) {
			reUser.setPassword(MD5.getMD5(pwd.getNewPassword()));
			euserService.save(reUser);
			this.clear(request, response);
		} else {
			mav.setViewName("/euser/resetPwd.httl");
			mav.addObject("passwordError", true);
			return mav;
		}
		return mav;
	}

	@RequestMapping("/addGoods")
	public ModelAndView goAddGoods(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/addGoods.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/cart")
	public ModelAndView cart(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/cart.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		// List<ECartItem> items = geItems(user.getId());
		Map<String, List<ECartItem>> itemMap = getItemsMap(user.getId());
		List<EUserAddress> addresses = euserAddressService
				.getAddressByUserId(user.getId());
		mav.addObject("user", user);
		mav.addObject("itemMap", itemMap);
		mav.addObject("addressList", addresses);

		return mav;
	}

	@RequestMapping("/deleteCartItem")
	public ModelAndView deleteCartItem(HttpServletRequest request,
			ECartItem ecartItem) {
		ModelAndView mav = new ModelAndView("/euser/cart.httl");
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		setVar(mav);
		if (user == null) {
			return new ModelAndView("redirect:/login");
		}
		// List<ECartItem> items = geItems(user.getId());
		ecartItem = ecartItemService.findById(ecartItem.getId(),
				ECartItem.class);
		ecartItemService.remove(ecartItem);
		Map<String, List<ECartItem>> itemMap = getItemsMap(user.getId());
		List<EUserAddress> addresses = euserAddressService
				.getAddressByUserId(user.getId());
		mav.addObject("user", user);
		mav.addObject("itemMap", itemMap);
		mav.addObject("addressList", addresses);

		return mav;
	}

	@ResponseBody
	@RequestMapping("/getConfirm")
	public Map<String, Object> getConfirm(HttpServletRequest request,
			@RequestBody ConfirmParam cparam) {
		// List<ShopAndGoods> goodsList =
		// shopAndGoodsService.getGoodsByIds(idArray);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		EUserAddress euserAddress = euserAddressService
				.getEUserAddressById(cparam.getAdsId());
		EShop eshop = eshopService.getEShopByUser(cparam.getShopId());
		Double distance = this.distance(eshop.getLng(), eshop.getLat(),
				euserAddress.getLng(), euserAddress.getLat());// 商户和店铺的距离
		List<Shipping> sps = shippingService.getShippingByUser(cparam
				.getShopId());
		Shipping shipping = null;
		List<ECartItem> cartItems = ecartItemService.getSubItems(user.getId(),
				cparam.getShopId());
		// if(distance>eshop.getDevliverScope()){
		// returnMap.put("ERROR", "已经超出商家服务范围!");
		// System.out.println("已经超出商家服务范围");
		// return null;
		// }
		for (Shipping sp : sps) {
			if (sp.getRange() > distance) {
				continue;
			} else {
				shipping = sp;
				break;
			}
		}
		returnMap.put("address", euserAddress);
		returnMap.put("distance", distance);
		returnMap.put("shipping", shipping);
		returnMap.put("eshop", eshop);
		returnMap.put("ecartItems", cartItems);
		// System.out.println("距离"+distance);
		// System.out.println("距离规则："+shipping.getRange()+"满免"+shipping.getFreePrice()+"运费"+shipping.getShippingPrice());
		return returnMap;
	}

	// 获取两个百度坐标点之间的距离
	private double distance(double centerLon, double centerLat,
			double targetLon, double targetLat) {
		double jl_jd = 102834.74258026089786013677476285;// 每经度单位米;
		double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米;
		double b = Math.abs((centerLat - targetLat) * jl_jd);
		double a = Math.abs((centerLon - targetLon) * jl_wd);
		return Math.sqrt((a * a + b * b));
	}

	@RequestMapping("/subCart")
	public ModelAndView subCart(String shopId, HttpServletRequest request,
			String adsId) {
		ModelAndView mav = new ModelAndView("/euser/cart.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		EUserAddress euserAddress = euserAddressService
				.getEUserAddressById(adsId);
		EShop eshop = eshopService.getEShopByUser(shopId);
		Double distance = this.distance(eshop.getLng(), eshop.getLat(),
				euserAddress.getLng(), euserAddress.getLat());// 商户和店铺的距离
		List<Shipping> sps = shippingService.getShippingByUser(shopId);
		Shipping shipping = null;
		List<ECartItem> cartItems = ecartItemService.getSubItems(user.getId(),
				shopId);
		for (Shipping sp : sps) {
			if (sp.getRange() > distance) {
				continue;
			} else {
				shipping = sp;
				break;
			}
		}
		Double totalPrice = 0.0;
		EOrder order = new EOrder();
		order.setId(UUID.randomUUID().toString());
		order.setOrderNumber(System.currentTimeMillis() + "");
		order.setUserId(user.getId());
		order.setOrderTime(new Date());
		order.setShopperId(eshop.getUserId());
		order.setShopName(eshop.getShopName());
		order.setShopAddress(eshop.getShopAddress());
		order.setStatus(0);
		order.setOrderAddress(euserAddress.getAddress());
		order.setOrderReceiver(euserAddress.getReceiver());
		order.setReceiverMobile(euserAddress.getMobile());
		order.setUserLng(euserAddress.getLng());
		order.setUserLat(euserAddress.getLat());

		for (ECartItem item : cartItems) {
			EOrderDetail od = new EOrderDetail();
			od.setOrderId(order.getId());
			od.setOrderNumber(order.getOrderNumber());
			od.setGoodsId(item.getGoodsId());
			ShopAndGoods sad = shopAndGoodsService.findById(item.getGoodsId(),
					ShopAndGoods.class);
			od.setGoodsName(sad.getGoodsName());
			od.setOutPirce(sad.getOutPrice());
			od.setGoodsCount(item.getGoodsCount());
			od.setSubtotal(item.getGoodsCount() * sad.getOutPrice());
			totalPrice += od.getSubtotal();
			eorderDetailService.save(od);
			ecartItemService.remove(item);
		}
		if (shipping.getFreePrice() > totalPrice) {
			totalPrice += shipping.getShippingPrice();
		}
		order.setTotalPrice(totalPrice);
		eorderService.save(order);

		// List<ShopAndGoods> goodsList =
		// shopAndGoodsService.getGoodsByIds(idArray);
		// EUserAddress euserAddress =
		// euserAddressService.getEUserAddressById(adsId);

		return mav;
	}

	@RequestMapping("/shopManage")
	public ModelAndView shopManage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/shopinfo.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		EShop eshop = eshopService.getEShopByUser(user.getId());
		List<Shipping> shippings = shippingService.getShippingByUser(user
				.getId());
		mav.addObject("user", user);
		mav.addObject("eshop", eshop);
		mav.addObject("shippings", shippings);

		return mav;
	}

	@RequestMapping(value = "/saveEShop", method = RequestMethod.POST)
	public ModelAndView saveEShop(EShop eshop, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/euser/shopManage");
		setVar(modelAndView);
		try {
			// eshopService.updateByObj(eshop);
			eshopService.save(eshop);
		} catch (Exception e) {
			logger.error("EUserController.saveEShop", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/deleteShipping", method = RequestMethod.POST)
	public ModelAndView deleteShipping(Shipping shipping,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/euser/shopManage");
		setVar(modelAndView);
		try {
			// eshopService.updateByObj(eshop);
			shippingService.remove(shipping);
		} catch (Exception e) {
			logger.error("EUserController.updateShipping", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/updateShipping", method = RequestMethod.POST)
	public ModelAndView updateShipping(Shipping shipping,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/euser/shopManage");
		setVar(modelAndView);
		try {
			// eshopService.updateByObj(eshop);
			shippingService.save(shipping);
		} catch (Exception e) {
			logger.error("EUserController.updateShipping", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/saveShipping", method = RequestMethod.POST)
	public ModelAndView saveShipping(Shipping shipping,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/euser/shopManage");
		setVar(modelAndView);
		try {
			// eshopService.updateByObj(eshop);
			shippingService.insert(shipping);
		} catch (Exception e) {
			logger.error("EUserController.saveShipping", e);
		}
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/getShopps", method = RequestMethod.POST)
	public List<EShop> getShopps(@RequestBody EShop user,
			HttpServletRequest request, HttpServletResponse response) {
		List<EShop> shopps = new ArrayList<EShop>();
		try {
			shopps = eshopService.searchShopps(user.getLng(), user.getLat());
		} catch (Exception e) {
			logger.error("EUserController.saveShipping", e);
		}
		return shopps;
	}

	@ResponseBody
	@RequestMapping(value = "/addCart", method = RequestMethod.POST)
	public String addCart(@RequestBody ECartItem ecartItem,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			EUser user = (EUser) this.getSessionAttribute(request,
					CoreConstant.USER_SESSION_NAME);
			if (user == null) {
				return "FALSE";
			}
			ECartItem myItem = ecartItemService.getMyItem(
					ecartItem.getGoodsId(), user.getId());
			if (myItem != null) {
				myItem.setGoodsCount(myItem.getGoodsCount() + 1);
				ecartItemService.save(myItem);
			} else {
				ecartItem.setAddTime(new Date());
				ecartItem.setUserId(user.getId());
				ecartItem.setGoodsCount(1);
				ecartItem.setStatus(0);
				ecartItemService.save(ecartItem);
			}
		} catch (Exception e) {
			logger.error("EUserController.addCart", e);
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/saveAddress", method = RequestMethod.POST)
	public ModelAndView saveAddress(EUserAddress euserAddress,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("redirect:/euser/cart");
		setVar(modelAndView);
		try {
			EUser user = (EUser) this.getSessionAttribute(request,
					CoreConstant.USER_SESSION_NAME);
			if (user == null) {
				modelAndView.setViewName("redirect:/login");
				return modelAndView;
			}
			euserAddress.setUserId(user.getId());
			euserAddressService.save(euserAddress);
		} catch (Exception e) {
			logger.error("EUserController.saveAddress", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/subAddress", method = RequestMethod.POST)
	public ModelAndView subAddress(EUserAddress euserAddress,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("redirect:/euser/address");
		setVar(modelAndView);
		try {
			EUser user = (EUser) this.getSessionAttribute(request,
					CoreConstant.USER_SESSION_NAME);
			if (user == null) {
				modelAndView.setViewName("redirect:/login");
				return modelAndView;
			}
			euserAddress.setUserId(user.getId());
			euserAddressService.save(euserAddress);
		} catch (Exception e) {
			logger.error("EUserController.saveAddress", e);
		}
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/getMyECartItems", method = RequestMethod.POST)
	public List<ECartItem> getMyECartItems(HttpServletRequest request,
			HttpServletResponse response) {
		List<ECartItem> ecartItems = new ArrayList<ECartItem>();
		try {
			EUser user = (EUser) this.getSessionAttribute(request,
					CoreConstant.USER_SESSION_NAME);
			ecartItems = getMyItems(user.getId());
		} catch (Exception e) {
			logger.error("EUserController.getMyECartItems", e);
		}
		return ecartItems;
	}

	private Map<String, List<ECartItem>> getItemsMap(String userId) {
		Map<String, List<ECartItem>> ecartMap = new HashMap<String, List<ECartItem>>();
		List<ECartItem> ecartItems = ecartItemService.getItems(userId);
		ShopAndGoods goods = null;
		EShop eshop = null;
		for (ECartItem item : ecartItems) {
			goods = shopAndGoodsService.findById(item.getGoodsId(),
					ShopAndGoods.class);
			item.setGoods(goods);
			eshop = eshopService.getEShopByUser(item.getShopId());
			item.setEshop(eshop);
			if (!ecartMap.containsKey(eshop.getShopName())) {
				ecartMap.put(eshop.getShopName(), new ArrayList<ECartItem>());
			}
			ecartMap.get(eshop.getShopName()).add(item);
		}
		return ecartMap;
	}

	private List<ECartItem> getMyItems(String userId) {
		shopMap = new HashMap<String, EShop>();
		List<ECartItem> ecartItems = new ArrayList<ECartItem>();
		ecartItems = ecartItemService.getItems(userId);
		ShopAndGoods goods = null;
		EShop eshop = null;
		for (ECartItem item : ecartItems) {
			goods = shopAndGoodsService.findById(item.getGoodsId(),
					ShopAndGoods.class);
			item.setGoods(goods);
			eshop = eshopService.getEShopByUser(item.getShopId());
			if (!shopMap.containsKey(item.getShopId())) {
				shopMap.put(item.getShopId(), eshop);
			}
			item.setEshop(eshop);
		}
		return ecartItems;
	}

	@ResponseBody
	@RequestMapping(value = "/getByUserName", method = RequestMethod.POST)
	public EUser getByUserName(EUser euser, HttpServletRequest request) {
		return euserService.getByUserName(euser);
	}

	@ResponseBody
	@RequestMapping(value = "/getByEmail", method = RequestMethod.POST)
	public EUser getByEmail(EUser euser, HttpServletRequest request) {
		return euserService.getByEmail(euser);
	}

	@ResponseBody
	@RequestMapping(value = "/getByMobile", method = RequestMethod.POST)
	public EUser getByMobile(EUser euser, HttpServletRequest request) {
		return euserService.getByMobile(euser);
	}

	@RequestMapping("/myBills")
	public ModelAndView myBills(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/mybill.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		List<EOrder> orders = eorderService.findList(Criteria.where("userId")
				.is(user.getId()), EOrder.class);
		mav.addObject("user", user);
		mav.addObject("orders", orders);
		return mav;
	}

	@RequestMapping("/shopBills")
	public ModelAndView shopBills(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/shopbill.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		List<EOrder> orders = eorderService.findList(Criteria
				.where("shopperId").is(user.getId()), EOrder.class);
		mav.addObject("user", user);
		mav.addObject("orders", orders);
		return mav;
	}

	@RequestMapping("/address")
	public ModelAndView address(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/address.httl");
		setVar(mav);
		EUser user = (EUser) this.getSessionAttribute(request,
				CoreConstant.USER_SESSION_NAME);
		if (user == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		List<EUserAddress> addressList = euserAddressService
				.getAddressByUserId(user.getId());
		mav.addObject("user", user);
		mav.addObject("addressList", addressList);
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/getBillDetail", method = RequestMethod.POST)
	public EOrder getBillDetail(@RequestBody EOrder eorder,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			eorder = eorderService.findById(eorder.getId(), EOrder.class);
			List<EOrderDetail> details = eorderDetailService
					.getOrderDetail(eorder.getId());
			eorder.setDetails(details);
		} catch (Exception e) {
			logger.error("EUserController.getBillDetail", e);
		}
		return eorder;
	}

	// @ResponseBody
	// @RequestMapping(value="/login",method=RequestMethod.POST)
	// public ModelAndView login(EUser euser, HttpServletRequest
	// request,HttpServletResponse response){
	// ModelAndView mav = new ModelAndView("redirect:/euser/ucenter");
	// EUser user = euserService.getByUserName(euser);
	// String password = MD5.getMD5(euser.getPassword());
	// if(user==null){
	// mav.addObject("user",euser);
	// mav.setViewName("login.httl");
	// mav.addObject("name_error", true);
	// }else if(!password.equals(user.getPassword())){
	// mav.addObject("user",euser);
	// mav.setViewName("login.httl");
	// mav.addObject("password_error", true);
	// }else if(user!=null&&password.equals(user.getPassword())){
	// this.setSessionAttribute(request, response,"USER_SESSION_NAME", user);
	// mav.addObject("user",user);
	// return mav;
	// }
	// return mav;
	// }
	@RequestMapping("/merchantSocket")
	public ModelAndView webSocket(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/webSocket.httl");
		setVar(mav);
		return mav;
	}
	@RequestMapping("/findPwd")
	public ModelAndView findPwd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/findPwd.httl");
		setVar(mav);
		return mav;
	}
	
	@RequestMapping(value="/reset",method = RequestMethod.POST)
	public ModelAndView reset(EUser euser,String key,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/message/reset_success.httl");
		setVar(mav);
		if (checkParam(key)) {
			EUser user = euserService.findById(euser.getId(), EUser.class);
			user.setPassword(MD5.getMD5(euser.getPassword()));
			euserService.save(user);
		} else {
			mav.setViewName("/error/error_msg.httl");
			mav.addObject("msg", "无效连接");
			return mav;
		}
		return mav;
	}
	
	// 发送找回密码邮件
	@RequestMapping(value = "/sendResetPwdMail", method = RequestMethod.POST)
	public ModelAndView sendmail(EUser euser, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/message/send_pwd_success.httl");
		setVar(mav);
		EUser user = euserService.getByEmail(euser);
		try {
			if (user != null) {
				// 调取发送邮件接口
				String url = CoreConstant.contextPath+"/euser/reset_pwd/"+ encrypt(user.getEmail());
				EmailParam ep = new EmailParam();
				ep.setEmail(user.getEmail());
				ep.setSubject("宅速乐找回密码邮件");
				ep.setActiveUrl(url);
				ep.setTemplateName(UserConstants.TEMPLATE_GET_PWD_EMAIL);
				ep.setToEmail(user.getEmail());
				Map<String, String> model = new HashMap<String, String>();
				model.put("nickName", user.getUsername());
				model.put("userName", user.getEmail());
				model.put("activeUrl", ep.getActiveUrl());
				model.put("adminEmail", UserConstants.ADMIN_EMAIL);
				model.put("nowdate",
						DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				model.put("img", CoreConstant.imagesPath);
				model.put("ctx", CoreConstant.contextPath);
				mailSenderService.sendWithTemplate(ep, model);
				user.setIsSendPwdMail(true);
				euserService.save(user);
				return mav;
			} else {
				mav.setViewName("/error/error_msg.httl");
				mav.addObject("msg", "邮箱输入错误");
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	//验证找回密码key，并跳转到重设密码页面
	@RequestMapping(value = "/reset_pwd/{key}", method = RequestMethod.GET)
	public ModelAndView sendmail(@PathVariable(value = "key") String key,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/euser/reset_pwd.httl");
		setVar(mav);
		try {
			if (checkParam(key)) {
				String email = getNameFromP(key);
				EUser user = euserService.getByEmail(email);
				if(user.getIsSendPwdMail()){
					user.setIsSendPwdMail(false);
					euserService.save(user);
					mav.addObject("user",user);
					mav.addObject("key",key);
				}else{
					mav.setViewName("/error/error_msg.httl");
					mav.addObject("msg", "无效连接");
					return mav;
				}
			} else {
				mav.setViewName("/error/error_msg.httl");
				mav.addObject("msg", "无效连接");
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	private String encrypt(String name) {
		String str = name + ":||:" + System.currentTimeMillis();
		String md5 = MD5.getMD5(str + UserConstants.MD5_KEY);
		String result = null;
		try {
			result = Base64.encodeURL(str + ":||:" + md5);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean checkParam(String p) {
		if (p == null)
			return false;
		String pStr = "";
		try {
			pStr = Base64.decodeURL(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] arr = pStr.split(":\\|\\|:");
		return arr.length == 3
				&& (System.currentTimeMillis() - Long.parseLong(arr[1])) <= (60L * 1000 * 60 * 24 * 2)
				&& MD5.getMD5(arr[0] + ":||:" + arr[1] + UserConstants.MD5_KEY)
						.equals(arr[2]);

	}

	private String getNameFromP(String p) {
		String pStr = "";
		try {
			pStr = Base64.decodeURL(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pStr.split(":\\|\\|:")[0];
	}
}
