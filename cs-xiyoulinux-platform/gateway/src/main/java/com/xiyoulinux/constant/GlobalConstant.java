package com.xiyoulinux.constant;


/**
 * @author qkm
 */
public class GlobalConstant {
    /**
     * 登录 uri
     */
    public static final String USER_LOGIN_PATH = "/cs-auth/authority/token";

    /**
     * 注册 uri
     */
    public static final String USER_REGISTER_PATH = "/cs-auth/authority/register";

    /**
     * 去授权中心拿到登录 token 的 uri 格式化接口
     */
    public static final String AUTHORITY_CENTER_TOKEN_URL_FORMAT =
            "http://%s:%s/cs-auth/authority/token";

    /**
     * 去授权中心注册并拿到 token 的 uri 格式化接口
     */
    public static final String AUTHORITY_CENTER_REGISTER_URL_FORMAT =
            "http://%s:%s/cs-auth/authority/register";
}
