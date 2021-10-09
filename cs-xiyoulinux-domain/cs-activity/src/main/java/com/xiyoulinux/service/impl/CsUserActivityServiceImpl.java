package com.xiyoulinux.service.impl;

import com.xiyoulinux.entity.CsUserActivity;
import com.xiyoulinux.mapper.CsUserActivityMapper;
import com.xiyoulinux.service.CsUserActivityService;
import com.xiyoulinux.utils.DistributedIDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qkm
 */
@Service
public class CsUserActivityServiceImpl implements CsUserActivityService {

    @Resource
    private CsUserActivityMapper csUserActivityMapper;

    @Override
    public void addDynamic(CsUserActivity csUserActivity) {
        csUserActivity.setId(String.valueOf(DistributedIDUtils.nextId()));
        csUserActivityMapper.insert(csUserActivity);
    }

    @Override
    public void deleteDynamic(String id) {
        csUserActivityMapper.deleteById(id);
    }

    @Override
    public List<CsUserActivity> getOneWeekDynamics() {
        return csUserActivityMapper.selectOneWeek();
    }

    @Override
    public List<CsUserActivity> getDynamicsByUserId(String userId) {
        return (List<CsUserActivity>) csUserActivityMapper.selectByUserId(userId);
    }
}




