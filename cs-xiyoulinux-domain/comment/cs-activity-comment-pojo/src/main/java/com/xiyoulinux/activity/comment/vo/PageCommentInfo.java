package com.xiyoulinux.activity.comment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页评论信息
 * @author qkm
 */
@ApiModel(description = "分页获取动态底下的评论")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCommentInfo {

    @ApiModelProperty(value = "分页评论信息")
    private List<CsUserActivityCommentVo> commentInfos;

    @ApiModelProperty(value = "是否有更多的评论(分页)")
    private Boolean hasMore;
}
