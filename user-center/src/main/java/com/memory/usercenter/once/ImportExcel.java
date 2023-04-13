package com.memory.usercenter.once;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 邓哈哈
 * 2023/4/3 22:16
 * Function: 导入Excel表
 * Version 1.0
 */
@Slf4j
public class ImportExcel {
    /**
     * 指定列的下标或者列名
     */
    public static void main(String[] args) {
        synchronousRead();
    }

    /**
     * 监听器
     */
    public static void readByListener() {
        String fileName = "D:\\Project\\星球项目\\ClientCenter\\user-center\\src\\main\\resources\\testExcel.xlsx";
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, UserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public static void synchronousRead() {
        String fileName = "D:\\Project\\星球项目\\ClientCenter\\user-center\\src\\main\\resources\\testExcel.xlsx";
        List<UserInfo> list = EasyExcel.read(fileName).head(UserInfo.class).sheet().doReadSync();
        for (UserInfo data : list) {
            log.info("读取到数据:{}", data);
        }
    }
}
