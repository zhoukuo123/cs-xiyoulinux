package com.xiyoulinux.activity.comment.scheduled;

/**
 * @author qkm
 */

import com.xiyoulinux.activity.comment.lock.RedisLock;
import com.xiyoulinux.activity.comment.mapper.CsUserCommentMapper;
import com.xiyoulinux.utils.RedisOperator;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author shuang.kou
 */
@Component
@Slf4j
public class ScheduledTasks {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final String TASK_LOCK = "task_lock";
    private static final long LOCK_TIME = 60 * 1000;

    private final RedisLock redisLock;
    private final RedisOperator redisOperator;
    private final CsUserCommentMapper csUserCommentMapper;

    public ScheduledTasks(RedisLock redisLock, RedisOperator redisOperator, CsUserCommentMapper csUserCommentMapper) {
        this.redisLock = redisLock;
        this.redisOperator = redisOperator;
        this.csUserCommentMapper = csUserCommentMapper;
    }

    /**
     * fixedRate：固定速率执行。每2小时执行一次。
     */
    @Scheduled(fixedRate = 10 * 60 * 1_000)
    @GlobalTransactional
    public void reportCurrentTimeWithFixedRate() {
        try {
            // 获取锁
            boolean lock = redisLock.tryLock(TASK_LOCK, LOCK_TIME, TimeUnit.SECONDS);
            if (lock) {
                // 同步数据业务
                Set<String> likesCommentId = redisOperator.scan("LIKE:*");
                HashMap<String, Integer> likes = new HashMap<>();
                likesCommentId.stream().forEach(commentId -> {
                    String id = commentId.substring(5);
                    int data = Integer.valueOf(redisOperator.get(commentId));
                    if (data != 0) {
                        likes.put(id, data);
                    }
                });

                try {
                    if (!likes.isEmpty()) {
                        csUserCommentMapper.mergeLikes(likes);
                    }
                    redisOperator.delCollect(likesCommentId);
                    log.info("merge redis likes to db success key: [{}] value:[{}] time [{}]", likes.keySet(),
                            likes.values(), DATE_FORMAT.format(new Date()));
                } catch (Exception e) {
                    log.error("merge likes error [{}], key: [{}],value:[{}]", likes.keySet(),
                            likes.values(), e.getMessage());
                    throw new RuntimeException("merge likes error");
                }
            } else {
                log.info("fail to get redis lock, wait next time");
            }

        } catch (Exception ex) {
            log.warn("failure, ex:{}, {}", ex.getClass(), ex.getMessage());

        } finally {
            // 释放锁
            redisLock.releaseLock(TASK_LOCK);
        }
        log.info("Fixed Rate Task : The time is now {}", DATE_FORMAT.format(new Date()));
    }

}
