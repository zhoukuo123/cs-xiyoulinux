package com.xiyoulinux.activity.bo;

import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "前端传递删除用户动态对象")
public class CsUserActivityDeleteBo implements Serializable {

    private static final long serialVersionUID = 7776738210882198241L;
    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id")
    private String activityId;


    /**
     * 发起问题or发起动态or发起讲座or发起任务(数据库存储0/1/3/2)
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
