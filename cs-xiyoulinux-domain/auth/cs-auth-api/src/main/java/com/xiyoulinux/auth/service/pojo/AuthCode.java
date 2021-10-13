package com.xiyoulinux.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author CoderZk
 */
@AllArgsConstructor
public enum AuthCode {
    SUCCESS(1L),
    USER_NOT_FOUND(1000L),
    /**
     * 无效的凭证
     */
    INVALID_CREDENTIAL(2000L),

    TOKEN_EXPIRED(3000L);

    @Getter
    private Long code;
}
