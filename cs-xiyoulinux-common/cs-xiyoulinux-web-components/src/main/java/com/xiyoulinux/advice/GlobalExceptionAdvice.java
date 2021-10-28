package com.xiyoulinux.advice;

import com.xiyoulinux.common.GlobalResponseEntity;
import com.xiyoulinux.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获处理,捕获controller层的异常
 *
 * @author qkm
 */
@Slf4j
@RestControllerAdvice(value = "com.xiyoulinux")
public class GlobalExceptionAdvice {


    /**
     * 类型转变异常（枚举）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> parseException(HttpMessageNotReadableException e) {
        log.info("parse exception [{}]", e.getMessage());
        return new ResponseEntity<>(
                new GlobalResponseEntity<>(500, "字段转变异常--字段不符合要求哦"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    /**
//     * 处理404异常
//     */
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
//        return new ResponseEntity<>(
//                GlobalResponseEntity.badRequest(),
//                HttpStatus.NOT_FOUND);
//    }

    /**
     * 捕获运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException:", e);
        return new ResponseEntity<>(
                new GlobalResponseEntity<>(500, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 没有权限异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public GlobalResponseEntity<Object> unAuthException(UnauthorizedException e) {
        return new GlobalResponseEntity<>(401, e.getMessage());
    }

}

