package com.xiyoulinux.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qkm
 * @Entity com.xiyoulinux.dynamic.entity.CsUserDynamic
 */
@Repository
public interface CsUserActivityMapper extends BaseMapper<CsUserActivity> {
    /**
     * 查询最近的1000条数据
     *
     * @return 动态集合
     */
    List<CsUserActivity> selectCurrentActivity();

    /**
     * 查询从1000条开始的动态
     *
     * @return 动态集合
     */
    List<CsUserActivity> selectOldActivity();

    /**
     * 通过userId查找发表过的动态
     *
     * @param userId 用户id
     * @return 动态集合
     */
    List<CsUserActivity> selectByUserId(@Param(("userId")) String userId);

    /**
     * 获取未解决的问题
     *
     * @return 未解决问题集合
     */
    public List<CsUserActivity> getUnresolvedIssues();

    /**
     * 获取已解决的问题
     *
     * @return 已解决问题集合
     */
    public List<CsUserActivity> getResolvedIssues();

    /**
     * 获取发布的任务数
     *
     * @return 任务集合
     */
    public List<CsUserActivity> getTasks();
}




