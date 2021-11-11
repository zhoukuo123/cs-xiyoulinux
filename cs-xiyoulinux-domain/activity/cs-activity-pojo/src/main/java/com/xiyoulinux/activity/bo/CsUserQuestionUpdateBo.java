package com.xiyoulinux.activity.bo;

import com.xiyoulinux.enums.ActivityStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "前端传递修改问题对象")
public class CsUserQuestionUpdateBo implements Serializable {

    private static final long serialVersionUID = -5838122371225556926L;

    /**
     * 问题id
     */
    @ApiModelProperty(value = "问题id")
    private String questionId;

    /**
     * 问题的状态
     * {@link ActivityStatus}
     */
    @ApiModelProperty(value = "问题状态--未解决/已解决", example = "已解决")
    private ActivityStatus questionStatus;

}
