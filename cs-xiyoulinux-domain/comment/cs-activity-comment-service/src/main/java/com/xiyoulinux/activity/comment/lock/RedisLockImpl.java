package com.xiyoulinux.activity.comment.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author qkm
 */
@Component
public class RedisLockImpl implements RedisLock {
    private final StringRedisTemplate stringRedisTemplate;

    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public RedisLockImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        String uuid = UUID.randomUUID().toString();
        threadLocal.set(uuid);
        boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent(key, uuid, timeout, unit);
        if (isLock) {
            new Thread(new UpdateLockTimeoutTask(uuid, stringRedisTemplate, key)).start();
        }
        return isLock;
    }

    @Override
    public void releaseLock(String key) {
        //当前线程中绑定的uuid与Redis中的uuid相同时，再执行删除锁的操作
        String uuid = stringRedisTemplate.opsForValue().get(key);
        if (threadLocal.get().equals(uuid)) {
            threadLocal.remove();
            stringRedisTemplate.delete(key);
            long threadId = Long.valueOf(stringRedisTemplate.opsForValue().get(uuid));
            Thread updateLockTimeoutThread = ThreadUtils.getThreadByThreadId(threadId);
            if (updateLockTimeoutThread != null) {
                //中断更新锁超时时间的线程
                updateLockTimeoutThread.interrupt();
                stringRedisTemplate.delete(uuid);
            }
        }
    }
}

