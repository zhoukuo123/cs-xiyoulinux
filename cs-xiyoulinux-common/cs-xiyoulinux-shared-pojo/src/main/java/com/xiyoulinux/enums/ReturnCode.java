package com.xiyoulinux.enums;

/**
 * @author CoderZk
 *
 * 返回状态码 枚举
 */
public enum ReturnCode {

    /**
     * 业务异常，这里约定每个业务场景的错误都是有一个错误码的。
     * 这个错误码需要展示给前端。前端需要根据这个码值做一些特殊的判断。如余额不足，错误码 001，那么前端根据这个码值引导用户去充值页面。
     * 返回状态码的规定:
     *
     *  200：表示成功
     *  500：表示错误，错误信息在msg字段中
     *  501：bean验证错误，不管多少个错误都以map形式返回
     *  401：认证错误
     *  555: 系统异常抛出信息
     */

    SUCCESS(200, "成功"),
    ERROR(500, "错误"),
    INVALID_PARAM(501, "bean验证错误, BO参数有误"),
    SYSTEM_EXCEPTION(555, "系统异常"),
    UNAUTHORIZED(401, "认证错误");

    public final Integer code;
    public final String value;

    ReturnCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
