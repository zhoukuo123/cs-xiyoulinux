package com.xiyoulinux.join.stream;

import com.xiyoulinux.join.pojo.dto.UserSignUpMsgDTO;
import com.xiyoulinux.joinadmin.pojo.dto.UserJoinDTO;
import com.xiyoulinux.joinadmin.service.SignUpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author CoderZk
 */
@Slf4j
@EnableBinding(value = {
        UserSignUpTopic.class
})
public class UserSignUpMessageConsumer {

    @DubboReference
    private SignUpService signUpService;

    @Autowired
    private UserSignUpTopic producer;

    @StreamListener(UserSignUpTopic.INPUT)
    public void consumeUserSignUpMessage(UserSignUpMsgDTO payload) {
        // RPC 调用保存用户报名信息到数据库
        UserJoinDTO userJoinDTO = new UserJoinDTO();
        BeanUtils.copyProperties(payload, userJoinDTO);
        signUpService.createUserJoinInfo(payload.getUid(), userJoinDTO);
    }

    // 服务降级, 自定义异常处理逻辑 - 发送延迟消息
    @ServiceActivator(inputChannel = "user-signup-topic.user-signup-group.errors")
    public void fallback(Message<?> message) {
        log.info("user signup failed");
        log.info("ready to send delayed message to delay 1 minute to re enqueue");

        producer.output().send(
                MessageBuilder.withPayload(message)
                        .setHeader("x-delay", 1000 * 60)
                        .build());
    }
}
