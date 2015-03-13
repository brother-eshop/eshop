package com.eshop.frameworks.core.util.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @Project: servic.ishowchina.com
 * @author Zhang Xiao Dong
 * @Date: 2011-10-10
 * 
 *        此类主要用于把xml数据转换为json字符串
 */
public class XMLToJSON {

	private Document	dom;
	DocumentBuilder		db;

	// xml字符串输入构造器
	public XMLToJSON(String xml) throws ParserConfigurationException, SAXException, IOException {
		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		dom = db.parse(new InputSource(new StringReader(xml)));
	}

	public XMLToJSON(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		dom = db.parse(new InputSource(stream));
	}

	// xml文件输入构造器
	public XMLToJSON(File f) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		dom = db.parse(new FileInputStream(f));
	}

	/**
	 * 
	 * @Project: servic.ishowchina.com
	 * @Method NodeToJsonObj
	 * @returnType String
	 * @author Zhang Xiao Dong
	 * @Date: 2011-10-10
	 * 
	 *        根据xml节点转换为json字符串
	 * @param node
	 * @return json 字符串
	 */
	private String NodeToJsonObj(Node node) {
		// 用于储存节点属性的Map
		TreeMap<String, String> rstMap = new TreeMap<String, String>();
		NamedNodeMap attrMap = node.getAttributes();
		// if(node.getNodeName().equals("#cdata-section")){
		// return null;
		// }
		for (int i = 0; attrMap != null && i < attrMap.getLength(); i++) {
			Node n = attrMap.item(i);
			// 节点属性存入Map中备用
			rstMap.put(SafeJSON(n.getNodeName()), "\"" + SafeJSON(n.getNodeValue()) + "\"");
		}
		// 取节点的所有子节点
		NodeList nl = node.getChildNodes();
		if (nl.getLength() == 1 && node.getFirstChild().getNodeType() == 4) {
			if (rstMap.isEmpty()) {
				return SafeJSON(node.getFirstChild().getNodeValue().trim()).equals("") ? "\"\"" : "\"" + SafeJSON(node.getFirstChild().getNodeValue().trim()) + "\"";
			}
		}
		// 判断子结点个数，和是否为文本节点
		if (nl.getLength() == 0 || (nl.getLength() == 1 && node.getFirstChild().getNodeType() == Node.TEXT_NODE)) {
			String value = "\"\"";
			if (node.getFirstChild() != null && !(node.getFirstChild().getNodeValue().trim().equals(""))) {
				value = "\"" + SafeJSON(node.getFirstChild().getNodeValue().trim()) + "\"";
			}
			// 如果节点属性为空并且没有子节点或只有一个文本节点则返回文本节点
			if (rstMap.isEmpty()) {
				return nl.getLength() == 0 ? "\"\"" : value;
			}
			else {
				// 如果属性不为空，则把文本节点作为值存入属性Map
				rstMap.put("value", value);
			}
		}
		// 如果存在子节点，并且子节点不全为文本节点，则建立一个set，并set中存入一个文本类型节点名
		HashSet<String> nSet = new HashSet<String>();
		nSet.add("#text");
		nSet.add("#comment");
		for (int i = 0; i < nl.getLength(); i++) {
			String nName = nl.item(i).getNodeName();
			// 遍历子节点，把nodename不相同的节点名存入set中，相同的则跳出本次循环
			if (nSet.contains(nName)) {
				continue;
			}
			if (nName.equals("#cdata-section")) {
				rstMap.put("value", "\"" + SafeJSON(nl.item(i).getNodeValue().trim()) + "\"");
				continue;
			}
			nSet.add(nName);
			StringBuilder nValue = new StringBuilder();
			int k = 0;
			// 在所有同级节点中查找相同nodename的Element类型节点
			for (int j = 0; j < nl.getLength(); j++) {

				Node tn = nl.item(j);
				if ((!(tn instanceof Element)) || tn.getNodeType() == Node.TEXT_NODE) continue;
				// 如果存在相同名称的节点，则以此节点为父节点递归调用本方法（至少有一次相同）
				if (tn.getNodeName().equals(nName)) {
					nValue.append(NodeToJsonObj(tn));
					nValue.append(",");
					k++;
				}
			}
			// 如果存在相同的则删除最后的逗号
			if (k > 0) {
				nValue.deleteCharAt(nValue.length() - 1);
			}
			// 如果相同的大于一个，则在开始和结束插入[]编程数组形式
			if (k > 1) {
				nValue.insert(0, "[");
				nValue.append("]");
			}
			// 把其子节点的属性值作为父节点的属性
			rstMap.put(nName, nValue.toString());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		while (!rstMap.isEmpty()) {
			String key = rstMap.lastKey();
			sb.append("\"");
			sb.append(key);
			sb.append("\":");
			if (rstMap.get(key) != null) {
				sb.append((rstMap.get(key).toString()));
			}
			else {
				sb.append("\"\"");
			}
			sb.append(",");
			rstMap.pollLastEntry();
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();

	}

	/**
	 * 
	 * @Project: servic.ishowchina.com
	 * @Method SafeJSON
	 * @returnType String
	 * @author Zhang Xiao Dong
	 * @Date: 2011-10-12 把json里的控制字符与特殊符号转为安全字符
	 * @param sIn
	 * @return String
	 */
	public static String SafeJSON(String sIn) {
		StringBuilder sbOut = new StringBuilder(sIn.length());
		char[] ch = sIn.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			// if (Character.isISOControl(ch[i]) || ch[i] == '\'')
			// {
			// String str = "000" + Integer.toHexString(ch[i]);
			// sbOut.append("\\\\u"
			// + str.substring(str.length() - 4, str.length()));
			// continue;
			// }
			// else
			if (ch[i] == '\"' || ch[i] == '\\')// remove || ch[i] == '/'
			{
				sbOut.append('\\');
			}
			sbOut.append(ch[i]);
		}
		return sbOut.toString();
	}

	/**
	 * 
	 * @returnType String
	 * @author Zhang Xiao Dong
	 * @Date: 2011-10-10
	 * 
	 *        转换方法
	 * @return json字符串
	 */
	public String run() {
		Node n = dom.getFirstChild();
		return "{\"" + n.getNodeName() + "\":" + NodeToJsonObj(n) + "}";
	}

	/**
	 * 
	 * @Project: servic.ishowchina.com
	 * @Method MapToJSON
	 * @returnType String
	 * @author Zhang Xiao Dong
	 * @Date: 2011-10-17 用于把map<String,<String>格式转化为json字符串返回
	 * @param map
	 * @return json字符串
	 */
	@SuppressWarnings("unchecked")
	public static String MapToJSON(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		boolean startMark = true;
		sb.append("{\"result\":[");
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			if (!startMark) {
				sb.append(",");
			}
			else {
				startMark = false;
			}
			sb.append("{\"id\":\"").append(entry.getKey()).append("\",").append("\"value\":\"").append(entry.getValue()).append("\"}");
		}
		sb.append("]}");
		return sb.toString();
	}

	public static void main(String[] args) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
		XMLToJSON xtj = new XMLToJSON(new File("e:\\test.xml"));
		System.out.println(xtj.run());
	}
}
