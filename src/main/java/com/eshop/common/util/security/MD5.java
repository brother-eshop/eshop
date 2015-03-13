package com.eshop.common.util.security;

import java.security.MessageDigest;
/**
 * 
 * @ClassName  com.yunxue.framework.core.util.MD5
 * @description MD5加密
 * @author : spencer
 */
public class MD5 {
	public final static String getMD5(String s){
		
		char hexDigits[] = {'d','e', 'f','A', 'b', 'c', '0', '1', '2', '3', '-', '5', '6', '7', '8', '9'};
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>>4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
		catch (Exception e){
			return null;
		}
	}
}