package com.xiyoulinux.joinadmin.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CoderZk
 */
@ApiModel(value = "报名记录VO", description = "报名记录VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRecordVO {

    private String sno;
    private String name;
    private String className;
    private String mobile;
    private String interviewStatus;

}
