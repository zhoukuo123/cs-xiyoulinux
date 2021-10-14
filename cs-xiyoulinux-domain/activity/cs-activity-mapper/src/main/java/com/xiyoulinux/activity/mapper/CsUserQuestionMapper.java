package com.xiyoulinux.activity.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.activity.pojo.CsUserQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.enums.ActivityStatus;
import org.apache.ibatis.annotations.Param;


/**
 * @author qkm
 * @Entity com.xiyoulinux.activity.pojo.CsUserQuestion
 */
public interface CsUserQuestionMapper extends BaseMapper<CsUserQuestion> {
    /**
     * 分页获取未解决的问题
     *
     * @param page 分页
     * @return 未解决问题集合
     */
    IPage<CsUserQuestion> getPageUnresolvedIssues(@Param("page") IPage<CsUserQuestion> page);

    /**
     * 分页获取已解决的问题
     *
     * @param page 分页
     * @return 已解决问题集合
     */
    IPage<CsUserQuestion> getPageResolvedIssues(@Param("page") IPage<CsUserQuestion> page);

    /**
     * 根据questionId删除问题
     *
     * @param questionId 问题id
     */
    void deleteByQuestionId(@Param("questionId") String questionId);

    /**
     * 更新问题的状态
     * @param id 问题id
     * @param questionStatus 要更新的问题状态
     */
    void updateQuestionStatus(@Param("id") String id, @Param("questionStatus") ActivityStatus questionStatus);
}




