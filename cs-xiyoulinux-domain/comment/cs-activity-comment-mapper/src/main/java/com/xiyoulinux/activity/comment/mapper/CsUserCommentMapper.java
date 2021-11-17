package com.xiyoulinux.activity.comment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiyoulinux.activity.comment.bo.ActivityCommentNumber;
import com.xiyoulinux.activity.comment.entity.CsUserActivityComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author qkm
 * @Entity com.xiyoulinux.activity.comment.pojo.CsUserActivityComment
 */
@Repository
public interface CsUserCommentMapper extends BaseMapper<CsUserActivityComment> {

    /**
     * 根据动态id获取动态的所有评论
     *
     * @param activityId 动态id
     * @param page 第几页
     * @return 评论集合
     */
    IPage<CsUserActivityComment> selectPageByActivityId(@Param("activityId") String activityId,
                                                        @Param("page") IPage<CsUserActivityComment> page);

    /**
     * 获取动态的评论数目
     *
     * @param activityIdList 动态id
     * @return 动态-评论
     */
    List<ActivityCommentNumber> getCommentNumberByActivityId(@Param("activityIdList") List<String> activityIdList);

    /**
     * 删除当前动态的所有评论
     *
     * @param activityId 动态id
     */
    void deleteByActivityId(@Param("activityId") String activityId);

    /**
     * 根据动态id获取动态的所有评论然后根据likes排序
     * @param activityId
     * @param csUserActivityCommentPage
     * @return
     */
    IPage<CsUserActivityComment> selectPageByActivityIdOrderByLikes(@Param("activityId") String activityId,
                                                                    @Param("csUserActivityCommentPage") IPage<CsUserActivityComment> csUserActivityCommentPage);

    /**
     * 同步redis 点赞数目到数据库
     * @param likes
     */
    void mergeLikes(@Param("likes") Map<String, Integer> likes);
}




