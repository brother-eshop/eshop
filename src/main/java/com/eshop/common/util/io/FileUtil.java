package com.eshop.common.util.io;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.eshop.common.image.ImageHelper;
import com.eshop.common.util.date.DateUtils;
import com.google.gson.JsonObject;

public class FileUtil {

	public static int CUS_PHOTO_WIDTH = 150;
	public static int CUS_PHOTO_HEIGHT = 150;
	private static Logger logger = Logger.getLogger(FileUtil.class);
	// 读取配置文件类
	public static PropertyUtil propertyUtil = PropertyUtil.getInstance("project");
	private static final String pathfix = propertyUtil.getProperty("file.pathfix");//
	// 统一传到pathfix下
	private static final String importroot = propertyUtil.getProperty("import.root");
	private static final String rootpath = propertyUtil.getProperty("file.root");
	private static final String tempPath = "temp";// 临时目录
	/**
	 * @param file
	 *            文件
	 * @param paths
	 *            路径 0物理 1 URL
	 * @returned JsonObject
	 */
	public static JsonObject saveFile(MultipartFile file, String[] paths) {
		JsonObject obj = new JsonObject();
		try {
			// 存储物理路径
			String savePath = paths[0];
			// 返回的url
			String urlPath = paths[1];
			String newFileName = "";// 新的文件名
			String upFileName = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(upFileName)) {
				if (!checkName(upFileName)) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件扩展名不允许。\n只允许 doc,zip,docx,pdf,txt格式。");
					return obj;
				}
				// 文件大小不能超过5M
				if (file.getSize() > 5242880) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件大小不能超过5M");
					return obj;
				}
				// 新文件名
				newFileName = getRandomFileNameString(upFileName);
				// 锁结束
				File isD = new File(savePath);
				// 校验如果目录不存在，则创建目录
				if (!isD.isDirectory()) {
					isD.mkdirs();
				}
				if (!isD.exists()) {
					synchronized (FileUtil.class) {
						isD.mkdirs();
					}
				}
				String saveFilename = savePath + File.separator + newFileName;
				// 保存文件
				file.transferTo(new File(saveFilename));

				obj.addProperty("error", 0);
				urlPath = urlPath + "/" + saveFilename;

