package com.xiyoulinux.activity.controller;


import com.xiyoulinux.activity.CsUserActivityService;
import com.xiyoulinux.activity.pojo.CsUserActivity;
import com.xiyoulinux.activity.vo.CsActivityVo;
import com.xiyoulinux.vo.GlobalResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 动态中心业务
 *
 * @author qkm
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private CsUserActivityService csUserActivityService;

    @PostMapping("/")
    public GlobalResponseEntity<CsActivityVo> addActivity(@RequestBody CsUserActivity csUserActivity) {
        csUserActivityService.addActivity(csUserActivity);
        return new GlobalResponseEntity<>();
    }

    @DeleteMapping("/{id}")
    public GlobalResponseEntity<CsUserActivity> deleteActivity(@PathVariable("id") String id) {
        csUserActivityService.deleteActivity(id);
        return new GlobalResponseEntity<>();
    }

    @GetMapping("/current")
    public List<CsActivityVo> getCurrentActivities() {
        return csUserActivityService.getCurrentActivity();
    }

    /**
     * 懒加载需要传参数吗？
     */
    @GetMapping("/old/")
    public List<CsActivityVo> getOldActivities() {
        return csUserActivityService.getOldActivity();
    }

    @GetMapping("/issues")
    public List<CsActivityVo> getUnresolvedIssues() {
        return csUserActivityService.getUnresolvedIssues();
    }

    @GetMapping("/solved")
    public List<CsActivityVo> getResolvedIssues() {
        return csUserActivityService.getResolvedIssues();
    }

    @GetMapping("/tasks")
    public List<CsActivityVo> getTasks() {
        return csUserActivityService.getTasks();
    }
}
