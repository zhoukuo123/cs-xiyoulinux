package com.xiyoulinux.controller;

import com.xiyoulinux.entity.CsUserActivity;
import com.xiyoulinux.service.CsUserActivityService;
import com.xiyoulinux.vo.GlobalResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 动态中心业务
 * @author qkm
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private CsUserActivityService csUserActivityService;

    @PostMapping("/")
    public GlobalResponseEntity<CsUserActivity> addDynamic(@RequestBody CsUserActivity csUserActivity) {
        csUserActivityService.addDynamic(csUserActivity);
        return new GlobalResponseEntity<>();
    }

    @DeleteMapping("/{id}")
    public GlobalResponseEntity<CsUserActivity> deleteDynamic(@PathVariable("id") String id) {
        csUserActivityService.deleteDynamic(id);
        return new GlobalResponseEntity<>();
    }

    @GetMapping("/all")
    public List<CsUserActivity> getOneWeekDynamics() {
        return csUserActivityService.getOneWeekDynamics();
    }

    @GetMapping("/{userId}")
    public List<CsUserActivity> getAllDynamics(@PathVariable("userId") String userId) {
        return csUserActivityService.getDynamicsByUserId(userId);
    }
}
