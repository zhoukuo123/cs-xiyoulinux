package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.vo.PageActivityInfo;

/**
 * @author qkm
 */
public interface IActivityService {
    /**
     * 根据用户id获取用户的动态信息
     * @param userId userId
     * @param page 第几页
     * @return 分页的用户信息
     */
    PageActivityInfo getPageActivityByUserId(String userId,int page);
}
