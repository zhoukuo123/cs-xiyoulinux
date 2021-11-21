package com.xiyoulinux.activity.inter;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.xiyoulinux.activity.comment.service.ICommentService;
import com.xiyoulinux.common.CsUserInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qkm
 */
@Service
public class IntelService {

    @DubboReference
    private ICommentService iCommentService;


    //用户服务增加降级

    /**
     * 调用用户服务获取用户信息
     *
     * @param userIdList 用户idList
     * @return userId-UserInfo
     */
    public Map<String, CsUserInfo> interCallPeopleList(Set<String> userIdList) {

        //使用userIdList;
        ArrayList<CsUserInfo> userInfos = new ArrayList<>();
        //组内人先定500人
        HashMap<String, CsUserInfo> userMap = new HashMap<>(16);
        CsUserInfo qkm = new CsUserInfo("1", "qkm", "http://www.xiyoulinux.com/images/xiyoulinux.png");
//        for (CsUserInfo userInfo : userInfos) {
//            userMap.put(userInfo.getUserId(), userInfo);
//        }
        userMap.put("1", qkm);
        return userMap;
    }

    //用户服务增加降级

    /**
     * 调用用户服务获取用户信息
     *
     * @param userId 用户id
     * @return userId-UserInfo
     */
    @HystrixCommand()
    public CsUserInfo interCallPeople(String userId) {
        return new CsUserInfo("1","qwe","https://t7.baidu.com/it/u=1092574912,855301095&fm=193&f=GIF");
    }


    /**
     * 调用动态评论服务获取评论数目
     *
     * @param activityIdList 动态idList
     * @return activityId-commentNumber
     */
    public Map<String, Long> interCallComment(List<String> activityIdList) {
        //调用评论服务获取评论数目
        return iCommentService.getCommentNumber(activityIdList);
    }
}
