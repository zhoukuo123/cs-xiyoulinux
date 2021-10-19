package com.xiyoulinux.joinadmin.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author CoderZk
 */
@ApiModel(value = "面试评价记录VO(包含被面试人信息)", description = "面试评价记录VO(包含被面试人信息)")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewEvaluationRecordVO {
    private String name;
    private String className;

    private String basicSkill;
    private String extraSkill;
    private String overall;
    private Integer grade;
    private Integer round;

    private Date interviewTime;
}
