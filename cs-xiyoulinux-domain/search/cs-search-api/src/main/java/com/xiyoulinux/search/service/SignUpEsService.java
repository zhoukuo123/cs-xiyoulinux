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

    /**
     * 从面试结果中搜索面试人
     * @param keywords 搜索关键字
     * @param round 轮次
     * @param status 状态
     * @param status2 备用字段, 可能为null
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchFromInterviewResult(String keywords, Integer round, Integer status, Integer status2, Integer page, Integer pageSize);
}
