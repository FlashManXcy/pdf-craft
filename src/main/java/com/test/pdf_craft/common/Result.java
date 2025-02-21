package com.test.pdf_craft.common;

import com.test.pdf_craft.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private T data;

    private String code;

    private String message;


    public Result setBaseInfo(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public Result setBaseInfo(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
        return this;
    }

    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public Result(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }


    public static <T> Result<T> success() {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCodeEnum.FAIL.getCode(), ResultCodeEnum.FAIL.getMessage());
    }

    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(code, msg);
    }
}