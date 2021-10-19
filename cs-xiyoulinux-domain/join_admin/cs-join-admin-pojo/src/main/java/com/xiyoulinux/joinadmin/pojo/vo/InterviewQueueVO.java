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
@ApiModel(value = "面试队列VO(待面试队列, 已面试队列)", description = "面试队列VO(待面试队列, 已面试队列)")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewQueueVO {
    private String sno;
    private String name;
    private String className;
    private Date signInTime;
    /**
     * null: 待安排面试
     * 有值: 已被 interviewerName 面试
     */
    private String interviewerName;
    private Integer round;
}
