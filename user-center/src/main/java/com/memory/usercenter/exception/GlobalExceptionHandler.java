package com.memory.usercenter.exception;

import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 邓哈哈
 * 2023/3/20 20:59
 * Function: 全局异常处理器
 * Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获局部业务代码下的自定义异常信息
     *
     * @param e 自定义异常
     * @return 封装返回失败状态
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        log.info("" + e.getCode());
        log.info(e.getMessage());
        log.info(e.getDescription());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 捕获common/ErrorCode下封装的全局自定义异常信息
     *
     * @param e 自定义异常
     * @return 封装返回失败状态
     */
    //捕获自定义的异常2
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
