package com.xiyoulinux.activity.comment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.activity.comment.entity.CsUserLikes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author qkm
 */
public interface CsUserLikesMapper extends BaseMapper<CsUserLikes> {

    /**
     * 取消点赞，通过userId和commentId
     *
     * @param csUserId    userId
     * @param csCommentId commentId
     */
    void deleteByCsUserIdAndCsCommentId(@Param("csUserId") String csUserId, @Param("csCommentId") String csCommentId);


    /**
     * 删除点赞信息 byActivityId
     *
     * @param csActivityId 动态id
     */
    void deleteLikesByCsActivityId(@Param("csActivityId") String csActivityId);

    /**
     * 删除点赞信息 byActivityId
     *
     * @param commentIdList 评论id
     * @param userId 用户id
     * @return userId List
     */
    List<String> selectLikesUserId(@Param("commentIdList") Set<String> commentIdList, @Param("userId") String userId);
}




