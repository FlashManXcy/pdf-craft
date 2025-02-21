package com.test.pdf_craft.enums;

public enum ResultCodeEnum {
    SUCCESS("00000", "success"),
    FAIL("99999", "fail"),
    DATE_FORMAT_INCORRECT("00001", "The date format is incorrect"),
    TIME_IS_INCORRECT("00002", "The start date cannot be longer than the end date");

    private final String code;
    private final String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}