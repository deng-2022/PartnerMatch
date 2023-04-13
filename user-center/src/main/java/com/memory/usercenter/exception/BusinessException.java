package com.memory.usercenter.exception;

import com.memory.usercenter.common.ErrorCode;

/**
 * @author 邓哈哈
 * 2023/3/20 20:07
 * Function: 自定义异常类
 * Version 1.0
 */

public class BusinessException extends RuntimeException {
    // 状态码
    private final int code;
    // 异常描述
    private final String description;

    /**
     * 局部业务代码里自定义的异常信息
     *
     * @param message     异常信息
     * @param code        异常状态码
     * @param description 异常描述
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * common/ErrorCode下封装的全局自定义异常信息
     *
     * @param errorCode 全局自定义异常
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
