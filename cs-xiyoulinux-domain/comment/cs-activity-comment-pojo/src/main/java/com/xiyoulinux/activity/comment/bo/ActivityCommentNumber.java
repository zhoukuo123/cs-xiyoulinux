package com.xiyoulinux.activity.comment.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
public class ActivityCommentNumber {
    /**
     * 动态id
     */
    private String activityId;

    /**
     * 评论数目
     */
    private Long commentNumbers;
}
