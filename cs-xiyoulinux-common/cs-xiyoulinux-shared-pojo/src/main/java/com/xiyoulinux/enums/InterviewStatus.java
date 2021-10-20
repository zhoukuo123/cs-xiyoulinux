package com.xiyoulinux.enums;

/**
 * @author CoderZk
 *
 * 面试状态枚举
 */
public enum InterviewStatus {
    PASS(1, "通过"),
    OUT(-1, "未通过"),
    WAIT_INTERVIEW(0, "等待面试"),
    PENDING_DECISION(2, "已被面试, 等待决策");

    public final Integer code;
    public final String value;

    InterviewStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
