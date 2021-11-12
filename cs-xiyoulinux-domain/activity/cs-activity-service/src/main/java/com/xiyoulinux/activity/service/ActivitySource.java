package com.xiyoulinux.activity.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author qkm
 * 自定义动态消息通信信道(Source)
 */
public interface ActivitySource {

    /**
     * 输出信道名称
     */
    String OUTPUT = "activityOutput";

    /**
     * 对应yml文件里面的 输出信道
     * @return 发送消息的管道
     */
    @Output(ActivitySource.OUTPUT)
    MessageChannel activityOutput();

    /**
     * 发送成功监听的通道
     * @return
     */
    @Input("testAck")
    MessageChannel testAck();
}
