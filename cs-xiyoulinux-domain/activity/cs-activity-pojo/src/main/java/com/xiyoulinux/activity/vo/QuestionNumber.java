package com.xiyoulinux.activity.vo;

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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "各个问题个数")
public class QuestionNumber implements Serializable {
    private static final long serialVersionUID = -8366400262069198256L;
    @ApiModelProperty(value = "未解决问题的个数")
    private int unResolved;

    @ApiModelProperty(value = "已解决问题的个数")
    private int resolved;
}
