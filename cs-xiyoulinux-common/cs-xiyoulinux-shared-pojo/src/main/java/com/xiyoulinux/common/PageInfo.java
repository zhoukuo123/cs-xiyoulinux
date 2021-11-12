package com.xiyoulinux.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端传递统一分页对象
 *
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页信息")
public class PageInfo {

    /**
     * 第几页
     */
    @ApiModelProperty(value = "第几页")
    private int page = 1;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小")
    private int size = 10;

    /**
     * 传递的关键字
     */
    @ApiModelProperty(value = "传递的关键字", required = false, notes = "搜索框才会有该关键字否则没有")
    private String key;
}
