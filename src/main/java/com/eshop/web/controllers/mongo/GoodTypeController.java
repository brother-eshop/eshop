package com.eshop.web.controllers.mongo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.eshop.model.manager.User;
import com.eshop.model.mongodb.GoodType;
import com.eshop.service.mongodb.GoodTypeService;

@Controller
@RequestMapping("/manager/good_type")
public class GoodTypeController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(GoodTypeController.class);

	@Autowired
	private GoodTypeService goodTypeService;
	
	// 路径
	private String toList = "/manager/good_type/good_type_list.httl";// 产品表页
	private String toAdd = "/manager/good_type/good_type_add.httl";// 添加页面
	private String toEdit = "/manager/good_type/good_type_edit.httl";// 修改页

	@RequestMapping("/list")
	public ModelAndView listAll(HttpServletRequest request,
			HttpServletResponse response, GoodType query) {
		ModelAndView modelAndView = new ModelAndView(toList);
		try {
			if (query == null) {
				query = new GoodType();
			}
			List<GoodType> list = goodTypeService.getGoodTyperPage(query);
			modelAndView.addObject("query", query);
			modelAndView.addObject("goodTypeList", list);
		} catch (Exception e) {
			logger.error("GoodTypeController.listAll", e);
		}

		return modelAndView;
	}
	

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		ModelAndView modelAndView = new ModelAndView(toAdd);
		try {
		} catch (Exception e) {
			logger.error("GoodTypeController.toAdd", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public RedirectView add(GoodType goodType, HttpServletRequest request) {
		try {
			goodTypeService.insert(goodType);
		} catch (Exception e) {
			logger.error("GoodTypeController.add", e);
		}
		return new RedirectView("/manager/good_type/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView toEdit(String id) {
		ModelAndView modelAndView = new ModelAndView(toEdit);
		try {
			GoodType goodType = goodTypeService.getByid(id);
			modelAndView.addObject(goodType);
		} catch (Exception e) {
			logger.error("UserController.toEdit", e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public RedirectView edit(GoodType goodType, HttpServletRequest request) {
		try {
			goodTypeService.updateGoodType(goodType);
		} catch (Exception e) {
			logger.error("GoodTypeController.edit", e);
		}
		return new RedirectView("/manager/good_type/list");
	}

	@RequestMapping("/delete")
	public RedirectView delete(String ids, HttpServletRequest request,
			User query, @ModelAttribute("page") PageEntity page,
			RedirectAttributes attr) {
		RedirectView rv = new RedirectView("/manager/good_type/list");
		String[] idArray = ids.split(",");
		try {// 软删除状态设置为2
			for (String id : idArray) {
				if (!"".equals(id)) {
					goodTypeService.deleteGoodType(id);
				}
			}
		} catch (Exception e) {
			logger.error("GoodTypeController.delete", e);
		}
		return rv;
	}
	
	@ResponseBody
	@RequestMapping(value="/getChildren",method=RequestMethod.POST)
	public List<GoodType> getChildren(GoodType goodType, HttpServletRequest request){
		return goodTypeService.getGoodTypeChildren(goodType);
	}
	
	static int insNum=1;
	static int typeLevel = 1;
	static String parentCode="";
	static String parentPath = "";
	
	@RequestMapping(value="/insertNewTyps",method=RequestMethod.POST)
	public void insertNewTyps(HttpServletRequest request){
		File file = new File("E:\\goods_type\\");
		for(File f : file.listFiles()){
			GoodType type = new GoodType();
			parentCode = insNum+"";
			parentPath = ":"+insNum+":";
			type.setCode(insNum+"");
			type.setName(f.getName());
			type.setPid("0");
			type.setPath(":"+insNum+":");
			GoodType t = goodTypeService.getByName(f.getName());
			System.out.println(t);
			System.out.println(t==null);
			if(t==null){
				goodTypeService.insert(type);
				System.out.println("insert===code is "+parentCode+"、name is "+f.getName()+"、path is "+parentPath);
			}else{
				parentCode = t.getCode();
				parentPath = t.getPath();
			}
			insertType(f.listFiles());
			insNum++;
		}
		
		
	}
	private void insertType(File[] files){
		int thisNum = 1;
		for(File file : files){
			if(!file.isDirectory()){
				continue;
			}
			GoodType type = new GoodType();
			GoodType parentType = goodTypeService.getByName(file.getParentFile().getName());
			if(parentType!=null){
				parentCode = parentType.getCode();
				parentPath = parentType.getPath();
			}
			String myCode = parentCode+"0"+thisNum;
			if(thisNum>9){
				myCode = parentCode+thisNum;
			}
			type.setCode(myCode);
			type.setName(file.getName());
			String myPath = parentPath+myCode+":";
			type.setPath(parentPath+myCode+":");
			type.setPid(parentCode);
			GoodType insType = goodTypeService.getByName(file.getName());
			if(insType!=null){
				continue;
			}
			goodTypeService.insert(type);
			System.out.println("insert===code is "+myCode+"、name is "+file.getName()+"、path is "+myPath);
			thisNum++;
			insertType(file.listFiles());
		}
	}
	
	private static Map<String,String> m = new HashMap<String,String>();
	public static void main(String[] args) {
		File file = new File("E:\\goods_type\\");
		
		//------------------导入商品
		getGoods(file);
		
		//-----------------商品分类
//		for(File f : file.listFiles()){
//			GoodType type = new GoodType();
//			parentCode = insNum+"";
//			parentPath = ":"+insNum+":";
//			type.setCode(parentCode);
//			type.setName(f.getName());
//			type.setPid("0");
//			type.setPath(parentPath);
//			insNum++;
//			System.out.println(parentCode+"=="+f.getName()+parentPath);
//			m.put(f.getName(), parentCode);
//			System.out.println(f.getName());
//			m.put(f.getName()+"_path", parentPath);
//			get(f.listFiles());
//		}
		
	}
	
	
	public static void getGoods(File file){
		if(file.isFile()){//如果是文件
			String parengName = file.getParentFile().getName();
			System.out.println(file.getName());
			
		}
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				getGoods(f);
			}
		}
		
	}
	
	private static void get(File[] files){
		int thisNum = 1;
		for(File file : files){
			if(!file.isDirectory()){
				continue;
			}
			
			String pcode = m.get(file.getParentFile().getName());
			String ppath = m.get(file.getParentFile().getName()+"_path");
			
			GoodType type = new GoodType();
			type.setCode(pcode+"0"+thisNum);
			m.put(file.getName(), pcode+"0"+thisNum);
			type.setName(file.getName());
			type.setPath(ppath+ppath+":0"+thisNum+":");
			m.put(file.getName()+"_path", ppath+pcode+"0"+thisNum+":");
			type.setPid(pcode);
			System.out.println(pcode+"0"+thisNum+"=="+file.getName()+"=="+ppath+pcode+"0"+thisNum+":");
			thisNum++;
			get(file.listFiles());
		}
	}
}
