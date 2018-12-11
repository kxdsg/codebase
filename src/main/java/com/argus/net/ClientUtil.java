package com.argus.net;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xingding on 18/12/11.
 */
public class ClientUtil {

    /**
     * 获取客户端真实IP, 用于应用服务器在nginx/apache等反向代理后. <br>
     * 在反向代理配置X-Forward-For, X-Real-IP或Proxy-Client-IP参数,
     * 通过request.getHeader获取真实IP.
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        // 首先判断X-Forwarded-For
        String ip = request.getHeader("X-Forwarded-For");
        if (isRealAddress(ip)) {
            // X-Forwarded-For中包含多个地址时, 取第一个地址
            return ip.contains(",") ? ip.substring(0, ip.indexOf(",")).trim() : ip;
        }
        // 其次判断X-Real-IP
        ip = request.getHeader("X-Real-IP");
        if (isRealAddress(ip)) {
            return ip;
        }
        // 然后判断Proxy-Client-IP
        ip = request.getHeader("Proxy-Client-IP");
        if (isRealAddress(ip)) {
            return ip;
        }
        //然后判断WL-Proxy-Client-IP
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isRealAddress(ip)) {
            return ip;
        }
        // 最后判断未经过代理的情况
        ip = request.getRemoteAddr();
        if (isRealAddress(ip)) {
            return ip;
        }
        ip = request.getHeader("http_client_ip");
        if (isRealAddress(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isRealAddress(ip)) {
            return ip;
        }
        return null;
    }

    private static boolean isRealAddress(String addr) {
        return StringUtils.isNotEmpty(addr) && !"unknown".equalsIgnoreCase(addr);
    }
}
