package com.xiyoulinux.exception;

import com.xiyoulinux.enums.InterviewStatus;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.*;
import com.xiyoulinux.pojo.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CoderZk
 */
@RestControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public JSONResult handlerException(Exception e) {
        if (e instanceof AuthenticationException) {

            return JSONResult.errorMsg(((AuthenticationException) e).getCode(), e.getMessage());

        } else if (e instanceof MethodArgumentNotValidException) {
            Map<String, String> errorMap = new HashMap<>();
            BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();
            List<FieldError> errorList = result.getFieldErrors();
            for (FieldError error : errorList) {
                // 发生验证错误所对应的某一个属性
                String errorField = error.getField();
                //  验证错误的信息
                String errorMsg = error.getDefaultMessage();
                errorMap.put(errorField, errorMsg);
            }

            return JSONResult.errorMap(ReturnCode.INVALID_PARAM.code, ReturnCode.INVALID_PARAM.value, errorMap);

        } else if (e instanceof PassportException) {

            return JSONResult.errorMsg(((PassportException) e).getCode(), e.getMessage());

        } else if (e instanceof UserJoinException) {

            return JSONResult.errorMsg(((UserJoinException) e).getCode(), e.getMessage());

        } else if (e instanceof SignUpException) {

            return JSONResult.errorMsg(((SignUpException) e).getCode(), e.getMessage());

        } else if (e instanceof InterviewException) {

            return JSONResult.errorMsg(((InterviewException) e).getCode(), e.getMessage());

        } else {
            // 系统异常
            return JSONResult.errorMsg(ReturnCode.SYSTEM_EXCEPTION.code, e.getMessage());
        }
    }

}
