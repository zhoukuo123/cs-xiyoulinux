package com.xiyoulinux.user.service;

import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.UserBO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CoderZk
 */
public interface UserService {
    /**
     * 判断手机号是否存在
     */
    boolean queryPhoneIsExist(@RequestParam("phone") String phone);

    /**
     * 创建用户
     */
    CsUser createUser(@RequestBody UserBO userBO);

    /**
     * 检索用户名和密码是否匹配, 用于登录
     */
    CsUser queryUserForLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password);

    /**
     * 找回密码
     */
    Integer retrievePassword(@RequestBody UserBO userBO);
}
