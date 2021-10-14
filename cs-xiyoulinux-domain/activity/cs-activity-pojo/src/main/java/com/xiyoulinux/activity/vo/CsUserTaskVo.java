package com.xiyoulinux.activity.vo;

import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.activity.pojo.CsUserTask;
import com.xiyoulinux.enums.ActivityStatus;
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
@ApiModel(description = "任务对象")
public class CsUserTaskVo implements Serializable {

    private static final long serialVersionUID = -3038062661270168095L;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;


    /**
     * 动态内容包含的文件url
     */
    @ApiModelProperty(value = "任务内容所包含的文件信息")
    private List<String> taskPicturesUrl;

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
         * 任务的内容
         */
        @ApiModelProperty(value = "任务的内容")
        private String taskContent;

        /**
         * 创建时间
         */
        @ApiModelProperty(value = "任务的创建时间")
        private Date taskCreateTime;

        /**
         * 任务的结束时间
         */
        @ApiModelProperty(value = "任务的截止时间")
        private Date taskEndTime;

        /**
         * 任务状态
         * {@link ActivityStatus}
         */
        @ApiModelProperty(value = "任务的状态--进行中/待进行/已完成")
        private ActivityStatus taskStatus;

        public static Task to(CsUserTask csUserTask) {
            return new Task(csUserTask.getTaskId(),
                    csUserTask.getTaskContent(),
                    csUserTask.getTaskCreateTime(),
                    csUserTask.getTaskEndTime(),
                    csUserTask.getTaskStatus());
        }

    }
}
