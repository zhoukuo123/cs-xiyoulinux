package com.xiyoulinux.activity.comment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.activity.comment.entity.CsUserLikes;
import org.apache.ibatis.annotations.Param;

/**
 * @author qkm
 */
public interface CsUserLikesMapper extends BaseMapper<CsUserLikes> {

    /**
     * 取消点赞，通过userId和commentId
     * @param csUserId userId
     * @param csCommentId commentId
     */
    void deleteByCsUserIdAndCsCommentId(@Param("csUserId") String csUserId, @Param("csCommentId") String csCommentId);

}




