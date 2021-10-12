package com.xiyoulinux.enums;

/**
 * @author CoderZk
 *
 * 性别 枚举
 */
public enum Sex {
    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
