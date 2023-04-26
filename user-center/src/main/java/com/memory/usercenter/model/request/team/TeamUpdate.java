package com.memory.usercenter.model.request.team;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/26 14:00
 * Function: 修改队伍参数
 * Version 1.0
 */
@Data
public class TeamUpdate {
    /***
     * 队伍id
     */
    private Long id;

    /***
     * 队长id
     */
    private Long userId;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
