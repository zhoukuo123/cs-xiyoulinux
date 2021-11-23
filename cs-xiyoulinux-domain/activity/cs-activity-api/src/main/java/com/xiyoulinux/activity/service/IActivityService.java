package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.common.PageInfo;

/**
 * @author qkm
 */
public interface IActivityService {
    /**
     * 根据用户id获取用户的动态信息
     *
     * @param userId   userId
     * @param pageInfo 第几页
     * @return 分页的用户信息
     */
    PageActivityInfo getPageActivityByUserId(String userId, PageInfo pageInfo);
}
