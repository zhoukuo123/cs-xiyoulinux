package com.xiyoulinux.joinadmin.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author CoderZk
 */
@ApiModel(value = "批量决策DTO", description = "用于批量决策发送消息到MQ")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchDecisionDTO implements Serializable {

    private List<String> uids;

    /**
     * 第几轮面试
     */
    private Integer round;

    /**
     * 是否通过
     */
    private boolean pass;
}
