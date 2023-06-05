package com.memory.usercenter.constant;

/**
 * @author 邓哈哈
 * 2023/4/20 10:36
 * Function: 队伍状态枚举
 * Version 1.0
 */
public enum TeamStatusEnum {
    /**
     * 0 - 公开, 在队伍大厅中可以直接加入
     */
    PUBLIC(0, "公开"),

    /**
     * 1 - 私有, 在队伍大厅中不可以直接加入
     */
    PRIVATE(1, "私有"),

    /**
     * 2 - 公开且加密, 加入队伍需要密码
     */
    SECRET(2, "加密");

    /**
     * 状态码
     */
    private int value;

    /**
     * 状态描述
     */
    private String text;

    /**
     * 判断用户状态
     *
     * @param value 用户状态
     * @return 存在与否
     */
    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}