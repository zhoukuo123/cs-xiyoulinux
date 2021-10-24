package com.xiyoulinux.joinadmin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CoderZk
 */
@ApiModel(value = "面试人信息和面试评级VO(用于决策)", description = "面试人信息和面试评级VO(用于决策)")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntervieweeInfoAndGradeVO {

    @ApiModelProperty(value = "被面试人id, 前端不用展示", name = "uid", example = "123123SX234", required = false)
    private String uid;

    private String sno;
    private String name;
    private String className;
    private String mobile;

    @ApiModelProperty(value = "面试评级, 如果为null, 则说明尚未面试!", name = "grade", example = "5")
    private Integer grade;
}
