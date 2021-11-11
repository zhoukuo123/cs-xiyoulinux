package com.xiyoulinux.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiyoulinux.activity.entity.CsUserActivity;
import com.xiyoulinux.enums.ActivityStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author qkm
 * @Entity com.xiyoulinux.activity.pojo.CsUserActivity
 */
public interface CsUserActivityMapper extends BaseMapper<CsUserActivity> {

    /**
     * 以分页的形式查找数据，每页20条
     *
     * @param page 分页
     * @return 动态集合
     */
    IPage<CsUserActivity> selectPageActivity(@Param("page") IPage<CsUserActivity> page);

    /**
     * 通过userId查找发表过的动态
     *
     * @param page   分页
     * @param userId 用户id
     * @return 动态集合
     */
    IPage<CsUserActivity> selectPageByUserId(@Param("page") IPage<CsUserActivity> page, @Param(("userId")) String userId);

    /**
     * 更新结束时间
     *
     * @param id             id
     * @param startTime      要更新的开始时间
     * @param endTime        要更新的结束时间
     * @param activityStatus 要更新的状态
     */
    void updateEndTimeAndStatusById(@Param("id") String id,@Param("startTime") Date startTime,@Param("endTime") Date endTime,
                                    @Param("activityStatus") ActivityStatus activityStatus);

    /**
     * 更新问题的状态
     *
     * @param id             问题id
     * @param questionStatus 要更新的问题状态
     */
    void updateQuestionStatus(@Param("id") String id, @Param("questionStatus") ActivityStatus questionStatus);

    /**
     * 根据id 类型 状态 删除动态
     *
     * @param id     动态id
     * @param type   动态的类型
     * @param status 动态的状态
     * @return 更新记录的数字
     */
    int deleteByIdAndTypeAndStatus(@Param("id") String id, @Param("type") Integer type, @Param("status") Integer status);
}




