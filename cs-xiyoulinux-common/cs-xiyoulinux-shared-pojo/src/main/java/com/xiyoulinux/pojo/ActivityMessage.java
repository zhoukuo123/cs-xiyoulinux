package com.xiyoulinux.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
public class ActivityMessage {
    /**
     * 动态id
     */
    private String activityId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 消息创建时间
     */
    private String createTime;
}
