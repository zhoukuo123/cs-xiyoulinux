package com.xiyoulinux.enums;

/**
 * @author CoderZk
 *
 * 面试评级 枚举
 */
public enum InterviewRating {
    S(5, "非常棒, 极力推荐"),
    A_PLUS(4, "很不错"),
    A(3, "还可以"),
    B(2, "一般"),
    C(1, "较差");

    public final Integer code;
    public final String value;

    InterviewRating(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
