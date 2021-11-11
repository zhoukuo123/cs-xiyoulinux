package com.xiyoulinux.activity.bo;

import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "前端传递修改任务对象")
public class CsUserTaskUpdateBo implements Serializable {

    private static final long serialVersionUID = 954671917374573419L;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 任务开始时间
     */
    @ApiModelProperty(value = "任务开始时间")
    private Date taskStartTimeDate;

/**
     * 任务结束时间
     */
    @ApiModelProperty(value = "任务结合时间")
    private Date taskEndTimeDate;

    /**
     * 任务/的状态
     * {@link ActivityStatus}
     */
    @ApiModelProperty(value = "任务的状态--进行中/待进行/已完成", example = "已完成")
    private ActivityStatus taskStatus;

}
