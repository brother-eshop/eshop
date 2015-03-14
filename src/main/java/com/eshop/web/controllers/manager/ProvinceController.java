package  com.eshop.web.controllers.manager;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eshop.frameworks.core.controller.BaseController;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.Province;
import com.eshop.service.manager.ProvinceService;

@Controller
@RequestMapping("/manager/province")
public class ProvinceController extends BaseController {

	private static final Logger logger = Logger.getLogger(ProvinceController.class);

	@Autowired
	private ProvinceService provinceService;

	// 路径
	private String toList = "/manager/province/province_list.httl";// 产品表页
	private String toAdd = "/manager/province/province_add.httl";// 添加页面
	private String toEdit = "/manager/province/province_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request, HttpServletResponse response, Province query, @ModelAttribute("page") PageEntity page) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			this.setPage(page);
			this.getPage().setPageSize(20);
			if (query == null) {
				query = new Province();
			}
			List<Province> list = provinceService.getProvincePage(query, this.getPage());
			modelAndView.addObject("query", query);
			modelAndView.addObject("provinceList", list);
			modelAndView.addObject("page", this.getPage());
		} catch (Exception e) {
			logger.error("ProvinceController.listAll", e);
		}

		return modelAndView;
	}

	@RequestMapping(value="/add",method=RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		try {
		} catch (Exception e) {
			logger.error("ProvinceController.toAdd", e);
		}
		return modelAndView;
	}

	@RequestMapping(value="/add",method=RequestMethod.POST)
	public RedirectView add(Province province, HttpServletRequest request) {
		try {
			provinceService.addProvince(province);
		} catch (Exception e) {
			logger.error("ProvinceController.add", e);
		}
		return new RedirectView("/manager/province/list");
	}

	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public ModelAndView toEdit(Long id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		try {
			Province province = provinceService.getProvinceById(id);
			modelAndView.addObject(province);
		} catch (Exception e) {
			logger.error("ProvinceController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public RedirectView edit(Province province, HttpServletRequest request) {
		try {
			provinceService.updateProvinceByObj(province);

		} catch (Exception e) {
			logger.error("ProvinceController.edit", e);
		}
		return new RedirectView("/manager/province/list");
	}

	@RequestMapping("/delete")
	public RedirectView delete(String ids, HttpServletRequest request, Province query, @ModelAttribute("page") PageEntity page,RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/province/list");
		String[] idArray = ids.split(",");
		Province province = new Province();
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					this.provinceService.updateProvinceByObj(province);
				}
			}
			//attr.addAttribute("query", query);
			//attr.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("ProvinceController.delete", e);
		}
		return rv;
	}
	
	@ResponseBody
	@RequestMapping(value="/getProvinces",method=RequestMethod.POST)
	public List<Province> getProvinces(Province province, HttpServletRequest request){
		System.out.println("/////////////////"+province.getParentid());
		List<Province> provinces = provinceService.getProvinceListByObj(province);
		System.out.println(provinces.size());
		return provinces;
	}
}
