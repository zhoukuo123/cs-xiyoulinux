package com.xiyoulinux.activity.inter;

import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.activity.comment.service.CommentService;
import com.xiyoulinux.file.service.GetFileService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qkm
 */
@Service
public class InterService {

    @DubboReference
    private GetFileService getFileService;

    @DubboReference
    private CommentService commentService;


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
        HashMap<String, CsUserInfo> userMap = new HashMap<>(500);
        for (CsUserInfo userInfo : userInfos) {
            userMap.put(userInfo.getUserId(), userInfo);
        }
        return userMap;
   }

   /**
     * 调用用户服务获取用户信息
     *
     * @param userId 用户id
     * @return userId-UserInfo
     */
   public CsUserInfo interCallPeople(String userId) {
       return new CsUserInfo();
   }

    /**
     * 调用文件服务获取文件信息
     *
     * @param activityIdList 动态idList
     * @return activityId-fileUrlsList
     */
    public Map<String, List<String>> interCallFile(List<String> activityIdList) {
        //调用文件服务获取文件,返回（动态id，文件集合）
        return getFileService.getFileUrlByActivityId(activityIdList);
    }

    /**
     * 调用动态评论服务获取评论数目
     *
     * @param activityIdList 动态idList
     * @return activityId-commentNumber
     */
    public Map<String, Long> interCallComment(List<String> activityIdList) {
        //调用评论服务获取评论数目
        return commentService.getCommentNumber(activityIdList);
    }
}
