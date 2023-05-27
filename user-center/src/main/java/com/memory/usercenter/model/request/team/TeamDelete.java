package com.memory.usercenter.model.request.team;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/30 21:54
 * Function: 解散队伍参数
 * Version 1.0
 */
@Data
public class TeamDelete {
    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
