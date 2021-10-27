package com.xiyoulinux.joinadmin.stream;

import com.xiyoulinux.joinadmin.mapper.JoinInfoMapper;
import com.xiyoulinux.joinadmin.mapper.JoinInfoMapperCustom;
import com.xiyoulinux.joinadmin.mapper.JoinQueueMapper;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.JoinQueue;
import com.xiyoulinux.joinadmin.pojo.dto.BatchDecisionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;

/**
 * @author CoderZk
 */
@Slf4j
@EnableBinding(value = {
        BatchDecisionTopic.class
})
public class BatchDecisionMessageConsumer {

    @Autowired
    private JoinInfoMapper joinInfoMapper;

    @Autowired
    private JoinQueueMapper joinQueueMapper;

    @Autowired
    private JoinInfoMapperCustom joinInfoMapperCustom;

    @Autowired
    private BatchDecisionTopic producer;

    @StreamListener(BatchDecisionTopic.INPUT)
    public void consumeBatchDecisionMessage(BatchDecisionDTO payload) {

        // 修改 round 和 status 在 join_info 上
        // 修改 join_info 后, 用户即可在纳新报名页看到面试结果
        JoinInfo updateJoinInfo = new JoinInfo();

        if (payload.isPass()) {
            updateJoinInfo.setRound(payload.getRound() + 1);
            updateJoinInfo.setStatus(0);
        } else {
            updateJoinInfo.setStatus(-1);
        }

        Example example = new Example(JoinInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("uid", new Iterable() {
            @Override
            public Iterator<String> iterator() {
                List<String> uids = payload.getUids();
                return uids.iterator();
            }
        });

        joinInfoMapper.updateByExampleSelective(updateJoinInfo, example);

        // 在 join_queue 上 删除 该用户的签到记录
        Example example2 = new Example(JoinQueue.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andIn("uid", new Iterable() {
            @Override
            public Iterator<String> iterator() {
                List<String> uids = payload.getUids();
                return uids.iterator();
            }
        });

        // 删除该用户的签到记录, 防止和下次签到冲突
        joinQueueMapper.deleteByExample(example2);
    }

    // 服务降级, 自定义异常处理逻辑 - 发送延迟消息
    @ServiceActivator(inputChannel = "batch-decision-topic.batch-decision-group.errors")
    public void fallback(Message<?> message) {
        log.info("Batch decision failed");
        log.info("ready to send delayed message to delay 1 minute to re enqueue");

        producer.output().send(
                MessageBuilder.withPayload(message)
                        .setHeader("x-delay", 1000 * 60)
                        .build());
    }
}
