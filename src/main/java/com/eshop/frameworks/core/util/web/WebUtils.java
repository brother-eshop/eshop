package com.eshop.frameworks.core.util.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @ClassName com.yunxue.framework.core.util.WebUtils
 * @description cookie和web一些工具类
 * @author : spencer
 * @Create Date : 2013-12-13 下午2:26:49
 */
public class WebUtils {

    // 存储主站域名
    private static String MYDOMAIN = "";// .268xue.com

    /**
     * 增加或修改cookie
     *
     * @param response
     * @param key
     * @param value
     * @param days
     */
    public static void setCookie(HttpServletResponse response, String key, String value,
            int days) {

        if (key != null && value != null) {
            Cookie cookie = new Cookie(key, value);
            // 设置有效日期
            cookie.setMaxAge(days * 24 * 60 * 60);
            // 设置路径（默认）
            cookie.setPath("/");
            // cookie.setDomain(MYDOMAIN);
            // 把cookie放入响应中
            response.addCookie(cookie);
        }

    }

    /**
     * 增加或修改cookie
     *
     * @param response
     * @param key
     * @param value
     * @param days
     */
    public static void setCookie(HttpServletResponse response, String key, String value,
            int days, String domain) {

        if (key != null && value != null) {
            Cookie cookie = new Cookie(key, value);
            // 设置有效日期
            cookie.setMaxAge(days * 24 * 60 * 60);
            // 设置路径（默认）
            cookie.setPath("/");
            if (domain != null) {
                cookie.setDomain(MYDOMAIN);
            }
            // 把cookie放入响应中
            response.addCookie(cookie);
        }

    }

