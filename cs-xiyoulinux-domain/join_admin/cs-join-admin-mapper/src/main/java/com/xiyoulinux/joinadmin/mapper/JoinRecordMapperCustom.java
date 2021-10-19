package com.xiyoulinux.joinadmin.mapper;

import com.xiyoulinux.joinadmin.pojo.JoinRecord;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewEvaluationRecordVO;
import com.xiyoulinux.joinadmin.pojo.vo.InterviewQueueVO;
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


}