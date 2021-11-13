package com.xiyoulinux.search.service;

import com.xiyoulinux.pojo.PagedGridResult;

/**
 * @author CoderZk
 */
public interface SignUpEsService {
    /**
     * 搜索报名记录
     * @param keywords 搜索关键字
     * @param sort 根据哪一个字段排序
     * @param page 第几页
     * @param pageSize
     * @return
     */
    PagedGridResult searchSignUpRecords(String keywords, String sort, Integer page, Integer pageSize);
}
