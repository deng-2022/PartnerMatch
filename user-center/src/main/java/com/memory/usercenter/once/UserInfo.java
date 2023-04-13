package com.memory.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/3 22:08:02
 * Function: 映射对象
 * Version 1.0
 */


@Data
public class UserInfo {
    @ExcelProperty("成员编号")
    private String planetCode;


    @ExcelProperty("成员昵称")
    private String username;
}