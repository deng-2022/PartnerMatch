package com.memory.usercenter.model.request.team;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/29 21:36
 * Function: 退出队伍参数
 * Version 1.0
 */
@Data
public class TeamQuit {
    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 剩余人数
     */
    private Integer joinNum;
}
