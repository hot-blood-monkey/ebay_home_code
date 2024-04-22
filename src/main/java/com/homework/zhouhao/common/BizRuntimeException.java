package com.homework.zhouhao.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BizRuntimeException extends RuntimeException {
    private Integer statusCode;
    private String message;

    public BizRuntimeException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.statusCode = errorCode.getCode();
        this.message = errorCode.getDesc();
    }

    public BizRuntimeException(ErrorCode errorCode, String overrideDesc) {
        super(errorCode.getDesc());
        this.statusCode = errorCode.getCode();
        this.message = overrideDesc;
    }

    public BizRuntimeException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public BizRuntimeException(String message, Throwable cause, Integer statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
        this.message = message;
    }

    public static BizRuntimeException of(ErrorCode errorCode) {
        return new BizRuntimeException(errorCode);
    }

    public static BizRuntimeException of(ErrorCode errorCode, String overrideDesc) {
        return new BizRuntimeException(errorCode, overrideDesc);
    }

    public static BizRuntimeException ofFormat(ErrorCode errorCode, Object... args) {
        String message = MessageFormat.format(errorCode.getDesc(), args);
        return new BizRuntimeException(errorCode, message);
    }

}
