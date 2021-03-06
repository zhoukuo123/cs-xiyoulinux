package com.xiyoulinux.activity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页任务信息
 *
 * @author qkm
 */
@ApiModel(description = "分页获取任务")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageTaskInfo implements Serializable {

    private static final long serialVersionUID = -3462646296682085418L;
    @ApiModelProperty(value = "分页任务信息")
    private List<CsUserTaskVo> taskInfos;

    @ApiModelProperty(value = "是否有更多的任务(分页)")
    private Boolean hasMore;

}
