package com.xiyoulinux.activity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiyoulinux.activity.entity.CsUserActivity;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.constant.AuthCommonConstant;
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
     * 用户是否可以修改
     */
    @ApiModelProperty(value = "用户是否可以修改，只有问题和任务可以修改，普通动态不能修改，前端需要做一下判断")
    private Boolean isModify;

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
         * 动态的标题
         */
        @ApiModelProperty(value = "动态的标题")
        private String activityTitle;

        /**
         * 问题or动态or讲座or任务的内容
         */
        @ApiModelProperty(value = "动态的内容")
        private String activityContent;

        /**
         * 创建时间
         */
        @ApiModelProperty(value = "动态的创建时间", example = "2021-02-03 18:00:00")
        @JsonFormat(pattern = AuthCommonConstant.DATE_FORMAT)
        private Date activityCreateTime;

        /**
         * 结束时间
         */
        @ApiModelProperty(value = "动态的截止时间", example = "2021-02-03 18:00:00")
        @JsonFormat(pattern = AuthCommonConstant.DATE_FORMAT)
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

        /**
         * 动态的文件信息
         */
        @TableField(value = "activity_files")
        private List<String> activityFiles;

        public static Activity to(CsUserActivity csUserActivity) {
            return new Activity(csUserActivity.getId(),
                    csUserActivity.getActivityTitle(),
                    csUserActivity.getActivityContent(),
                    csUserActivity.getActivityCreateTime(),
                    csUserActivity.getActivityEndTime(),
                    csUserActivity.getActivityType(),
                    csUserActivity.getActivityStatus(),
                    csUserActivity.getActivityFiles());
        }
    }

}
