package com.xiyoulinux.service;

import com.xiyoulinux.entity.CsUserActivity;

import java.util.List;

/**
 * @author qkm
 */
public interface CsUserActivityService {
    void addDynamic(CsUserActivity csUserActivity);

    void deleteDynamic(String id);

    List<CsUserActivity> getOneWeekDynamics();

    List<CsUserActivity> getDynamicsByUserId(String userId);

}
