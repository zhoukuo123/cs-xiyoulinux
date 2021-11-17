package com.xiyoulinux.activity.comment.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author qkm
 */
public interface RedisLock{
    /**
     * 加锁
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryLock(String key, long timeout, TimeUnit unit);

    /**
     * 解锁
     * @param key
     */
    void releaseLock(String key);
}