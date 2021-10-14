package com.xiyoulinux.activity.vo;

import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户动态对象")
public class CsUserActivityVo implements Serializable {

    private static final long serialVersionUID = -7116795610479337404L;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;


    /**
     * 动态内容包含的文件url
     */
    @ApiModelProperty(value = "动态内容的文件内容")
    private List<String> activityPicturesUrl;

    /**
     * 评论的数目
     */
    @ApiModelProperty(value = "动态的评论数目")
    private Long commentNumber;

    /**
     * 动态信息
     */
    @ApiModelProperty(value = "动态信息")
    private Activity csUserActivity;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "用户动态对象")
    public static class Activity implements Serializable {

        private static final long serialVersionUID = -8099396834146181750L;
        /**
         * 主键
         */
        @ApiModelProperty(value = "主键id")
        private String id;

        /**
         * 问题or动态or讲座or任务的内容
         */
        @ApiModelProperty(value = "动态的内容")
        private String activityContent;

        /**
         * 创建时间
         */
        @ApiModelProperty(value = "动态的创建时间")
        private Date activityCreateTime;

        /**
         * 结束时间
         */
        @ApiModelProperty(value = "动态的截止时间")
        private Date activityEndTime;

        /**
         * 问题or动态or讲座or任务(0/1/2/3)
         * {@link ActivityType}
         */
        @ApiModelProperty(value = "动态的类型（发起问题/发起动态/发起讲座/发起任务）")
        private ActivityType activityType;

        /**
         * 任务/讲座/问题的状态
         * {@link ActivityStatus}
         */
        @ApiModelProperty(value = "动态状态（问题状态--未解决/已解决、讲座/任务的状态--进行中/待进行/已完成）")
        private ActivityStatus activityStatus;

        public static Activity to(CsUserActivity csUserActivity) {
            return new Activity(csUserActivity.getId(), csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),csUserActivity.getActivityEndTime(),
                    csUserActivity.getActivityType(),csUserActivity.getActivityStatus());
        }
    }

}
