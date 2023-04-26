package com.memory.usercenter.model.request.user;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/3/10 13:34
 * Function: 用户登录参数
 * Version 1.0
 */
@Data
public class UserLogin {
    private String userAccount;
    private String userPassword;
}
