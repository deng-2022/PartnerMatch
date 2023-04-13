package com.memory.usercenter.model.request;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/3/10 13:26
 * Function:
 * Version 1.0
 */

@Data
public class UserRegisterRequest {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
