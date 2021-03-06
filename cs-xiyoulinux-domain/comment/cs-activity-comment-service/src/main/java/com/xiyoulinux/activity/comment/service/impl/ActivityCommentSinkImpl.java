package com.xiyoulinux.activity.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xiyoulinux.activity.comment.service.ActivityCommentSink;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.pojo.ActivityMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 监听动态消息
 *
 * @author qkm
 */
@Slf4j
@EnableBinding(ActivityCommentSink.class)
public class ActivityCommentSinkImpl {

    private final ICsUserActivityCommentService iCsUserActivityCommentService;

    public ActivityCommentSinkImpl(ICsUserActivityCommentService iCsUserActivityCommentService) {
        this.iCsUserActivityCommentService = iCsUserActivityCommentService;
    }

    /**
     * 监听动态删除消息
     */
    @StreamListener("commentInput")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void consumeActivityMessage(@Payload Object payload,
                                       @Header(AmqpHeaders.CHANNEL) Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {

        log.info("receive and consume activity message: [{}]", payload.toString());
        ActivityMessage activityMessage = JSON.parseObject(
                payload.toString(), ActivityMessage.class
        );
        try {
            iCsUserActivityCommentService.deleteComments(activityMessage.getActivityId());
            iCsUserActivityCommentService.deleteLikesByCsActivityId(activityMessage.getActivityId());
            channel.basicAck(deliveryTag, true);
            log.info("consume activity message success: activityId [{}],userId [{}]，createTime [{}]",
                    activityMessage.getActivityId()
                    , activityMessage.getUserId(),
                    activityMessage.getCreateTime());
        } catch (Exception e) {
            log.error("consume activity message: [{}] error [{}]", activityMessage, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
