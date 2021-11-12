package com.xiyoulinux.activity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


/**
 * @author qkm
 * 有问题
 */
@Slf4j
@EnableBinding(ActivitySource.class)
public class ListenSendMessage {

    @StreamListener("testAck")
    public void acks(Message ack) {
        log.error("消息发送成功");
    }

}
