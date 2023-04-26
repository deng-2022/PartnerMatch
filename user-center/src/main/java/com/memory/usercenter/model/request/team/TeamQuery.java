package com.memory.usercenter.model.request.team;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/4/20 10:10
 * Function: 查询队伍参数
 * Version 1.0
 */

@Data
public class TeamQuery {
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

}
