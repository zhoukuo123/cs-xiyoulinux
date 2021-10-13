package com.xiyoulinux.activity.controller;


import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.vo.*;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.service.ICsUserTaskService;
import com.xiyoulinux.common.GlobalResponseEntity;
import com.xiyoulinux.enums.ActivityStatus;
import com.xiyoulinux.enums.ActivityType;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

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
//            @ApiImplicitParam(name = "files", value = "多个文件", paramType = "formData", allowMultiple = true, required = true, dataType = "file")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户的信息", response = CsUserInfoAndIdAndFileInfo.class)
    })
    public CsUserInfoAndIdAndFileInfo addActivity(@RequestBody CsUserActivityBo csUserActivityBo
                                                  // @RequestParam(value = "files") MultipartFile[] files
    ) {
//        System.out.println(files);
        System.out.println(csUserActivityBo);
        return iCsUserActivityService.addActivity(csUserActivityBo, null);
//        return iCsUserActivityService.addActivity(csUserActivityBo, null);
    }


    @ApiOperation(value = "删除动态", notes = "根据动态id删除动态", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "动态id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "type", value = "发起问题/发起动态/发起讲座/发起任务--输入汉字就可以",
                    required = true, dataType = "Stirng", example = "发起任务"),
            @ApiImplicitParam(paramType = "path", name = "status", value = "状态--未解决/已解决--已完成/待进行/进行中",
                    required = true, dataType = "Stirng", example = "未解决")
    })
    @DeleteMapping("/{id}/{type}/{status}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    public GlobalResponseEntity deleteActivity(@PathVariable("id") String id,
                                               @PathVariable("type") String type, @PathVariable("status") String status) {
        iCsUserActivityService.deleteActivity(id, ActivityType.of(type), ActivityStatus.of(status));
        return new GlobalResponseEntity<>();
    }

    @ApiOperation(value = "获取动态", notes = "分页的获取动态数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "第几页", required = true, dataType = "int")}
    )
    @ApiResponses(@ApiResponse(code = 200, message = "动态集合"))
    @GetMapping("/home/{userId}/{page}")
    public GlobalResponseEntity getPageActivities(@PathVariable("userId") String userId,
                                                  @PathVariable("page") int page) {
        PageActivityInfo pageActivity = iCsUserActivityService.getPageActivity(page, userId);
        if (pageActivity == null) {
            return new GlobalResponseEntity(501, "一大波动态向你赶来...");
        }
        return new GlobalResponseEntity(pageActivity);
    }

    @ApiOperation(value = "分页的获取未解决的问题", notes = "未解决的问题(1)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "path", dataType = "int")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "未解决问题集合"))
    @GetMapping("/issues/unsolved/{userId}/{page}")
    public GlobalResponseEntity getUnresolvedIssues(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageQuestionInfo pageUnresolvedIssues = iCsUserQuestionService.getPageUnresolvedIssues(page,userId);
        if (pageUnresolvedIssues == null) {
            return new GlobalResponseEntity(501, "一大波问题向你赶来...");
        }
        return new GlobalResponseEntity(pageUnresolvedIssues);
    }


    @ApiOperation(value = "分页的获取已解决的问题", notes = "已解决的问题(0)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "path", dataType = "int")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已解决问题集合"))
    @GetMapping("/issues/solved/{userId}/{page}")
    public GlobalResponseEntity getResolvedIssues(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageQuestionInfo pageResolvedIssues = iCsUserQuestionService.getPageResolvedIssues(page,userId);
        if (pageResolvedIssues == null) {
            return new GlobalResponseEntity(501, "一大波问题向你赶来...");
        }
        return new GlobalResponseEntity(pageResolvedIssues);
    }


    @ApiOperation(value = "分页的获取进行中的任务", notes = "进行中的任务(2)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "path", dataType = "int")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "进行中的任务集合"))
    @GetMapping("/doing/tasks/{userId}/{page}")
    public GlobalResponseEntity getDoingTasks(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageTaskInfo pageDoingTasks = iCsUserTaskService.getPageDoingTasks(page,userId);
        if (pageDoingTasks == null) {
            return new GlobalResponseEntity(501, "一大波任务向你赶来...");
        }
        return new GlobalResponseEntity(pageDoingTasks);
    }

    @ApiOperation(value = "分页的获取已完成的任务", notes = "已完成的任务(4)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已完成的任务集合"))
    @GetMapping("/did/tasks/{userId}/{page}")
    public GlobalResponseEntity getDidTasks(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageTaskInfo pageDidTasks = iCsUserTaskService.getPageDidTasks(page,userId);
        if (pageDidTasks == null) {
            return new GlobalResponseEntity(501, "一大波任务向你赶来...");
        }
        return new GlobalResponseEntity(pageDidTasks);
    }

    @ApiOperation(value = "分页的获取待完成的任务", notes = "待完成的任务(3)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "path", dataType = "int")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "待进行的任务集合"))
    @GetMapping("/future/tasks/{userId}/{page}")
    public GlobalResponseEntity getFutureTasks(@PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageTaskInfo pageFutureTasks = iCsUserTaskService.getPageFutureTasks(page,userId);
        if (pageFutureTasks == null) {
            return new GlobalResponseEntity(501, "一大波任务向你赶来...");
        }
        return new GlobalResponseEntity(pageFutureTasks);
    }

    @ApiOperation(value = "分页的获取用户动态", notes = "根据用户id获取", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "path")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "用户的动态集合"))
    @GetMapping("/user/{userId}/{page}")
    public GlobalResponseEntity getUserActivity(
            @PathVariable("userId") String userId, @PathVariable("page") int page) {
        PageActivityInfo pageActivityByUserId = iCsUserActivityService.getPageActivityByUserId(userId, page);
        if (pageActivityByUserId == null) {
            return new GlobalResponseEntity(501, "一大波动态向你赶来...");
        }
        return new GlobalResponseEntity(pageActivityByUserId);
    }


    @ApiOperation(value = "修改任务", notes = "修改任务对象的内容", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "taskEndTime", value = "要更新的任务截止时间/直接用js new Date()", required = true, paramType = "path",
                    dataType = "java.sql.Date", example = "Fri Feb 10 2017 10:06:45 GMT+0800 (CST)"),
            @ApiImplicitParam(name = "taskStatus", value = "进行中/待进行/已完成--输入汉字就可以", required = true,
                    paramType = "path", dataType = "String", example = "进行中")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/revise/{id}/{taskEndTime}/{taskStatus}")
    public GlobalResponseEntity updateTask(@PathVariable("id") String id
            , @PathVariable("taskEndTime") Date taskEndTime, @PathVariable("taskStatus")
                                                   String taskStatus) {
        iCsUserTaskService.updateTasks(id, taskEndTime, ActivityStatus.of(taskStatus));
        return new GlobalResponseEntity<>("更新成功");
    }

    @ApiOperation(value = "修改问题", notes = "修改问题的状态", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "问题id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "questionStatus", value = "未解决/已解决--输入汉字就可以", required = true,
                    paramType = "path", dataType = "String", example = "未解决")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/revise/{id}/{questionStatus}")
    public GlobalResponseEntity updateQuestion(@PathVariable("id") String id
            , @PathVariable("questionStatus") String questionStatus) {
        iCsUserQuestionService.updateQuestionStatus(id, ActivityStatus.of(questionStatus));
        return new GlobalResponseEntity<>("更新成功");
    }

    @ApiOperation(value = "获取任务个数", notes = "三种状态的任务", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "成功", response = TaskNumber.class))
    @GetMapping("/number/task")
    public TaskNumber getTaskNumber() {
        return iCsUserTaskService.getTaskNumber();
    }


    @ApiOperation(value = "获取问题个数", notes = "二种状态的问题", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "成功", response = QuestionNumber.class))
    @GetMapping("/number/issues")
    public QuestionNumber getQuestionNumber() {
        return iCsUserQuestionService.getQuestionNumber();
    }
}
