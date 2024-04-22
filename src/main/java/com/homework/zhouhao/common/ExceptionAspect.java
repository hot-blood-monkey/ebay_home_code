package com.homework.zhouhao.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionAspect {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity defaultExceptionHandler(Throwable e) {
        log.error(e.getMessage(), e);
        return buildErrorModel(ErrorCode.SERVICE_INTERNAL_ERROR.getCode(), e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return buildErrorModel(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BizRuntimeException.class)
    public ResponseEntity bizRuntimeExceptionHandler(BizRuntimeException e) {
        log.error(e.getMessage(), e);
        return buildErrorModel(e.getStatusCode(), e.getMessage());
    }


    public static ResponseEntity buildErrorModel(int httpCode, String message) {
        return ResponseEntity.status(httpCode).body(message);
    }

    public static ResponseEntity buildErrorModel(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getCode()).body(errorCode.getDesc());
    }
}
