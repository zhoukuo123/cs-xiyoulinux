package com.xiyoulinux.activity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xiyoulinux.activity.pojo.CsUserQuestion;
import com.xiyoulinux.bo.CsUserInfo;
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
@ApiModel(description = "问题对象")
public class CsUserQuestionVo implements Serializable {

    private static final long serialVersionUID = 2483464510256794028L;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;


    /**
     * 动态内容包含的文件url
     */
    @ApiModelProperty(value = "问题内容所包含的文件信息")
    private List<String> questionPicturesUrl;

    /**
     * 评论的数目
     */
    @ApiModelProperty(value = "评论数目")
    private Long commentNumber;

    /**
     * 问题信息
     */
    @ApiModelProperty(value = "问题信息")
    private Question csUserQuestion;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "用户问题详细信息")
    public static class Question implements Serializable {

        private static final long serialVersionUID = 7858901002535373410L;
        /**
         * 问题id
         */
        @TableId(value = "question_id")
        private String questionId;

        /**
         * 问题的内容
         */
        @TableField(value = "question_content")
        private String questionContent;

        /**
         * 创建时间
         */
        @TableField(value = "question_create_time")
        private Date questionCreateTime;

        /**
         * 问题的状态
         * {@link ActivityStatus}
         */
        @ApiModelProperty(value = "问题状态--未解决/已解决")
        private ActivityStatus questionStatus;

        public static Question to(CsUserQuestion csUserQuestion) {
            return new Question(csUserQuestion.getQuestionId(),
                    csUserQuestion.getQuestionContent(),
                    csUserQuestion.getQuestionCreateTime(),
                    csUserQuestion.getQuestionStatus());
        }
    }

}
