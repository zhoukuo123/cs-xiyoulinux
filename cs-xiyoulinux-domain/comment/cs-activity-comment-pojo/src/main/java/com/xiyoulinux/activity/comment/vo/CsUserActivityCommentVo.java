package com.xiyoulinux.activity.comment.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiyoulinux.activity.comment.entity.CsUserActivityComment;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.constant.AuthCommonConstant;
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "动态评论的内容")
public class CsUserActivityCommentVo implements Serializable {
    private static final long serialVersionUID = 7999263881655737513L;
    /**
     * 评论信息
     */
    @ApiModelProperty(value = "评论的详细信息")
    private ActivityComment activityComment;

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private CsUserInfo csUserInfo;

    /**
     * 用户是否点赞
     */
    @ApiModelProperty(value = "用户是否点赞")
    private boolean isLike;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "评论的详细信息")
    public static class ActivityComment implements Serializable {
        private static final long serialVersionUID = -1351309525049849086L;
        /**
         * 主键
         */
        @ApiModelProperty(value = "主键id")
        private String id;

        /**
         * 评论内容
         */
        @ApiModelProperty(value = "评论的内容")
        private String commentContent;

        /**
         * 评论点赞数目
         */
        @ApiModelProperty(value = "评论的点赞数目")
        private int commentLikes;

        /**
         * 评论创建时间
         */
        @ApiModelProperty(value = "评论的创建时间", example = "2021-02-03 12:00:00")
        @JsonFormat(pattern = AuthCommonConstant.DATE_FORMAT)
        private Date commentCreateTime;

        /**
         * 评论对应的文件信息
         */
        @TableField(value = "comment_files")
        private List<String> commentFiles;

        public static ActivityComment to(CsUserActivityComment comment) {
            return new ActivityComment(comment.getId(),
                    comment.getCommentContent(), comment.getCommentLikes()
                    , comment.getCommentCreateTime(),
                    comment.getCommentFiles());
        }
    }

}
