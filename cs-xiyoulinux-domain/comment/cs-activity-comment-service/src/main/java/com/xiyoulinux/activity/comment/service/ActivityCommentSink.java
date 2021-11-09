package com.xiyoulinux.activity.comment.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义评论信息接收器(Sink)
 * @author qkm
 */
public interface ActivityCommentSink {

    /**
     * 输入信道名称
     */
    String INPUT = "commentInput";

    /**
     * 对应yml文件里面的输入信道
     * @return 接受消息
     */
    @Input(ActivityCommentSink.INPUT)
    SubscribableChannel commentInput();
}
