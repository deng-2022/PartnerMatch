package com.memory.usercenter.model.request.team;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/27 21:04
 * Function: 加入队伍参数
 * Version 1.0
 */

@Data
public class TeamJoin {
    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

    /**
     * 成员数量
     */
    private Integer joinNum;
}
