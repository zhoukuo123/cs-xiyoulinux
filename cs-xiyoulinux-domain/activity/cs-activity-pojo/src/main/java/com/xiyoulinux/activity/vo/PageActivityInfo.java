package com.xiyoulinux.activity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页动态信息
 *
 * @author qkm
 */
@ApiModel(description = "分页获取动态")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageActivityInfo implements Serializable {

    private static final long serialVersionUID = -5844122151343534886L;
    @ApiModelProperty(value = "分页动态信息")
    private List<CsUserActivityVo> activityInfos;

    @ApiModelProperty(value = "是否有更多的动态(分页)")
    private Boolean hasMore;
}