				obj.addProperty("url", saveFilename);
			} else {
				obj.addProperty("error", -1);
				obj.addProperty("message", "文件名为空");
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++upload kindeditor  error", e);
			obj.addProperty("error", -1);
			obj.addProperty("message", "上传异常，请稍后再试");
			return obj;
		}
	}

	/**
	 * @param imgFile
	 *            文件
	 * @param paths
	 *            路径 0物理 1 URL
	 * @returned JsonObject
	 */
	public static JsonObject saveImage(MultipartFile imgFile, String[] paths) {
		JsonObject obj = new JsonObject();
		try {
			// 存储物理路径
			String savePath = paths[0];
			// 返回的url
			String urlPath = paths[1];
			String newFileName = "";// 新的文件名
			String upFileName = imgFile.getOriginalFilename();
			// 保存第一张图片
			if (StringUtils.isNotEmpty(upFileName)) {
				if (!checkFileName(upFileName)) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件扩展名不允许。\n只允许 gif,jpg,jpeg,png,bmp格式。");
					return obj;
				}
				// 文件大小不能超过5M
				if (imgFile.getSize() > 5242880) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件大小不能超过5M");
					return obj;
				}
				// 新文件名
				newFileName = getRandomFileNameString(upFileName);
				// 锁结束
				File isD = new File(savePath);
				// 校验如果目录不存在，则创建目录
				if (!isD.isDirectory()) {
					isD.mkdirs();
				}
				if (!isD.exists()) {
					synchronized (FileUtil.class) {
						isD.mkdirs();
					}
				}
				String saveFilename = savePath + File.separator + newFileName;
				// 保存文件
				imgFile.transferTo(new File(saveFilename));
				obj.addProperty("error", 0);
				urlPath = urlPath + "/" + newFileName;
				obj.addProperty("url", urlPath);
			} else {
				obj.addProperty("error", -1);
				obj.addProperty("message", "文件名为空");
			}

			return obj;
		} catch (Exception e) {
			logger.error("+++upload images error", e);
			obj.addProperty("error", -1);
			obj.addProperty("message", "上传异常，请稍后再试");
			return obj;
		}

	}

	public static JsonObject saveImage(MultipartFile imgFile, String[] paths, boolean isLogo) {
		JsonObject obj = new JsonObject();
		try {
			// 存储物理路径
			String savePath = paths[0];
			// 返回的url
			String urlPath = paths[1];
			String newFileName = "";// 新的文件名
			String upFileName = imgFile.getOriginalFilename();
			// 保存第一张图片
			if (StringUtils.isNotEmpty(upFileName)) {
				if (!checkFileName(upFileName)) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件扩展名不允许。\n只允许 gif,jpg,jpeg,png,bmp格式。");
					return obj;
				}
				// 文件大小不能超过5M
				if (imgFile.getSize() > 5242880) {
					obj.addProperty("error", -1);
					obj.addProperty("message", "上传文件大小不能超过5M");
					return obj;
				}
				// 新文件名
				newFileName = getRandomFileNameString(upFileName);
				// 锁结束
				File isD = new File(savePath);
				// 校验如果目录不存在，则创建目录
				if (!isD.isDirectory()) {
					isD.mkdirs();
				}
				if (!isD.exists()) {
					synchronized (FileUtil.class) {
						isD.mkdirs();
					}
				}
				String saveFilename = savePath + File.separator + newFileName;
				// 保存文件
				imgFile.transferTo(new File(saveFilename));
				if (isLogo) {
					pressImage(saveFilename, propertyUtil.getProperty("file.root") + "logopng.png", 1, 1, 0.7f);// 加水印
				}
				obj.addProperty("error", 0);
				urlPath = urlPath + "/" + newFileName;
				obj.addProperty("url", urlPath);
			} else {
				obj.addProperty("error", -1);
				obj.addProperty("message", "文件名为空");
			}

			return obj;

		} catch (Exception e) {
			logger.error("+++upload kindeditor images error", e);
			obj.addProperty("error", -1);
			obj.addProperty("message", "上传异常，请稍后再试");
			return obj;
		}

	}

	/**
	 * 获得随机的数字为文件名，有效防止文件名重读
	 * @param fileName
	 *            传来的文件名
	 * @param cusid
	 *            用户id
	 * @return 返回新的文件名
	 */
	public static String getRandomFileNameString(String fileName) {
		StringBuffer buffer = new StringBuffer();
		// 加锁为防止文件名重复
		final Lock lock = new ReentrantLock();
		lock.lock();
		try {
			buffer.append(System.currentTimeMillis());// 当前时间
			// 增加6位随机的数字
			buffer.append(getRandomString(6));
			// 添加后缀名
			buffer.append(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
		} finally {
			lock.unlock();
		}
		return buffer.toString();
	};

	/**
	 * 获取指定长度的随机数字
	 * @param len
	 * @return
	 */
	public static String getRandomString(int len) {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			buffer.append(random.nextInt(10));
			random = new Random();
		}
		return buffer.toString();
	}

	public static boolean checkName(String fileName) {
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String> asList(extMap.get("file").split(",")).contains(fileExt)) {
			return false;
		}
		return true;
	}

	/**
	 * 检查是否是符合的后缀名
	 * @param fileName
	 * @return
	 */
	public static boolean checkFileName(String fileName) {
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		/*
		 * 现在只有图片 extMap.put("flash", "swf,flv"); extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb"); extMap.put("file",
		 * "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		 */
		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String> asList(extMap.get("image").split(",")).contains(fileExt)) {
			return false;
		}
		return true;
	}

	/**
	 * 获得存储的物理路径
	 * @param request
	 * @return
	 */
	public static String[] getSavePathByRequest(HttpServletRequest request) {

		String base = request.getParameter("base");// 区分项目的变量 如：恒企、仁和等区分
		String param = request.getParameter("param");// 区分模块的变量 如：考试、商品、课程等区分
		String dateStr = DateUtils.toString(new Date(), "yyyyMMdd");

		if (StringUtils.isEmpty(base)) {
			base = "eshop";// 临时，未传次参数的项目统一到yizhilu目录下
		}
		if (StringUtils.isEmpty(param)) {
			param = "common";// 临时，未传的项目统一到common目录下
		}
		// 拼凑 存储物理路径
		String savePath = propertyUtil.getProperty("file.root") + pathfix + "/" + base + "/" + param + "/" + dateStr;
		// 拼凑 url
		String urlPath = "/" + pathfix + "/" + base + "/" + param + "/" + dateStr;

		String[] result = new String[] { savePath, urlPath };
		return result;
	}

	/**
	 * 获得存储的临时物理路径放到tempxia
	 * @param request
	 * @return
	 */
	public static String[] getTempSavePathByRequest(HttpServletRequest request) {

		String base = request.getParameter("base");// 区分项目的变量 如：恒企、仁和等区分
		String param = request.getParameter("param");// 区分模块的变量 如：考试、商品、课程等区分
		String dateStr = DateUtils.toString(new Date(), "yyyyMMdd");

		if (StringUtils.isEmpty(base)) {
			base = "eshop";// 临时，未传次参数的项目统一到yizhilu目录下
		}
		if (StringUtils.isEmpty(param)) {
			param = "common";// 临时，未传的项目统一到common目录下
		}
		// 拼凑 存储物理路径
		String savePath = propertyUtil.getProperty("file.root") + pathfix + "/" + tempPath + "/" + base + "/" + param + "/" + dateStr;
		// 拼凑 url
		String urlPath = "/" + pathfix + "/" + tempPath + "/" + base + "/" + param + "/" + dateStr;

		String[] result = new String[] { savePath, urlPath };
		return result;

	}

	/**
	 * @param photoPath
	 *            图片路径
	 * @param imageWidth
	 *            图片宽地
	 * @param imageHeight
	 *            图片高度
	 * @param cutLeft
	 *            左 坐标
	 * @param cutTop
	 *            顶 坐标
	 * @return
	 */
	public static JsonObject saveCutImage(String photoPath, int imageWidth, int imageHeight, int cutLeft, int cutTop, int dropWidth, int dropHeight) {
		JsonObject obj = new JsonObject();
		// 新文件名
		String newPhotoName = getRandomFileNameString(photoPath);
		Rectangle rec = createPhotoCutRec(imageWidth, imageHeight, cutLeft, cutTop, dropWidth, dropHeight);
		photoPath = photoPath.replace(importroot, rootpath);
		File tempPic = new File(photoPath);
		// 新文件路径
		newPhotoName = photoPath.substring(0, photoPath.lastIndexOf("/")) + newPhotoName;
		newPhotoName = newPhotoName.replace(tempPath, "customer");
		File file = new File(newPhotoName);
		try {

			saveSubImage(tempPic, file, rec, new int[] { imageWidth, imageHeight, cutLeft, cutTop, dropWidth, dropHeight });
			// 返回的地址
			obj.addProperty("url", newPhotoName.replace(rootpath, ""));
			obj.addProperty("error", 0);
		} catch (IOException e) {
			e.printStackTrace();
			obj.addProperty("error", -1);
			obj.addProperty("message", "上传文件大小不能超过5M");
		}

		return obj;
	};

	private static Rectangle createPhotoCutRec(int imageWidth, int imageHeight, int cutLeft, int cutTop, int dropWidth, int dropHeight) {
		int recX = cutLeft > 0 ? cutLeft : 0;
		int recY = cutTop > 0 ? cutTop : 0;
		int recWidth = dropWidth;
		int recHieght = dropHeight;
		if (cutLeft < 0) {
			// 注意curLeft 是负数
			if (imageWidth - cutLeft > dropWidth) {
				recWidth = dropWidth + cutLeft;
			} else {
				recWidth = imageWidth;
			}
		} else {
			if (imageWidth - cutLeft < dropWidth) {
				recWidth = imageWidth - cutLeft;
			}
		}
		if (cutTop < 0) {
			// 注意curLeft 是负数
			if (imageHeight - cutTop > dropHeight) {
				recHieght = dropHeight + cutTop;
			} else {
				recHieght = imageHeight;
			}
		} else {

			if (imageHeight - cutTop < dropHeight) {
				recHieght = imageHeight - cutTop;
			}
		}
		return new Rectangle(recX, recY, recWidth, recHieght);
	}

	private static void saveSubImage(File srcImageFile, File descDir, Rectangle rect, int[] intParms) throws IOException {
		ImageHelper.cut(srcImageFile, descDir, rect, intParms);
	}

	/**
	 * 读取file文件的大小
	 * @param f
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 添加图片水印
	 * @param targetImg
	 *            目标图片路径，如：C://myPictrue//1.jpg
	 * @param waterImg
	 *            水印图片路径，如：C://myPictrue//logo.png
	 * @param x
	 *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 */
	public final static void pressImage(String targetImg, String waterImg, int x, int y, float alpha) {
		try {
			File file = new File(targetImg);
			if (file.exists()) {
				Image image = ImageIO.read(file);
				int width = image.getWidth(null);
				int height = image.getHeight(null);
				BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, width, height, null);

				Image waterImage = ImageIO.read(new File(waterImg)); // 水印文件
				int width_1 = waterImage.getWidth(null);
				int height_1 = waterImage.getHeight(null);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

				int widthDiff = width - width_1;
				int heightDiff = height - height_1;
				if (x < 0) {
					x = widthDiff / 2;
				} else {
					x = widthDiff - 10;
				}
				if (x < 0 || x > widthDiff) {
					x = widthDiff;
				}

				if (y < 0) {
					y = heightDiff / 2;
				} else {
					y = heightDiff - 5;
				}
				if (y < 0 || y > heightDiff) {
					y = heightDiff;
				}
				g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
				g.dispose();
				ImageIO.write(bufferedImage, "jpg", file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
