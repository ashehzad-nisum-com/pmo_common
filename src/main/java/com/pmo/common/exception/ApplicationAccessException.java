package com.pmo.common.exception;

import com.pmo.common.enums.PmoErrors;

import java.text.MessageFormat;

import static java.lang.String.format;

public class ApplicationAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;

    public ApplicationAccessException(){
        super();
    }

    public ApplicationAccessException(String code, String message) {
        super(message);
        setCode(code);
    }

    public ApplicationAccessException(String message) {
        super(message);
    }

    public ApplicationAccessException(String message, Object[] params) {
        super(params!=null?new MessageFormat(message).format(params):message);
    }

    public ApplicationAccessException(String message, Object[] params, String code) {
        super(params!=null?new MessageFormat(message).format(params):message);
        setCode(code);
    }

    public ApplicationAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationAccessException(Throwable cause) {
        super(cause);
    }

    protected ApplicationAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationAccessException(PmoErrors code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public ApplicationAccessException(PmoErrors code, String... param) {
        super(format(code.getMessage(), param));
        this.code = code.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
