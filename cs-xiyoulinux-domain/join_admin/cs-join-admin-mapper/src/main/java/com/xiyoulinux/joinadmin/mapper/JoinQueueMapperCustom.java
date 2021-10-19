package com.xiyoulinux.joinadmin.mapper;

import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author CoderZk
 */
public interface JoinQueueMapperCustom extends MyMapper<JoinQueue> {

    /**
     * 根据面试状态 查询面试队列信息
     * @param map map.status 面试状态
     * @return 面试队列信息
     */
    List<InterviewQueueVO> queryInterviewQueueMessage(@Param("paramsMap") Map<String, Object> map);

    /**
     * 查询待面试队列最早的签到时间的用户学号
     * @param map
     * @return 学号
     */
    String querySnoBySignInTime(@Param("paramsMap") Map<String, Object> map);


}
