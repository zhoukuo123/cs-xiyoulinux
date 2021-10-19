package com.xiyoulinux.joinadmin.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CoderZk
 */
@ApiModel(value = "面试记录VO", description = "面试记录VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewRecordVO {
    private String basicSkill;
    private String extraSkill;
    private String overall;
    private Integer grade;
    private Integer round;
    /**
     * 面试官姓名
     */
    private String interviewerName;
}
