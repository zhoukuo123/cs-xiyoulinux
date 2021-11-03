package com.xiyoulinux.search.service;

import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.common.PageInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author qkm
 */
public interface ISearchFromEs {

    /**
     * 根据 key 搜索 动态
     *
     * @param pageInfo
     * @param userId
     * @return
     * @throws IOException
     */
    PageActivityInfo searchByKey(PageInfo pageInfo, String userId);

    /**
     * 根据 key 搜索 动态 按照时间排序
     *
     * @param pageInfo
     * @param userId
     * @return
     * @throws IOException
     */
    PageActivityInfo searchByKeyOrderByCreateTime(PageInfo pageInfo, String userId);

    /**
     * 搜索框根据key自动补全
     * @param key
     * @return
     */
    List<String> searchBoxAutoCompletion(String key);
}
