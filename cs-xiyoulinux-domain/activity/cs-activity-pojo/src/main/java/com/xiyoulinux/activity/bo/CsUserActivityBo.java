package com.xiyoulinux.activity.bo;

import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.enums.ActivityStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "前端传递用户动态对象")
public class CsUserActivityBo implements Serializable {
    private static final long serialVersionUID = 1420004850360161502L;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 活动的标题
     */
    @ApiModelProperty(value = "活动的标题")
    private String activityTitle;


    /**
     * 问题or动态or讲座or任务的内容
     */
    @ApiModelProperty(value = "动态的内容")
    private String activityContent;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "动态的创建时间", example = "2021-12-24T16:00:00.000Z")
    private Date activityCreateTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "动态的结束时间", example = "2021-12-24T16:00:00.000Z")
    private Date activityEndTime;

    /**
     * 发起问题or发起动态or发起讲座or发起任务(数据库存储0/1/2/3)
     * {@link ActivityType}
     */
    @ApiModelProperty(value = "动态的类型（发起问题/发起动态/发起讲座/发起任务）", example = "发起问题")
    private ActivityType activityType;

    /**
     * 任务/讲座/问题的状态
     * {@link ActivityStatus}
     */
    @ApiModelProperty(value = "动态状态（问题状态--未解决/已解决、讲座/任务的状态--进行中/待进行/已完成）", example = "未解决")
    private ActivityStatus activityStatus;

}