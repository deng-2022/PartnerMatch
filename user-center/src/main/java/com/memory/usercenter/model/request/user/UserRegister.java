package com.memory.usercenter.model.request.user;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/3/10 13:26
 * Function: 用户注册参数
 * Version 1.0
 */

@Data
public class UserRegister {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
