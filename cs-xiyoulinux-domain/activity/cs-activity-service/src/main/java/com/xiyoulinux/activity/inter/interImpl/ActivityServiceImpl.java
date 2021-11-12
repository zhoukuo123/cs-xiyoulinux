package com.xiyoulinux.activity.inter.interImpl;

import com.xiyoulinux.activity.service.IActivityService;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qkm
 */

@DubboService
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private ICsUserActivityService iCsUserActivityService;

    @Override
    public PageActivityInfo getPageActivityByUserId(String userId,int page) {
        return iCsUserActivityService.getPageActivityByUserId(userId,page);
    }
}
