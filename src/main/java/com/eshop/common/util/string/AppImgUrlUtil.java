package com.eshop.common.util.string;

import java.util.List;

import com.eshop.common.util.io.PropertyUtil;

public class AppImgUrlUtil {
	public static  String imgctx;
	public static  String appListIconSize;//列表页，内容页图尺寸
	public static  String appFocusNewsImgSize;//焦点图尺寸
	
	public static PropertyUtil propertyUtil = PropertyUtil.getInstance("project");
	
	static{
		try{
			imgctx = propertyUtil.getProperty("imagesPath");
			appListIconSize = propertyUtil.getProperty("appListIconSize");
			appFocusNewsImgSize = propertyUtil.getProperty("appFocusImgNewsSize");
			if(imgctx == null){
				imgctx = "";
			}
		}catch(Exception ex){
			imgctx = "";
			appListIconSize = "";
			appFocusNewsImgSize = "";
			ex.printStackTrace();
		}
	}
	
	//转换列表页，内容页图尺寸
	public static String convertListIcon(String url){
		String rtn = url;
		if(rtn != null && ! rtn.equals("") && imgctx != null && ! "".equals(imgctx)){
			
			if(rtn.startsWith(imgctx)){
				rtn = rtn.replace(imgctx, "");
			}
			if(rtn.startsWith("http://")){
				//如果是外部网站的图片，直接返回
			}else{
				rtn = "/resize_" + appListIconSize + rtn;
				rtn = imgctx + rtn;
			}
		}
		return rtn;
	}
	
	//转换列表页，内容页图尺寸
	public static String convertFocusNewsImgSize(String url){
		String rtn = url;
		if(rtn != null && ! rtn.equals("") && imgctx != null && ! "".equals(imgctx)){
			if(rtn.startsWith(imgctx)){
				rtn = rtn.replace(imgctx, "");
			}
			if(rtn.startsWith("http://")){
				//如果是外部网站的图片，直接返回
			}else{
				rtn = "/resize_" + appFocusNewsImgSize + rtn;
				rtn = imgctx + rtn;
			}
		}
		return rtn;
	}

	//将内容中的所有图片url转换成相应的格式
	public static String convertContentImgs(String content){
		String rtn = content;
		List<String> originalImgList = StringUtils.getImgSrcUrl(content);
		if(originalImgList != null && originalImgList.size() > 0){
			String newImgUrl;
			for(String original : originalImgList){
				if(original != null && ! original.equals("")){
					if(original.startsWith("src=\"")){
						original = original.replace("src=\"", "");
					}
					if(original.startsWith("src='")){
						original = original.replace("src='", "");
					}
					if(original.endsWith("\"")){
						original = original.substring(0,original.length() - 1);
					}
					if(original.endsWith("'")){
						original = original.substring(0,original.length() - 1);
					}
					newImgUrl = convertFocusNewsImgSize(original);
					rtn = content.replace(original, newImgUrl);
				}
			}
		}
		return rtn;
	}
}
