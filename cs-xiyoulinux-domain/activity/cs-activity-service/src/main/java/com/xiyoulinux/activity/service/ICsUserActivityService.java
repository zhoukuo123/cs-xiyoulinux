package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.bo.CsUserInfoAndId;
import com.xiyoulinux.enums.ActivityType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author qkm
 */
public interface ICsUserActivityService {

    /**
     * 添加动态
     *
     * @param files            文件
     * @param csUserActivityBo object
     * @return 用户信息
     */
    CsUserInfoAndId addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files);

    /**
     * 根据动态id删除动态
     *
     * @param id   id
     * @param type 动态的类型
     */
    void deleteActivity(String id, ActivityType type);

    /**
     * 分页获取动态
     *
     * @param page 第几页
     * @return 动态集合
     */
    PageActivityInfo getPageActivity(int page);



    /**
     * 根据userId获取所有动态
     *
     * @param userId 用户id
     * @param page 分页
     * @return 该用户发表的动态集合
     */
    PageActivityInfo getPageActivityByUserId(String userId, int page);

}
