package com.xiyoulinux.activity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "各个任务的个数")
public class TaskNumber {
    @ApiModelProperty(value = "进行中任务的个数")
    private int doing;

    @ApiModelProperty(value = "待进行任务的个数")
    private int future;

    @ApiModelProperty(value = "已完成的任务的个数")
    private int achieved;

}
