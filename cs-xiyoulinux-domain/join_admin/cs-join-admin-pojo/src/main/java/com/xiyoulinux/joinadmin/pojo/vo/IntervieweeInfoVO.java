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
@ApiModel(value = "被面试人信息VO", description = "被面试人信息VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntervieweeInfoVO {
    private String sno;
    private String name;
    private String className;
    /**
     * 目前是第几轮面试
     */
    private Integer round;
}
