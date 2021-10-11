package com.xiyoulinux.activity.impl;

import com.xiyoulinux.activity.CsUserActivityService;
import com.xiyoulinux.activity.bo.CsUserInfo;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.activity.vo.CsActivityVo;
import com.xiyoulinux.utils.DistributedIDUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author qkm
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class CsUserActivityServiceImpl implements CsUserActivityService {

    @Resource
    private CsUserActivityMapper csUserActivityMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void addActivity(CsUserActivity csUserActivity) {
        csUserActivity.setId(String.valueOf(DistributedIDUtils.nextId()));
        csUserActivityMapper.insert(csUserActivity);
        //是否需要捕获异常
        //redis
        //1.新增数据到redis中（不用删除原来缓存存在的，只是从缓存中获取然后添加之后在放回缓存），
        // 问题是多个线程都写，会造成数据不一致的问题。（两个写缓存的线程都读到相同的数据各自分别添加记录在写灰缓存就会造成数据不一致）
        //（用消息队列？串行化更新操作）

        //2.先把缓存全部删除，然后再去加数据库，加数据库结束在删除一次缓存（但是动态是频繁更新的，如果好多人都发动态基本上
        // 都是从数据库里面获取了（采用双删策略）

        //3.加锁？
    }

    @Override
    public void deleteActivity(String id) {
        //1.多个线程同时删除数据缓存的数据，导致缓存的数据不一致问题（先删除数据库，再去删除缓存）使用消息队列串行化更新操作？
        //因为删除数据是先删除数据库，在删除缓存中的部分数据，在删除缓存中的部分数据时，会造成缓存数据的错误（都先获取到相同的数据
        // 然后各自删除记录，之后在存入缓存就会造成不一致）

        //2.全部删除，然后下次在从数据里面获取（采用双删策略）

        //3.加锁？
        csUserActivityMapper.deleteById(id);
    }

    @Override
    public List<CsActivityVo> getCurrentActivity() {
        //先判断redis里面有没有，如果没有从数据库里面查，查到之后存到redis，否则直接从缓存里面获取
        List<CsUserActivity> csUserActivities = csUserActivityMapper.selectCurrentActivity();
        return getCsActivityVo(csUserActivities);
    }

    @Override
    public List<CsActivityVo> getOldActivity() {
        return null;
    }

    @Override
    public List<CsActivityVo> getActivityByUserId(String userId) {
        List<CsUserActivity> csUserActivities = csUserActivityMapper.selectByUserId(userId);
        return getCsActivityVo(csUserActivities);
    }

    @Override
    public List<CsActivityVo> getUnresolvedIssues() {
        List<CsUserActivity> unresolvedIssues = csUserActivityMapper.getUnresolvedIssues();
        return getCsActivityVo(unresolvedIssues);
    }

    @Override
    public List<CsActivityVo> getResolvedIssues() {
        List<CsUserActivity> resolvedIssues = csUserActivityMapper.getResolvedIssues();
        return getCsActivityVo(resolvedIssues);
    }

    @Override
    public List<CsActivityVo> getTasks() {
        List<CsUserActivity> tasks = csUserActivityMapper.getTasks();
        return getCsActivityVo(tasks);
    }

    /**
     * 根据用户id从用户微服务获取用户信息
     *
     * @return 用户信息集合
     */
    public static List<CsActivityVo> getCsActivityVo(List<CsUserActivity> activityList) {
        HashSet<String> idList = new HashSet<>();
        for (CsUserActivity csUserActivity : activityList) {
            idList.add(csUserActivity.getUserId());
        }
        //调用用户中心获取userInfo(传入idList);
        List<CsUserInfo> userInfos = new ArrayList<>();
        //组内人先定500人
        HashMap<String, CsUserInfo> userMap = new HashMap<>(500);
        for (CsUserInfo userInfo : userInfos) {
            userMap.put(userInfo.getUserId(), userInfo);
        }
        List<CsActivityVo> csActivityVos = new ArrayList<>();
        for (CsUserActivity csUserActivity : activityList) {
            CsActivityVo csActivityVo = new CsActivityVo();
            csActivityVo.setCsUserActivity(csUserActivity);
            csActivityVo.setUserName(userMap.get(csUserActivity.getUserId()).getUserName());
            csActivityVo.setUserPic(userMap.get(csUserActivity.getUserId()).getUserPic());
            csActivityVos.add(csActivityVo);
        }
        return csActivityVos;
    }
}




