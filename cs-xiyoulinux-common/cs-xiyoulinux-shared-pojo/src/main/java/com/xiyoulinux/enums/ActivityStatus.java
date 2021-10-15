package com.xiyoulinux.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author qkm
 */
public enum ActivityStatus {

    /**
     * 问题状态--未解决
     */
    UNRESOLVED(1, "未解决"),

    /**
     * 问题状态--已解决
     */
    RESOLVED(0, "已解决"),

    /**
     * 讲座/任务--进行中
     */
    ONGOING(2, "进行中"),

    /**
     * 讲座/任务--待进行
     */
    PENDING(3, "待进行"),

    /**
     * 讲座/任务--已完成
     */
    ACHIEVE(4, "已完成");


    ActivityStatus(Integer code, String description) {
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

    /**
     * 根据 description 获取到 ActivityStatus
     */
    public static ActivityStatus of(String description) {

        Objects.requireNonNull(description);

        return Stream.of(values())
                .filter(bean -> bean.description.equals(description))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(description + " not exists")
                );
    }
}
