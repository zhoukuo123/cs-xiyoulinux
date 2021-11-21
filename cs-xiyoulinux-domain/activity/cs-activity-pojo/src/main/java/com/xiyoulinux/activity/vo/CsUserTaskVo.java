package com.xiyoulinux.activity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiyoulinux.activity.entity.CsUserTask;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.constant.AuthCommonConstant;
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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "任务对象")
public class CsUserTaskVo implements Serializable {

    private static final long serialVersionUID = -3038062661270168095L;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;


    /**
     * 评论的数目
     */
    @ApiModelProperty(value = "评论数目")
    private Long commentNumber;

    /**
     * 任务信息
     */
    @ApiModelProperty(value = "任务信息")
    private Task csUserTask;

    /**
     * 用户是否可以修改
     */
    @ApiModelProperty(value = "用户是否可以修改")
    private Boolean isModify;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "用户任务详细信息")
    public static class Task implements Serializable {
        private static final long serialVersionUID = -5044809579079197953L;

        /**
         * 任务id
         */
        @ApiModelProperty(value = "任务id")
        private String taskId;

        /**
         * 任务的标题
         */
        @ApiModelProperty(value = "任务的标题")
        private String taskTitle;

        /**
         * 任务的内容
         */
        @ApiModelProperty(value = "任务的内容")
        private String taskContent;

        /**
         * 创建时间
         */
        @ApiModelProperty(value = "任务的创建时间", example = "2021-02-03 12:00:00")
        @JsonFormat(pattern = AuthCommonConstant.DATE_FORMAT)
        private Date taskCreateTime;

        /**
         * 任务的结束时间
         */
        @ApiModelProperty(value = "任务的截止时间", example = "2021-02-03 12:00:00")
        @JsonFormat(pattern = AuthCommonConstant.DATE_FORMAT)
        private Date taskEndTime;

        /**
         * 任务状态
         * {@link ActivityStatus}
         */
        @ApiModelProperty(value = "任务的状态--进行中/待进行/已完成", example = "进行中")
        private ActivityStatus taskStatus;

        /**
         * 任务的文件信息
         */
        @TableField(value = "task_files")
        private String taskFiles;

        public static Task to(CsUserTask csUserTask) {
            return new Task(csUserTask.getTaskId(),
                    csUserTask.getTaskTitle(),
                    csUserTask.getTaskContent(),
                    csUserTask.getTaskCreateTime(),
                    csUserTask.getTaskEndTime(),
                    csUserTask.getTaskStatus(),
                    csUserTask.getTaskFiles());
        }

    }
}
