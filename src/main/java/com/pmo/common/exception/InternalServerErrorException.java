package com.pmo.common.exception;

public class InternalServerErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public InternalServerErrorException(){
        super();
    }


    public InternalServerErrorException(String message) {
        super(message);
    }


    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    protected InternalServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

