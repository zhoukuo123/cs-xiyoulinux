package com.xiyoulinux.error;

/**
 * 抛出业务异常
 * @author qkm
 */
public class BusinessServiceException extends RuntimeException {

    private static final long serialVersionUID = -1152885741982115257L;

    public BusinessServiceException(String message) {
        super(message);
    }

}
