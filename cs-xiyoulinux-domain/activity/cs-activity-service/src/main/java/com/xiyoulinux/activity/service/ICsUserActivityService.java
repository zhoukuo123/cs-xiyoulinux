package com.xiyoulinux.activity.service;

import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import org.springframework.web.multipart.MultipartFile;

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
//    CsUserInfoAndId addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files);
    CsUserInfoAndIdAndFileInfo addActivity(CsUserActivityBo csUserActivityBo, MultipartFile[] files);

    /**
     * 根据动态id删除动态
     *
     * @param id     id
     * @param type   动态的类型
     * @param status 动态的状态
     */
    void deleteActivity(String id, ActivityType type, ActivityStatus status);

    /**
     * 分页获取动态
     *
     * @param page   第几页
     * @param userId 用户id
     * @return 动态集合
     */
    PageActivityInfo getPageActivity(int page, String userId);


    /**
     * 根据userId获取所有动态
     *
     * @param userId 用户id
     * @param page   分页
     * @return 该用户发表的动态集合
     */
    PageActivityInfo getPageActivityByUserId(String userId, int page);

}
