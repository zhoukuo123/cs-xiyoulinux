package com.xiyoulinux.activity.comment.lock;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author qkm
 */
public class UpdateLockTimeoutTask implements Runnable {
    private static final long SLEEP_TIME = 20 * 1000;
    private static final long LOCK_TIME = 60 * 1000;

    //uuid
    private String uuid;
    private StringRedisTemplate stringRedisTemplate;
    private String key;

    public UpdateLockTimeoutTask(String uuid, StringRedisTemplate stringRedisTemplate, String key) {
        this.uuid = uuid;
        this.stringRedisTemplate = stringRedisTemplate;
        this.key = key;
    }


    @Override
    public void run() {
        //以uuid为key，当前线程id为value保存到Redis中
        stringRedisTemplate.opsForValue().set(uuid, String.valueOf(Thread.currentThread().getId()));
        //定义更新锁的过期时间
        while (true) {
            Thread thread = Thread.currentThread();
            if (thread.isInterrupted()) {
                break;
            }
            stringRedisTemplate.expire(key, LOCK_TIME, TimeUnit.SECONDS);
            try {
                //每隔20秒执行一次
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                thread.interrupt();
            }
        }
    }
}


