package com.xiyoulinux.activity.controller;


import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.vo.*;
import com.xiyoulinux.annotation.IgnoreResponseAdvice;
import com.xiyoulinux.bo.CsUserInfoAndId;
import com.xiyoulinux.activity.mapper.CsUserActivityMapper;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.service.ICsUserTaskService;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import com.xiyoulinux.vo.GlobalResponseEntity;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 动态中心业务
 *
 * @author qkm
 */
@Api(tags = "动态数据接口")
@RestController
@RequestMapping("/activity")
@IgnoreResponseAdvice
@SuppressWarnings("all")
public class ActivityController {


    @Resource
    private ICsUserActivityService iCsUserActivityService;

    @Resource
    private ICsUserQuestionService iCsUserQuestionService;

    @Resource
    private ICsUserTaskService iCsUserTaskService;


    @PostMapping("/")
    @ApiOperation(value = "增加动态", notes = "传入一个动态对象", httpMethod = "POST")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "csUserActivityBo", value = "动态的信息", required = true)
//            @ApiImplicitParam(paramType = "form", name = "files", value = "文件数组", allowMultiple = true,
//                    dataType = "__file", required = false)
            @ApiImplicitParam(name = "files", value = "多个文件", paramType = "formData", allowMultiple = true, required = true, dataType = "file")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户的信息", response = CsUserInfoAndId.class)
    })
    public CsUserInfoAndId addActivity(@RequestBody CsUserActivityBo csUserActivityBo,
                                       @RequestParam(value = "files") MultipartFile[] files) {
        System.out.println(files);
        System.out.println(csUserActivityBo);
        return iCsUserActivityService.addActivity(csUserActivityBo, null);
    }


    @ApiOperation(value = "删除动态", notes = "根据动态id删除动态", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "动态id", required = true),
            @ApiImplicitParam(paramType = "path", name = "type", value = "问题-0/动态-1/讲座-2/任务-3", required = true)
    })
    @DeleteMapping("/{id}/{type}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    public GlobalResponseEntity deleteActivity(@PathVariable("id") String id,
                                               @PathVariable("type") String type) {
        iCsUserActivityService.deleteActivity(id, ActivityType.of(type));
        return new GlobalResponseEntity<>();
    }

    @ApiOperation(value = "获取动态", notes = "分页的获取动态数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "第几页", required = true)}
    )
    @ApiResponses(@ApiResponse(code = 200, message = "动态集合"))
    @GetMapping("/{page}")
    public PageActivityInfo getPageActivities(
            @PathVariable("page") int page) {
        return iCsUserActivityService.getPageActivity(page);
    }

    @ApiOperation(value = "分页的获取未解决的问题", notes = "未解决的问题(1)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "未解决问题集合"))
    @GetMapping("/issues/unsolved/{page}")
    public PageQuestionInfo getUnresolvedIssues(@PathVariable("page") int page) {
        return iCsUserQuestionService.getPageUnresolvedIssues(page);
    }


    @ApiOperation(value = "分页的获取已解决的问题", notes = "已解决的问题(0)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已解决问题集合"))
    @GetMapping("/issues/solved/{page}")
    public PageQuestionInfo getResolvedIssues(@PathVariable("page") int page) {
        return iCsUserQuestionService.getPageResolvedIssues(page);
    }


    @ApiOperation(value = "分页的获取进行中的任务", notes = "进行中的任务(2)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "进行中的任务集合"))
    @GetMapping("/doing/tasks/{page}")
    public PageTaskInfo getDoingTasks(@PathVariable("page") int page) {
        return iCsUserTaskService.getPageDoingTasks(page);
    }

    @ApiOperation(value = "分页的获取已完成的任务", notes = "已完成的任务(4)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已完成的任务集合"))
    @GetMapping("/did/tasks/{page}")
    public PageTaskInfo getDidTasks(@PathVariable("page") int page) {
        return iCsUserTaskService.getPageDidTasks(page);
    }

    @ApiOperation(value = "分页的获取待完成的任务", notes = "待完成的任务(3)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "待进行的任务集合"))
    @GetMapping("/future/tasks/{page}")
    public PageTaskInfo getFutureTasks(@PathVariable("page") int page) {
        return iCsUserTaskService.getPageFutureTasks(page);
    }

    /**
     * TODO 可能会改----userId是否从前端传递，因为抓包可以修改值
     */
    @ApiOperation(value = "分页的获取用户动态", notes = "根据用户id获取", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "用户的动态集合"))
    @GetMapping("/{userId}/{page}")
    public PageActivityInfo getUserActivity(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        return iCsUserActivityService.getPageActivityByUserId(userId, page);
    }

    /**
     * TODO 状态
     */
    @ApiOperation(value = "修改任务", notes = "修改任务对象的内容", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务id", required = true),
            @ApiImplicitParam(name = "taskEndTime", value = "要更新的任务截止时间", required = true),
            @ApiImplicitParam(name = "taskStatus", value = "要更新的任务状态", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/revise/{id}/{taskEndTime}/{taskStatus}")
    public GlobalResponseEntity updateTask(@PathVariable("id") String id
            , @PathVariable("taskEndTime") Date taskEndTime, @PathVariable("taskStatus")
                                                   String taskStatus) {
        iCsUserTaskService.updateTasks(id, taskEndTime, ActivityStatus.of(taskStatus));
        return new GlobalResponseEntity<>();
    }

    @ApiOperation(value = "修改问题", notes = "修改问题的状态", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "问题id", required = true),
            @ApiImplicitParam(name = "questionStatus", value = "要更新的问题状态", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/revise/{id}/{questionStatus}")
    public GlobalResponseEntity updateQuestion(@PathVariable("id") String id
            , @PathVariable("questionStatus") String questionStatus) {
        iCsUserQuestionService.updateQuestionStatus(id, ActivityStatus.of(questionStatus));
        return new GlobalResponseEntity<>();
    }
}
