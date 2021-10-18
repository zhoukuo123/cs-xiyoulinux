package com.xiyoulinux.user.service;

import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.UserBO;

/**
 * @author CoderZk
 */
public interface UserService {
    /**
     * 判断手机号是否存在
     */
    boolean queryPhoneIsExist(String phone);

    /**
     * 创建用户
     */
    CsUser createUser(UserBO userBO);

    /**
     * 检索手机号和密码是否匹配, 用于登录
     */
    CsUser queryUserForLogin(String phone, String password);

    /**
     * 找回密码
     */
    Integer retrievePassword(UserBO userBO);

    /**
     * 根据uid查询user
     */
    CsUser queryUserByUid(String uid);

    /**
     * 根据uid查询userName
     */
    String queryUserNameByUid(String uid);
}
