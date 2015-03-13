package com.eshop.common.util.string;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class StringUtils {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	// 去除编辑器保存后的所有html标签
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private final static String[] static_ext = { "jpg", "png", "gif", "bmp", "JPG", "PNG", "GIF", "BMP" };
	private final static String regxpForHtml = "<([^>]*)>";
	private final static String REG_IMG = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签
	private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性
	private final static String regxpForImaTagSrcUrl = "src\\s*=\\s*['\"]([^'\"]+)['\"]"; // 找出IMG标签的SRC属性
	private final static int date_format_limit = 30;
	private final static String URL_REGEX = "((https|http|ftp)?://)?" + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|" + "([0-9a-z_!~*'()\\-]+\\.)*"
			+ "([0-9a-z][0-9a-z\\-]{0,61})?[0-9a-z]\\." + "[a-z]{2,6})" + "(:[0-9]{1,4})?" + "(/[0-9a-z_!~*'()\\.;\\?:@&=+$,%#\\-]+)*/?";
	private final static Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);
	public final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {

			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {

			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	// MD5加密
	public static String encodeByMD5(String originString) {

		if (originString != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(originString.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	private static String byteArrayToHexString(byte[] b) {

		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {

		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// 时间对比
	public static boolean BalanceDate(String day1, String day2) throws Exception {

		boolean flag = true;
		DateFormat df = DateFormat.getDateInstance();
		try {
			flag = df.parse(day1).before(df.parse(day2));
		} catch (ParseException e) {
			throw e;
		}
		return flag;
	}

	public static String clearHtml(String str) {

		if (isNotEmpty(str)) {
			return str.replaceAll("<[.[^<]]*>", "").replaceAll("[\\n|\\r]", "").replaceAll("&nbsp;", "");
		} else {
			return str;
		}
	}

	public static String cutAndClearHtml(String str, int length) {
		String s = clearHtml(str);
		if (s.length() > length) {
			s = s.substring(0, length);
		}

		return s;
	}

	public static String formateHtml(String html) {

		if (isNotEmpty(html)) {
			html = html.replaceAll(" ", "&nbsp;");
			html = html.replaceAll("<", "&lt;");
			html = html.replaceAll("\n", "<br>");
			return html;
		}
		return html;
	}

	public static String reFormateHtml(String html) {

		if (isNotEmpty(html)) {
			html = html.replaceAll("&nbsp;", " ");
			html = html.replaceAll("&lt;", "<");
			html = html.replaceAll("<br>", "\n");
			return html;
		}
		return html;
	}

	// 获取字符长度
	public static int getRealLength(String str) {

		int len = 0;
		String[] arrayVal = str.split("");
		for (int i = 1; i < arrayVal.length; i++) {
			if (arrayVal[i].matches("[^\\x00-\\xff]")) // 全角
				len += 2;
			else
				len += 1;
		}
		return len;
	}

	/**
	 * 判断不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {

		if (null != str && !"".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {

		if (null == str || "".equals(str)) {
			return true;
		} else if ("".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String pathRelative2Absolutely(String con) {

		if (null != con) {
			// con = con.replace("../", Constant.WEBSTIE);
			return con;
		} else {
			return "";
		}

	}

	public static boolean isNumber(String str) {

		String checkPassWord = "^[0-9]+$";
		if (null == str) {
			return false;
		}
		if (!str.matches(checkPassWord)) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public static String getImages(String content) {

		StringBuffer sbf = new StringBuffer();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode allNode = htmlCleaner.clean(content);
		List<TagNode> nodeList = allNode.getElementListByName("img", true);
		String image = "";
		if (nodeList != null) {
			for (TagNode node : nodeList) {
				image = String.valueOf(node.getAttributeByName("src")).trim();
				if (!image.contains("emotion") && ArrayUtils.contains(static_ext, image.substring(image.lastIndexOf(".") + 1))) {
					sbf.append(image + "|");
				}
			}
		}
		if (sbf.length() > 0) {
			sbf.substring(sbf.lastIndexOf("|"));
		}
		return sbf.toString();
	}

	public static String getFirstImage(String content) {

		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode allNode = htmlCleaner.clean(content);
		TagNode node = allNode.findElementByName("img", true);
		if (node != null) {
			String image = String.valueOf(node.getAttributeByName("src")).trim();
			return image;
		}
		return null;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String delHTMLTag(String htmlStr) {
		htmlStr = htmlStr.replaceAll("&lt;", "<").replaceAll("&nbsp;", "");
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

	public static String delHTMLTag(String htmlStr, Integer size) {
		htmlStr = filterHtml(htmlStr);
		htmlStr = htmlStr.trim();
		htmlStr = htmlStr.replaceAll("\r|\n", "");
		int l = 0;
		if (size != null && size > 0) {
			l = htmlStr.length();
			if (l > size) {
				htmlStr = htmlStr.substring(0, size) + "...";
			}
		}
		return htmlStr;
	}

	public static String filterHtml(String str) {
		if (str == null) {
			return "";
		}
		str = str.replaceAll("&lt;", "<").replaceAll("&nbsp;", "");
		;
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String delStyleAndClass(String str) {
		// str = str.replaceAll("&lt;", "<");
		Pattern patternClass = Pattern.compile(" (class|style)=\".*?\"|(class|style)='.*?'");
		Matcher matcher = patternClass.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {

		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static boolean hasImg(String content) {
		if (StringUtils.isNotEmpty(content)) {
			Pattern p = Pattern.compile(regxpForImgTag, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher m = p.matcher(content);
			return m.find();
		}
		return false;
	}

	public static List<String> getImgSrc(String content) {
		List<String> srcs = null;
		if (StringUtils.isNotEmpty(content)) {
			srcs = new ArrayList<String>();
			Pattern p = Pattern.compile(regxpForImaTagSrcAttrib, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher m = p.matcher(content);
			while (m.find()) {
				srcs.add(m.group());
			}
		}
		return srcs;
	}

	public static List<String> getImgSrcUrl(String content) {
		List<String> srcs = null;
		if (StringUtils.isNotEmpty(content)) {
			srcs = new ArrayList<String>();
			Pattern p = Pattern.compile(regxpForImaTagSrcUrl, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher m = p.matcher(content);
			int groupCount = 0;
			while (m.find()) {
				groupCount = m.groupCount();
				if (groupCount > 0) {
					srcs.add(m.group(1));
				}
			}
		}
		return srcs;
	}

	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {

		if (isEmpty(sdate)) {
			return "";
		}
		// 获取时间分钟
		// String stime = sdate.substring(10, 16) + " ";
		String mtime = sdate.substring(10, 16) + " ";
		Date curDateTime = toDate(sdate);
		// 从需要格式化的时间的0点开始算
		sdate = sdate.substring(0, 10) + " 00:00:00";
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - curDateTime.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - curDateTime.getTime()) / 60000, 1) + "分钟以前";
			else
				ftime = hour + "个小时以前";
			return ftime;
		}
		double lt = (double) time.getTime() / 86400000;
		double ct = (double) cal.getTimeInMillis() / 86400000;
		double days = ct - lt;
		if (days <= 1) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟以前";
			else
				ftime = hour + "个小时以前";
		} else if (days > 1 && days <= 2) {
			ftime = "昨天 " /* + stime */;
		} else if (days > 2 && days <= 3) {
			ftime = "前天 " /* + stime */;
		} else if (days > 3 && days <= date_format_limit) {
			ftime = (int) days + "天以前 "/* + stime */;
		} else if (days > date_format_limit) {
			ftime = dateFormater2.get().format(time) + mtime;
		}
		return ftime;
	}

	public static String getRandStr(int n) {
		Random random = new Random();
		String sRand = "";
		for (int i = 0; i < n; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}

	public static String urlAddLink(String content) {
		StringBuilder html = new StringBuilder();
		int lastIdx = 0;
		Matcher matchr = URL_PATTERN.matcher(content);
		while (matchr.find()) {
			String str = matchr.group();
			html.append(content.substring(lastIdx, matchr.start()));
			if (str.startsWith("http://") || str.startsWith("https://") || str.startsWith("//")) {
				html.append("<a href='" + str + "'>" + str + "</a>");
			} else {
				html.append("<a href='//" + str + "'>" + str + "</a>");
			}
			lastIdx = matchr.end();
		}
		html.append(content.substring(lastIdx));
		return html.toString();
	}

	public static void main(String[] args) {
		// Pattern p = Pattern.compile("(http:|https:)?//[^[A-Za-z0-9\\._\\?%&+\\-=/#]]*",Pattern.CASE_INSENSITIVE );
		// String regexp = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" // 以http...或www开头
		// + ".*?" // 中间为任意内容，惰性匹配
		// + "(?=(&nbsp;|\\s|　|<br />|$|[<>]))"; // 结束条件
		// String regexp = "((https|http|ftp)?://)?"
		// + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
		// + "|"
		// + "([0-9a-z_!~*'()\\-]+\\.)*"
		// + "([0-9a-z][0-9a-z\\-]{0,61})?[0-9a-z]\\."
		// + "[a-z]{2,6})"
		// + "(:[0-9]{1,4})?"
		// + "(/[0-9a-z_!~*'()\\.;\\?:@&=+$,%#\\-]+)*/?";
		// Pattern p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		// Matcher m = p.matcher("中文ddgimg.qqgb.com/Program/Java/JavaFAQ/JavaJ2SE/Program_146959.html");
		// if (m.find()) {
		// System.out.println(m.group());
		// }
		// m = p.matcher("http://baike.baidu.com/view/230199.htm?fr=ala0_1中文");
		// if (m.find()) {
		// System.out.println(m.group());
		// }
		// m =
		// p.matcher("http://www.google.cn/gwt/x?u=http%3A%2F%2Fanotherbug.blog.chinajavaworld.com%2Fentry%2F4550%2F0%2F&btnGo=Go&source=wax&ie=UTF-8&oe=UTF-8");
		// if (m.find()) {
		// System.out.println(m.group());
		// }
		// m = p.matcher("http://zh.wikipedia.org:80/wiki/Special:Search?search=tielu&go=Go");
		// if (m.find()) {
		// System.out.println(m.group());
		// }
		// String content= "<img src=\"http://img.383cloud.com\"/>中文ddgimg.qqgb.com/Program/Java/JavaFAQ/JavaJ2SE/Program_146959.html";
		// content = HtmlUtils.htmlEscape(content);
		// System.out.println(content);
		// System.out.println(StringUtils.urlAddLink(content));
		// String regxp ="/upload/(.*)/([^/]+)_([\\d]+)x([\\d]+).(jpg|png|gif|jpeg)$";
		// Pattern p = Pattern.compile(regxp, Pattern.CASE_INSENSITIVE);
		// Matcher m = p.matcher("http://img.383cloud.com/upload/yunmar/recommend/20141124/images/670_340/1416794824453862692_60x60.png");
		//
		// if (m.find()) {
		// System.out.println(m.groupCount());
		// System.out.println(m.group(1));
		// System.out.println(m.group(2));
		// System.out.println(m.group(3));
		// System.out.println(m.group(4));
		// System.out.println(m.group(5));
		//
		// }

		System.out.println(StringUtils.urlAddLink("哈哈http://img.383cloud.com中文ddgimg.qqgb.com/Program/Java/JavaFAQ/JavaJ2SE/Program_146959.html"));
		System.exit(0);
	}

	public static String filterHtmlForAppContent(String str) {
		if (str == null) {
			return "";
		}
		str = StringUtils.delStyleAndClass(str);

		Pattern patternClass = Pattern.compile("</*font[^>]*>|</*h[^>]*>");
		Matcher matcher = patternClass.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		sb = new StringBuffer();
		result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		String rtn = sb.toString();
		// if(rtn != null && rtn.length() > 0){
		// rtn = "<span style=\"font-size:34px;font-family: 微软雅黑,Microsoft YaHei;\">" + rtn;
		// rtn += "</span>";
		// }

		return rtn;
	}

	public static String getUserBrowserName(HttpServletRequest request) {
		String userbrowser = "";
		String Agent = request.getHeader("User-Agent");
		if (Agent != null) {
			StringTokenizer st = new StringTokenizer(Agent, ";");
			if (st.hasMoreTokens()) {
				st.nextToken();
			}
			if (st.hasMoreTokens()) {
				// 得到用户的浏览器名
				userbrowser = st.nextToken();
			}
		}
		return userbrowser;
	}
}
