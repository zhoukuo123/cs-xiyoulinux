package com.xiyoulinux.activity.comment.inter;

import com.xiyoulinux.common.CsUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qkm
 */
@Slf4j
@Service
public class InterService {

    //用户服务加降级
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
        log.info("Get userInfos [{}] --- from user service",userIdList);
        userMap.put("1",qkm);
        return userMap;
    }

    // //用户服务加降级
    /**
     * 调用用户服务获取用户信息
     *
     * @param userId 用户id
     * @return userId-UserInfo
     */
    public CsUserInfo interCallPeople(String userId) {
        //调用用户服务获取用户信息
        log.info("Get userInfo [{}] --- from user service",userId);
        CsUserInfo qkm = new CsUserInfo("1", "qkm", "http://www.xiyoulinux.com/images/xiyoulinux.png");
        return qkm;
    }

}
