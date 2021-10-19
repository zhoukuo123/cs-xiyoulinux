package com.xiyoulinux.joinadmin.pojo.bo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CoderZk
 */
@ApiModel(value = "面试评价BO", description = "面试评价BO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewEvaluationBO {
    private String basicSkill;
    private String extraSkill;
    private String overall;
    private Integer grade;
    /**
     * 目前是第几轮面试
     */
    private Integer round;

    private String interviewerUid;
}
