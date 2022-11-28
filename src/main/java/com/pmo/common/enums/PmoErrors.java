package com.pmo.common.enums;

public enum PmoErrors {


    PMO_ERRORS("9999", "Error occurred please contact support");

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    PmoErrors(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
