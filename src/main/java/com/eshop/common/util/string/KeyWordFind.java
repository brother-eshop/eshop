package com.eshop.common.util.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关键词过滤
 * @author Zhang Xiao Dong
 */
public class KeyWordFind {
	public static boolean isInit = false;
	private static int key_max = 0; // 敏感词最大长度
	private static String[] keys;
	private static ArrayList<String> first = new ArrayList<String>();
	private static String[] sortFirst;
	private static char[] charFirst;
	private static HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, String[]> sortMap = new HashMap<String, String[]>();
	private static HashMap<String, char[]> charMap = new HashMap<String, char[]>();

	// 初始化黑词库 path为黑词文件位置
	public static void init(String path) throws IOException {
		keys = read(path);
		initData();
		isInit = true;
	}

	public static boolean reload(List<String> list) {
		try {
			keys = null;
			first = new ArrayList<String>();
			sortFirst = null;
			charFirst = null;
			map = new HashMap<String, ArrayList<String>>();
			sortMap = new HashMap<String, String[]>();
			charMap = new HashMap<String, char[]>();
			init(list);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void init(List<String> list) throws IOException {
		keys = new String[list.size()];
		list.toArray(keys);
		initData();
		isInit = true;
	}

	public static void initData() {
		ArrayList<String> temp;
		String key, value;
		int length;
		for (String k : keys) {
			if (!first.contains(k.substring(0, 1))) {
				first.add(k.substring(0, 1));
			}
			length = k.length();
			if (length > key_max)
				key_max = length;
			for (int i = 1; i < length; i++) {
				key = k.substring(0, i);
				value = k.substring(i, i + 1);
				if (i == 1 && !first.contains(key)) {
					first.add(key);
				}

				// 有，添加
				if (map.containsKey(key)) {
					if (!map.get(key).contains(value)) {
						map.get(key).add(value);
					}
				}
				// 没有添加
				else {
					temp = new ArrayList<String>();
					temp.add(value);
					map.put(key, temp);
				}
			}
		}
		sortFirst = first.toArray(new String[first.size()]);
		Arrays.sort(sortFirst); // 排序

		charFirst = new char[first.size()];
		for (int i = 0; i < charFirst.length; i++) {
			charFirst[i] = first.get(i).charAt(0);
		}
		Arrays.sort(charFirst); // 排序

		String[] sortValue;
		ArrayList<String> v;
		Map.Entry<String, ArrayList<String>> entry;
		Iterator<Entry<String, ArrayList<String>>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			sortValue = v.toArray(new String[v.size()]);
			Arrays.sort(sortValue); // 排序
			sortMap.put(entry.getKey(), sortValue);
		}

		char[] charValue;
		iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			charValue = new char[v.size()];
			for (int i = 0; i < charValue.length; i++) {
				charValue[i] = v.get(i).charAt(0);
			}
			Arrays.sort(charValue); // 排序
			charMap.put(entry.getKey(), charValue);
		}
	}

	public static String find(String content) {
		String r = null;
		String c = delBlank(content);
		char g;
		char[] temps;
		char[] keys = new char[key_max];
		int length = c.length(), index;
		tag: for (int i = 0; i < length - 1; i++) {
			index = 0;
			g = c.charAt(i);
			// 过滤特殊字符
			if (Arrays.binarySearch(filters, g) > -1) {
				continue;
			}
			// 二分查找
			if (Arrays.binarySearch(charFirst, g) > -1) {
				keys[index++] = g;
				for (int j = i + 1; j < length; j++) {
					g = c.charAt(j);
					// 过滤特殊字符
					if (Arrays.binarySearch(filters, g) > -1) {
						continue;
					}
					temps = charMap.get(String.valueOf(keys, 0, index));
					if (temps == null) { // 找到了
						r = String.valueOf(keys, 0, index);
						break tag;
					}
					// 二分查找
					if (Arrays.binarySearch(temps, g) > -1) {
						if (j == length - 1) {
							keys[index++] = g;
							temps = charMap.get(String.valueOf(keys, 0, index));
							if (temps == null) {
								r = String.valueOf(keys, 0, index);
							} else {
								r = null;
							}
							break tag;
						}
					} else { // 没有找到了
						break;
					}
					keys[index++] = g;
				}
			}
		}
		return r;
	}

	public static String[] read(String path) throws IOException {
		FileReader fr1 = new FileReader(new File(path));
		BufferedReader br1 = new BufferedReader(fr1);
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br1.readLine()) != null) {
			sb.append(line);
		}
		br1.close();
		String[] keywords = sb.toString().split("\\|");
		return keywords;
	}

	// 过滤特殊字符[敏感词需要过滤、用户输入内容也需要过滤]
	static char[] filters = "`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\".toCharArray();
	static {
		Arrays.sort(filters); /* 排序 */
	}

	// 关键字替换
	public static String replaceKeyWord(String content) {
		String newContent = content;
		String k;
		while ((k = find(newContent)) != null) {
			Matcher m = getRegexp(k).matcher(newContent);
			newContent = m.replaceAll("**");
		}
		return newContent;
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.toString());
	}

	public static String getKeyWord(String content) {
		String newContent = content;
		String k;
		StringBuilder sb = new StringBuilder();
		while ((k = find(newContent)) != null) {
			Matcher m = getRegexp(k).matcher(newContent);
			newContent = m.replaceAll("**");
			if (sb.length() > 0) {
				sb.append("，");
			}
			sb.append(k);
			if (sb.length() > 20) {
				sb.append("……");
				break;
			}
		}
		return sb.toString();
	}

	private static Pattern getRegexp(String string) {
		char[] ch = string.toCharArray();
		String reg = "[\\s|`|~|!|@|#|$|%|^|&|*|(|)|\\+|=|\\||{|}|'|:|;|'|,|\\[|\\]|.|<|>|/|\\?|~|！|@|#|￥|%|…|…|&|*|（|）|—|\\-|||{|}|【|】|‘|；|：|”|“|’|。|，|、|？|\\\\]*";
		StringBuilder sb = new StringBuilder();
		sb.append(ch[0]);
		int length = ch.length;
		for (int i = 1; i < length; i++) {
			sb.append(reg).append(ch[i]);
		}
		Pattern p = Pattern.compile(sb.toString());
		return p;
	}

	private static String delBlank(String string) {
		if (StringUtils.isNotEmpty(string)) {
			Pattern p = Pattern.compile("(\\s)*");
			Matcher m = p.matcher(string);
			String strNoBlank = m.replaceAll("");
			return strNoBlank;
		} else {
			return "";
		}
	}

	// public static void main(String[] args) {
	// try {
	// KeyWordFind.init("E:\\workspace\\jinkao_edu\\jinkao_web_problem\\src\\main\\resources\\keyword.txt");
	// String temp = KeyWordFind.replaceKeyWord("一二三四五六七八九十三运动");
	// System.out.println(temp);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

}
