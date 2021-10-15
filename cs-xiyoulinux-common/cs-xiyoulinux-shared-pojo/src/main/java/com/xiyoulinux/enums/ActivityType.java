package com.xiyoulinux.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author qkm
 */
public enum ActivityType {

    /**
     * 动态类型--问题
     */
    QUESTION(0, "发起问题"),

    /**
     * 动态类型--动态
     */
    ACTIVITY(1, "发起动态"),

    /**
     * 动态类型--任务
     */
    TASK(2, "发起任务"),

    /**
     * 动态类型--讲座
     */
    LECTURE(3, "发起讲座");


    ActivityType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public final String description;

    @EnumValue
    public final Integer code;

    @JsonValue
    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据 description 获取到 ActivityType
     */
    public static ActivityType of(String description) {

        Objects.requireNonNull(description);

        return Stream.of(values())
                .filter(bean -> bean.description.equals(description))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(description + " not exists")
                );
    }
}
