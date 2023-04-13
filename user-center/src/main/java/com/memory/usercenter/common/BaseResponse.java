package com.memory.usercenter.common;

/**
 * @author 邓哈哈
 * 2023/3/19 21:44
 * Function: 通用返回类
 * Version 1.0
 */

public class BaseResponse<T> {
    // 状态码
    private int code;
    // 数据
    private T data;
    // 信息
    private String message;
    // 描述
    private String description;

    /**
     *
     * @param code
     * @param data
     * @param message
     * @param description
     */
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }


    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.data = null;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.data = null;
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
