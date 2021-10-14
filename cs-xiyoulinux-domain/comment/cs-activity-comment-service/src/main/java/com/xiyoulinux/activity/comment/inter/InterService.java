package com.xiyoulinux.activity.comment.inter;

import com.xiyoulinux.activity.comment.service.CommentService;
import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.file.service.GetFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qkm
 */
@Slf4j
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
        HashMap<String, CsUserInfo> userMap = new HashMap<>(16);
        for (CsUserInfo userInfo : userInfos) {
            userMap.put(userInfo.getUserId(), userInfo);
        }
        log.info("Get userInfos [{}] --- from user service",userIdList);
        return userMap;
    }

    /**
     * 调用用户服务获取用户信息
     *
     * @param userId 用户id
     * @return userId-UserInfo
     */
    public CsUserInfo interCallPeople(String userId) {
        //调用用户服务获取用户信息
        log.info("Get userInfo [{}] --- from user service",userId);
        return new CsUserInfo();
    }

    /**
     * 调用文件服务获取评论的文件信息
     *
     * @param commentIdList 动态idList
     * @return commentId-fileUrlsList
     */
    public Map<String, List<String>> interCallFile(List<String> commentIdList) {
        //调用文件服务获取文件,返回（动态id，文件集合）
        return getFileService.getFileUrlByCommentId(commentIdList);
    }
}