    /**
     * 得到指定键的值
     *
     * @param request
     * @param name
     *            指定的键
     * @return String 值
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        String resValue = "";
        if (cookies != null) {
            if (cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    if (key.equalsIgnoreCase(cookies[i].getName())) {
                        resValue = cookies[i].getValue();
                    }
                }
            }
        }
        return resValue;
    }

    /**
     * 根据name销毁cookie
     *
     * @param request
     * @param response
     */
    public static void deleteCookie(HttpServletRequest request,
            HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            if (cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    if (name.equalsIgnoreCase(cookies[i].getName())) {
                        Cookie cookie = cookies[i];
                        // 销毁
                        Cookie ck = new Cookie(cookie.getName(), null);
                        ck.setPath("/");
                        ck.setMaxAge(0);
                        response.addCookie(ck);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 根据域名和name销毁cookie
     *
     * @param request
     * @param response
     * @param
     */
    public static void deleteCookieDomain(HttpServletRequest request,
            HttpServletResponse response, String name, String domain) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            if (cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    if (name.equalsIgnoreCase(cookies[i].getName())) {
                        Cookie cookie = cookies[i];
                        // 销毁
                        Cookie ck = new Cookie(cookie.getName(), null);
                        ck.setPath("/");
                        ck.setDomain(domain);
                        ck.setMaxAge(0);
                        response.addCookie(ck);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 创建cookie
     *
     * @param response
     *            回应
     * @param nameValues
     *            Hashtable<String, String> 存入cookie的键值对
     * @param days
     *            设置cookie的有效期
     */
    public static void createCookieFromMap(HttpServletResponse response,
            Hashtable<String, String> nameValues, int days) {
        Set<String> set = nameValues.keySet();
        Iterator<String> it = set.iterator();
        for (; it.hasNext();) {
            String name = (String) it.next();
            String value = (String) nameValues.get(name);
            // 生成新的cookie
            Cookie cookie = new Cookie(name, value);
            // 设置有效日期
            cookie.setMaxAge(days * 24 * 60 * 60);
            // 设置路径（默认）
            cookie.setPath("/");
            // 把cookie放入响应中
            response.addCookie(cookie);
        }
    }

    /**
     * 创建cookie
     *
     * @param response
     *            回应
     * @param nameValues
     *            存入cookie的键值对
     * @param days
     *            设置cookie的有效期
     * @param domain
     *            设置的域名
     */
    public static void createCookieFromMapDomain(HttpServletResponse response,
            Hashtable<String, String> nameValues, int days, String domain) {
        Set<String> set = nameValues.keySet();
        Iterator<String> it = set.iterator();
        for (; it.hasNext();) {
            String name = (String) it.next();
            String value = (String) nameValues.get(name);
            // 生成新的cookie
            Cookie cookie = new Cookie(name, value);
            cookie.setDomain(domain);
            cookie.setSecure(false);
            // 设置有效日期
            cookie.setMaxAge(days * 24 * 60 * 60);
            // 设置路径（默认）
            cookie.setPath("/");
            // 把cookie放入响应中
            response.addCookie(cookie);
        }
    }

    /**
     * 读取Cookie
     *
     * @param request
     * @return Hashtable 返回cookie的键值对
     */
    public static Hashtable<String, String> getCookiesForMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Hashtable<String, String> cookieHt = new Hashtable<String, String>();
        if (cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                cookieHt.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieHt;
    }

    /**
     * 修改cookie中指定键的值
     *
     * @param request
     * @param name
     *            指定的键
     * @param value
     *            值
     */
    public static void updateCookie(HttpServletRequest request, String name, String value) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equalsIgnoreCase(cookies[i].getName())) {
                    cookies[i].setValue(value);
                    return;
                }
            }
        }
    }

    /**
     * 销毁所有cookie
     *
     * @param request
     * @param response
     */
    public static void deleteAllCookie(HttpServletRequest request,
            HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                // 销毁
                Cookie ck = new Cookie(cookie.getName(), null);
                ck.setPath("/");
                ck.setMaxAge(0);
                response.addCookie(ck);
            }
        }
    }

    // 获得IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                                                            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获得用户浏览器ua
     *
     * @param request
     * @return String 浏览器类型
     */
    public static String getUserAgent(HttpServletRequest request) {
        String uabrow = request.getHeader("User-Agent");

        uabrow = uabrow.toLowerCase();
        String result = "";
        if (uabrow.indexOf("firefox") > 0) {
            result = "firefox";
        }
        if (uabrow.indexOf("opera") > 0) {
            result = "opera";
        }
        if (uabrow.indexOf("msie") > 0) {
            // msie 10.0
            result = uabrow.split(";")[1].trim();
        }
        if (uabrow.indexOf("chrome") > 0) {
            result = "chrome";
        }
        if (uabrow.indexOf("android") > 0) {
            result = "android";
        }
        if (uabrow.indexOf("mac os") > 0) {
            result = "mac";
        }
        if (uabrow.indexOf("ios") > 0) {
            result = "ios";
        }
        return result.toLowerCase();
    }

    // encodeURL
    public static String encodeURL(String url, String encode)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        StringBuilder noAsciiPart = new StringBuilder();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c > 255) {
                noAsciiPart.append(c);
            } else {
                if (noAsciiPart.length() != 0) {
                    sb.append(URLEncoder.encode(noAsciiPart.toString(), encode));
                    noAsciiPart.delete(0, noAsciiPart.length());
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // IPUTIL********
    public static String visitWeb(String urlStr) {
        URL url = null;
        HttpURLConnection httpConn = null;
        InputStream in = null;
        try {
            url = new URL(urlStr);
            httpConn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0(compatible;MSIE 6.0;Windows 2000)");
            in = httpConn.getInputStream();
            return convertStreamToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                httpConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {

            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String getAddressByIP(String ip) {
        String js = visitWeb("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="
                + ip);
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = jsonParser.parse(js.substring(21)).getAsJsonObject();
        String province = "";
        String city = "";
        try {
            province = jo.get("province") == null ? "" : URLDecoder.decode(
                    jo.get("province").toString(), "UTF-8");
            city = jo.get("city") == null ? "" : URLDecoder.decode(jo.get("city")
                    .toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (province.equals("") || province.equals(city)) ? city : province + " "
                + city;
    }

    // IPUTIL********

    /**
     * 获取URL及参数
     *
     * @return 类似于a.action?name=tx&age=20
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    public String getServletRequestUrlParms(HttpServletRequest request) {
        // 获得的地址参数，如果没有为空 ，有时是以&结束的
        StringBuffer sbUrlParms = request.getRequestURL();
        sbUrlParms.append("?");
        Enumeration parNames = request.getParameterNames();
        while (parNames.hasMoreElements()) {
            String parName = parNames.nextElement().toString();
            try {
                sbUrlParms
                        .append(parName)
                        .append("=")
                        .append(URLEncoder.encode(request.getParameter(parName), "UTF-8"))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
        return sbUrlParms.toString();
    }


}
