package com.xiyoulinux.vo;
import lombok.Data;
import java.io.Serializable;

/**
 * 实现统一响应
 * @author qkm
 */
@Data
public class GlobalResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = -4169227164065647462L;
    private Integer code = 200;
    private String message = "request successfully";
    private T data;

    public GlobalResponseEntity() {
        super();
    }

    public GlobalResponseEntity(T data) {
        this.data = data;
    }

    public GlobalResponseEntity(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public GlobalResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GlobalResponseEntity<?> badRequest() {
        return new GlobalResponseEntity<>( 404, "页面走丢了～～");
    }

}