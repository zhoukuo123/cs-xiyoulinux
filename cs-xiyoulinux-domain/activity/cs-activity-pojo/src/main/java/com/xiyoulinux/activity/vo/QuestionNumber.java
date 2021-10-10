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
@ApiModel(description = "各个问题个数")
public class QuestionNumber {
    @ApiModelProperty(value = "未解决问题的个数")
    private int unResolved;

    @ApiModelProperty(value = "已解决问题的个数")
    private int resolved;
}
