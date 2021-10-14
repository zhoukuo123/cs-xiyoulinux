package com.xiyoulinux.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.file.pojo.CsActivityCommentFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author qkm
 * @Entity com.xiyoulinux.file.pojo.CsActivityCommentFile
 */
@Repository
public interface CsActivityCommentFileMapper extends BaseMapper<CsActivityCommentFile> {
    /**
     * 根据commentIdList查询对应文件
     *
     * @param commentIdList 评论id
     * @return 文件集合
     */
    List<CsActivityCommentFile> selectAllByCommentIdList(@Param("commentIdList") List<String> commentIdList);

    /**
     * 根据评论id 删除 评论的文件信息
     *
     * @param commentId 评论id
     */
    void delByCommentId(@Param("commentId") String commentId);

    /**
     * 根据动态id 删除 所有评论的文件信息
     *
     * @param activityId 评论id
     */
    void delCommentByActivityId(@Param("activityId") String activityId);
}




