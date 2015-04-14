package com.eshop.web.controllers.common;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eshop.common.image.ImageScale;
import com.eshop.common.util.date.DateUtils;
import com.eshop.common.util.io.FileUtil;
import com.eshop.common.util.io.PropertyUtil;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/upload")
public class ImageUploadController {
	// 读取配置文件类
	public static PropertyUtil propertyUtil = PropertyUtil.getInstance("project");
	public ImageScale imageScale;

	@Autowired
	public void setImageScale(ImageScale imageScale) {
		this.imageScale = imageScale;
	}

	/**
	 * 上传图片方法
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = "/imagess")
	@ResponseBody
	public String img(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String dateStr = DateUtils.toString(new Date(), "yyyyMMdd");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile imgFile = multipartRequest.getFileMap().entrySet().iterator().next().getValue();
		String[] paths = FileUtil.getSavePathByRequest(request);
		JsonObject json = FileUtil.saveImage(imgFile, paths);
		String cut = request.getParameter("cut");
		String resize = request.getParameter("resize");
		if (json.get("error").getAsInt() != -1) {
			String newFileName = json.get("url").getAsString().substring(json.get("url").getAsString().lastIndexOf("/") + 1);
			String extension = FilenameUtils.getExtension(imgFile.getOriginalFilename());
			// File srcSource = new File(paths[0] + File.separator + "images" + File.separator + dateStr + File.separator + newFileName);
			File srcSource = new File(paths[0] + File.separator + newFileName);
			if (StringUtils.isNotEmpty(cut)) {
				File userDefineTempFile = new File(paths[0] + File.separator + "images" + File.separator + "cut" + File.separator + dateStr + File.separator
						+ newFileName + "." + extension);
				String[] temp = cut.split("\\|");
				String x = temp[0]; // 开始截取的x坐标
				String y = temp[1]; // 开始截取的y坐标
				String width = temp[2]; // 需要裁减图片的宽度
				String height = temp[3]; // 需要裁减图片高度
				if (StringUtils.isNotEmpty(x) && StringUtils.isNotEmpty(y) && StringUtils.isNotEmpty(width) && StringUtils.isNotEmpty(height)) { // 判断用户是否需要裁减照片
					ImageScale.cut(srcSource, userDefineTempFile, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height));
				}
				String userTempUrl = paths[1] + "/" + "images" + "/" + "cut" + "/" + dateStr + "/" + newFileName;
				json.addProperty("cutUrl", userTempUrl);
				srcSource = userDefineTempFile;
			}
			if (StringUtils.isNotEmpty(resize)) {
				String[] rlist = resize.split("\\|");
				for (String r : rlist) {
					String[] rs = r.split(":");
					if (StringUtils.isNotEmpty(rs[0]) && StringUtils.isNotEmpty(rs[1])) {
						File userDefineTempFile = new File(paths[0] + File.separator + "images" + File.separator + r.replace(":", "_") + File.separator
								+ newFileName);
						ImageScale.resizeFix(srcSource, userDefineTempFile, Integer.parseInt(rs[0]), Integer.parseInt(rs[1]));
						String userTempUrl = paths[1] + "/" + "images" + "/" + r.replace(":", "_") + "/" + newFileName;
						json.addProperty(r, userTempUrl);
					}
				}
			}
		}
		return json.toString();
	}
}