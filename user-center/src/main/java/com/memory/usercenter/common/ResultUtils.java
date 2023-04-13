package com.memory.usercenter.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author 邓哈哈
 * 2023/3/19 21:47
 * Function:
 * Version 1.0
 */

public class ResultUtils {
    // 请求成功
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
}
