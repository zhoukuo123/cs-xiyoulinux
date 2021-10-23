package com.xiyoulinux.joinadmin.mapper;

import com.xiyoulinux.joinadmin.pojo.JoinRecord;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewEvaluationRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoAndGradeVO;
import com.xiyoulinux.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JoinRecordMapperCustom extends MyMapper<JoinRecord> {
    /**
     * 根据面试官uid 查询该面试官的面试评价记录
     *
     * @param map map.interviewer 面试官uid
     * @return 面试评价记录List
     */
    List<InterviewEvaluationRecordVO> queryInterviewEvaluationRecord(@Param("paramsMap") Map<String, Object> map);

    /**
     * 根据 round 和 status 查询面试信息和被面试人信息 用于决策
     *
     * @param map map.round  map.status
     * @return 面试评价记录List
     */
    List<IntervieweeInfoAndGradeVO> queryInterviewInfoAndIntervieweeInfo(@Param("paramsMap") Map<String, Object> map);


}