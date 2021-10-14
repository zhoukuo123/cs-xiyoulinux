package com.xiyoulinux.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiyoulinux.file.pojo.CsActivityCommentFile;
import com.xiyoulinux.file.pojo.CsActivityFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author qkm
 * @Entity com.xiyoulinux.file.pojo.CsActivityFile
 */
@Repository
public interface CsActivityFileMapper extends BaseMapper<CsActivityFile> {

    /**
     * 根据动态id获取对应的文件
     *
     * @param activityIdList 动态idList
     * @return 文件集合
     */
    List<CsActivityFile> selectAllByActivityIdList(@Param("activityIdList") List<String> activityIdList);

    /**
     * 删除动态对应的文件信息
     *
     * @param activityId 动态id
     */
    void delActivityByActivityId(@Param("activityId") String activityId);
}




