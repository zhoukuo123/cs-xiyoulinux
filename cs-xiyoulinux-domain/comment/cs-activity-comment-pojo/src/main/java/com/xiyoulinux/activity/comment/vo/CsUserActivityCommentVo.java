package com.xiyoulinux.activity.comment.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiyoulinux.activity.comment.pojo.CsUserActivityComment;
import com.xiyoulinux.bo.CsUserInfo;
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
     * 评论内容包含的文件url
     */
    @ApiModelProperty(value = "评论内容包含的文件url")
    List<String> activityPicturesUrl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "评论的详细信息")
    public static class ActivityComment implements Serializable {
        private static final long serialVersionUID = -1351309525049849086L;
        /**
         * 主键
         */
        @TableId(value = "id")
        @ApiModelProperty(value = "主键id")
        private String id;

        /**
         * 评论内容
         */
        @TableId(value = "comment_content")
        @ApiModelProperty(value = "评论的内容")
        private String commentContent;

        /**
         * 评论点赞数目
         */
        @TableId(value = "comment_likes")
        @ApiModelProperty(value = "评论的点赞数目")
        private int commentLikes;

        /**
         * 评论创建时间
         */
        @TableId(value = "comment_create_time")
        @ApiModelProperty(value = "评论的创建时间")
        private Date commentCreateTime;

        public static ActivityComment to(CsUserActivityComment comment) {
            return new ActivityComment(comment.getId(),
                    comment.getCommentContent(), comment.getCommentLikes()
                    , comment.getCommentCreateTime());
        }
    }

}
