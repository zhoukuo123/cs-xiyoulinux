package com.xiyoulinux.advice;

import com.xiyoulinux.annotation.IgnoreResponseAdvice;
import com.xiyoulinux.vo.GlobalResponseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 捕捉响应实现统一封装
 *
 * @author qkm
 */
@RestControllerAdvice
public class GlobalResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
//        if (methodParameter.getDeclaringClass()
//                .isAnnotationPresent(IgnoreResponseAdvice.class)) {
//            return false;
//        }
//
//        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
//            return false;
//        }
//
//        return true;
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 定义最终的返回对象,默认是正确的
        GlobalResponseEntity<Object> response = new GlobalResponseEntity<>();

        if (o instanceof GlobalResponseEntity) {
            response = (GlobalResponseEntity<Object>) o;
        } else {
            response.setData(o);
        }
        return response;
    }
}
