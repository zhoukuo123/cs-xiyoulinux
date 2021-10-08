package com.xiyoulinux.user.service.center;

import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.center.CenterUserBO;
import org.springframework.web.bind.annotation.*;

/**
 * @author CoderZk
 */
public interface CenterUserService {
    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    CsUser queryUserInfo(String userId);

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     */
    CsUser updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     *
     * @param userId
     * @param faceUrl
     * @return
     */
    CsUser updateUserFace(String userId, String faceUrl);
}
