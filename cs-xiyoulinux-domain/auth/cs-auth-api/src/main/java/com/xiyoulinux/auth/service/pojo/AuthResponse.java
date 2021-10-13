package com.xiyoulinux.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author CoderZk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {
    private Account account;
    private Long code;
}
