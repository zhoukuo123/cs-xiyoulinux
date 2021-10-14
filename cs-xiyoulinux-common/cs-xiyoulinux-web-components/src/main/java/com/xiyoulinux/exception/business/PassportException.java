package com.xiyoulinux.exception.business;

/**
 * @author CoderZk
 */
public class PassportException extends Exception {

    private Integer code;

    /**
     * 认证异常
     * @param code 错误码
     * @param message 错误信息
     */
    public PassportException(Integer code ,String message) {
        super(message);
        this.code = code;
    }

    /**
     * 返回错误码
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }

}
