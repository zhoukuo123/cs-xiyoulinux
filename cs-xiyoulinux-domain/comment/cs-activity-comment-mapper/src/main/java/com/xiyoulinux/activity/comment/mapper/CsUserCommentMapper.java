package com.xiyoulinux.activity.comment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiyoulinux.activity.comment.bo.ActivityCommentNumber;
import com.xiyoulinux.activity.comment.entity.CsUserActivityComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 给评论点赞
     *
     * @param id      评论id
     * @return 更新的行数
     */
    int likesComment(@Param("id") String id);

    /**
     * 给评论取消点赞
     *
     * @param id      评论id
     * @return 更新的行数
     */
    Integer dislikeComment(@Param("id") String id);

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
}




