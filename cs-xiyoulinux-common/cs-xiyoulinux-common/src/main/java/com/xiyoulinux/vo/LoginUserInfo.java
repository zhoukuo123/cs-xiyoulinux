package com.xiyoulinux.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息 存储在token中的
 *
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {

    /**
     * 用户 id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

}
