package com.eshop.common.util.security;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 
 * 安全工具类
 * 
 * @ClassName net.sns.framework.core.util.Security.PurseSecurityUtils
 * @description
 * @author : spencer
 * @Create Date : 2014-1-11 上午9:29:45
 */
public class PurseSecurityUtils {

    private static CryptUtil cryptUtil = new CryptUtilImpl();

    public static CryptUtil getCrypt() {
        return cryptUtil;
    }

    // 24位加密
    private final static int count = 24;

    /**
     * 数据签名
     * 
     * @param key
     *            加密key
     * @param value
     *            数据
     * @return
     */
    public static String hmacSign(String key, String value) {

        return Digest.hmacSign(value, key);
    }

    /**
     * 将报文数据加密
     * 
     * @param value
     *            报文加密后的字符串
     * @return
     */
    public static String secrect(String value, String key) {
        return cryptUtil.cryptDes(value, getKey(key));
    }

    public static String getKey(String key) {
        return key.substring(0, count);
    }

    /**
     * 将报文数据解密
     * 
     * @param value
     *            解密后原文
     * @return
     */
    public static String decryption(String value, String key) {
        return cryptUtil.decryptDes(value, getKey(key));
    }

    /**
     * 验签是否通过
     * 
     * @param key
     *            加密key
     * @param value
     *            报文数据
     * @param secretValue
     *            加密后的报文数据
     * @return false 未通过 true 通过
     */
    public static boolean isPassHmac(String secretValue, String key, String value) {
        String svalue = Digest.hmacSign(value, key);
        boolean flag = false;
        if (secretValue.equals(svalue)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据Map里面设定的值 转化成签名
     * 
     * @param map
     *            相应返回的数据报文数据
     * @param customerKey
     *            customer自己的 hamcKey
     */
    public static void generateHmac(Map<String, String> map, String customerKey) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = map.keySet();
        for (String string : set) {
            String value = map.get(string);
            sb.append(value);
        }
        String hmacSign = hmacSign(customerKey, sb.toString());
        map.put("hmac", hmacSign);
    }

    /**
     * 产生不重复号(如：订单号、批次号)
     * 
     * @param prefix
     *            前缀
     * @return 生成号
     */
    public static synchronized String generateOrderNumber(String prefix) {
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        String uu = uuid.toString().split("-")[0];
        sb.append(prefix);
        sb.append(time);
        sb = new StringBuffer(sb.substring(0, sb.toString().length() - 2));
        sb.append(uu);
        return sb.toString();
    }

    public static boolean isJointMobileNumber(String mobileNumber) {
        String pattern = "^(1([0-9]{10}))$";
        return mobileNumber.matches(pattern);
    }

    /**
     * 判断手机号
     */
    public static boolean isJointUserLoginName(String mobileNumber) {
        // 判断该用户是否是 手机用户
        return isJointMobileNumber(mobileNumber);
    }

    /**
     * 验证邮箱
     * 
     * @param value
     * @param length
     *            邮箱长度 默认不超过40
     * @return
     */
    public static boolean checkEmail(String value, int length) {
        if (length == 0) {
            length = 40;
        }
        return value.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
                && value.length() <= length;
    }

    public static String getRandomNumber(int i) {
        StringBuilder sb = new StringBuilder(System.currentTimeMillis() + "");
        String str = sb.reverse().substring(0, i).toString();
        return str;
    }

    /**
     * 验证字符是否为6-16为字符、数字或下划线组成
     * 
     * @param password
     * @return
     */
    public static boolean isPasswordAvailable(String password) {
        String partten = "^[_0-9a-zA-Z]{3,}$";
        boolean flag = password.matches(partten) && password.length() >= 6
                && password.length() <= 16;
        return flag;
    }
}
