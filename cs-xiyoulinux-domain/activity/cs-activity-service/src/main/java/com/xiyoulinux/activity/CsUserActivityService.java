package com.xiyoulinux.activity;

import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.activity.vo.CsActivityVo;

import java.util.List;

/**
 * @author qkm
 */
public interface CsUserActivityService {
    /**
     * 添加动态
     *
     * @param csUserActivity object
     */
    void addActivity(CsUserActivity csUserActivity);

    /**
     * 根据动态id删除动态
     *
     * @param id id
     */
    void deleteActivity(String id);

    /**
     * 获取前1000条动态
     *
     * @return 动态集合
     */
    List<CsActivityVo> getCurrentActivity();

    /**
     * 获取从1000条开始的动态
     *
     * @return 动态集合
     */
    List<CsActivityVo> getOldActivity();


    /**
     * 根据userId获取所有动态
     *
     * @param userId 用户id
     * @return 该用户发表的动态集合
     */
    List<CsActivityVo> getActivityByUserId(String userId);

    /**
     * 获取未解决的问题
     *
     * @return 未解决问题集合
     */
    public List<CsActivityVo> getUnresolvedIssues();

    /**
     * 获取已解决的问题
     *
     * @return 已解决问题集合
     */
    public List<CsActivityVo> getResolvedIssues();

    /**
     * 获取发布的任务数
     *
     * @return 任务集合
     */
    public List<CsActivityVo> getTasks();

}
