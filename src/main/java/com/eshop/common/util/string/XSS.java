package com.eshop.common.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS脚本攻击
 * <p>
 * @author hubin
 * @Date 2014-5-8
 */
public class XSS {
	private static final Pattern scriptPattern = Pattern.compile("(<|%(25)?3C)script(>|%(25)?3E)(.*?)(<|%(25)?3C)(/|%2F)script(>|%(25)?3E)",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern src1 = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern src2 = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern scriptEnd = Pattern.compile("(<|%(25)?3C)(/|%2F)script(>|%(25)?3E)", Pattern.CASE_INSENSITIVE);
	private static final Pattern scriptStart = Pattern.compile("(<|%(25)?3C)script(.*?)(>|%(25)?3E)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
			| Pattern.DOTALL);
	private static final Pattern eval = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern expression = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern javascript = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	private static final Pattern vbscript = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	private static final Pattern onload = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern script = Pattern.compile("(<|%(25)?3C)(/|%2F)?script(.*?)(>|%(25)?3E)", Pattern.CASE_INSENSITIVE);

	/**
	 * @Description XSS脚本内容剥离
	 * @param value
	 *            待处理内容
	 * @return
	 */
	public static String strip(String value) {
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);
			// Avoid null characters
			// value = value.replaceAll("", "");
			// Avoid anything between script tags

			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid anything in a src='...' type of expression

			value = src1.matcher(value).replaceAll("");

			value = src2.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag

			value = scriptStart.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag

			value = scriptEnd.matcher(value).replaceAll("");

			// Avoid eval(...) expressions

			value = eval.matcher(value).replaceAll("");

			// Avoid expression(...) expressions

			value = expression.matcher(value).replaceAll("");

			// Avoid javascript:... expressions

			value = javascript.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions

			value = vbscript.matcher(value).replaceAll("");

			// Avoid onload= expressions

			value = onload.matcher(value).replaceAll("");
		}
		return value;
	}

	public static boolean hasXss(String value) {
		if (value != null) {
			Matcher m = script.matcher(value);
			if (m.find()) {
				return true;
			}
			m = eval.matcher(value);
			if (m.find()) {
				return true;
			}
			m = expression.matcher(value);
			if (m.find()) {
				return true;
			}
			m = javascript.matcher(value);
			if (m.find()) {
				return true;
			}
			m = vbscript.matcher(value);
			if (m.find()) {
				return true;
			}
			m = onload.matcher(value);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		String s = "http%3A%2F%2Fback.yunmar.com.cn%2Ftopic%2F1687%3Ft%3D%253Cscript%253E%253C%2Fscript%253E";
		Matcher m = scriptPattern.matcher(s);
		while (m.find()) {
			System.out.println(m.group());
		}
	}
}
