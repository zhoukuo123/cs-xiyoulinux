package com.xiyoulinux.activity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页问题信息
 * @author qkm
 */
@ApiModel(description = "分页获取问题")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQuestionInfo {

    @ApiModelProperty(value = "分页问题信息")
    private List<CsUserQuestionVo> questionInfos;

    @ApiModelProperty(value = "是否有更多的问题(分页)")
    private Boolean hasMore;
}
