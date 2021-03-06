package com.xiyoulinux.joinadmin.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CoderZk
 */
@ApiModel(value = "纳新设置DTO", description = "纳新设置DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinSettingDTO implements Serializable {

    /**
     * 纳新报名开始时间
     */
    private Date joinStartTime;

    /**
     * 纳新报名截止时间
     */
    private Date joinEndTime;

    /**
     * 本次纳新有多少轮
     */
    private Integer round;
}
