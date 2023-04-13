package com.memory.usercenter.model.request;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/3/10 13:34
 * Function:
 * Version 1.0
 */
@Data
public class UserLoginRequest {
    private String userAccount;
    private String userPassword;
}
