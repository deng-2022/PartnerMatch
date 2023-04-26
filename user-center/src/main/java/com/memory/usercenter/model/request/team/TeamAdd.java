package com.memory.usercenter.model.request.team;

import lombok.Data;

import java.util.Date;

/**
 * @author 邓哈哈
 * 2023/4/20 14:22
 * Function: 队伍新增参数
 * Version 1.0
 */
@Data
public class TeamAdd {
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
