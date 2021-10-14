package com.xiyoulinux.advice;

import com.xiyoulinux.error.BusinessServiceException;
import com.xiyoulinux.vo.GlobalResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常捕获处理,捕获controller层的异常
 *
 * @author qkm
 */
@Slf4j
@RestControllerAdvice(value = "com.xiyoulinux")
public class GlobalExceptionAdvice {


    /**
     * 捕获一般异常
     * 捕获未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(
                new GlobalResponseEntity<>(500,
                        e.getMessage() == null ? "未知异常" : e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return new ResponseEntity<>(
                GlobalResponseEntity.badRequest(),
                HttpStatus.NOT_FOUND);
    }

    /**
     * 捕获运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException:", e);
        return new ResponseEntity<>(
                new GlobalResponseEntity<>(500,
                        e.getMessage() == null ? "运行时异常" : e.getMessage().replace("java.lang.RuntimeException: ", "")),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 捕获业务异常
     * 捕获自定义异常
     */
    @ExceptionHandler(BusinessServiceException.class)
    public GlobalResponseEntity<Object> handleBizServiceException(BusinessServiceException e) {
        return new GlobalResponseEntity<>(500, e.getMessage());
    }

}

