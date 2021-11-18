package com.xiyoulinux.activity.controller;


import com.xiyoulinux.activity.bo.CsUserActivityBo;
import com.xiyoulinux.activity.bo.CsUserActivityDeleteBo;
import com.xiyoulinux.activity.bo.CsUserQuestionUpdateBo;
import com.xiyoulinux.activity.bo.CsUserTaskUpdateBo;
import com.xiyoulinux.activity.vo.*;
import com.xiyoulinux.activity.service.ICsUserActivityService;
import com.xiyoulinux.activity.service.ICsUserQuestionService;
import com.xiyoulinux.activity.service.ICsUserTaskService;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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


    @PostMapping("/add")
    @ApiOperation(value = "增加动态", notes = "传入一个动态对象", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "多个文件", paramType = "formData", allowMultiple = true, required = true, dataType = "file")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户的信息", response = CsUserInfoAndIdAndFileInfo.class)
    })
    public JSONResult addActivity(@RequestPart("csUserActivityBo") CsUserActivityBo csUserActivityBo,
                                  @RequestPart(value = "files") MultipartFile[] files
    ) {
        return JSONResult.ok(iCsUserActivityService.addActivity(csUserActivityBo, files));
    }


    @ApiOperation(value = "删除动态", notes = "根据动态id删除动态", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    public JSONResult deleteActivity(@RequestBody CsUserActivityDeleteBo csUserActivityDeleteBo) {
        iCsUserActivityService.deleteActivity(csUserActivityDeleteBo);
        return JSONResult.ok("删除成功");
    }

    @ApiOperation(value = "获取动态", notes = "分页的获取动态数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "动态集合"))
    @PostMapping("/home/{userId}")
    public JSONResult getPageActivities(@PathVariable("userId") String userId,
                                        @RequestBody PageInfo pageInfo) {
        PageActivityInfo pageActivity = iCsUserActivityService.getPageActivity(pageInfo, userId);
        if (pageActivity == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波动态向你赶来...");
        }
        return JSONResult.ok(pageActivity);
    }

    @ApiOperation(value = "分页的获取未解决的问题", notes = "未解决的问题(1)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "未解决问题集合"))
    @PostMapping("/issues/unsolved/{userId}")
    public JSONResult getUnresolvedIssues(@PathVariable("userId") String userId,
                                          @RequestBody PageInfo pageInfo) {
        PageQuestionInfo pageUnresolvedIssues = iCsUserQuestionService.getPageUnresolvedIssues(pageInfo, userId);
        if (pageUnresolvedIssues == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波问题向你赶来...");
        }
        return JSONResult.ok(pageUnresolvedIssues);
    }


    @ApiOperation(value = "分页的获取已解决的问题", notes = "已解决的问题(0)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已解决问题集合"))
    @PostMapping("/issues/solved/{userId}")
    public JSONResult getResolvedIssues(@PathVariable("userId") String userId,
                                        @RequestBody PageInfo pageInfo) {
        PageQuestionInfo pageResolvedIssues = iCsUserQuestionService.getPageResolvedIssues(pageInfo, userId);
        if (pageResolvedIssues == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波问题向你赶来...");
        }
        return JSONResult.ok(pageResolvedIssues);
    }


    @ApiOperation(value = "分页的获取进行中的任务", notes = "进行中的任务(2)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "进行中的任务集合"))
    @PostMapping("/tasks/doing/{userId}")
    public JSONResult getDoingTasks(@PathVariable("userId") String userId,
                                    @RequestBody PageInfo pageInfo) {
        PageTaskInfo pageDoingTasks = iCsUserTaskService.getPageDoingTasks(pageInfo, userId);
        if (pageDoingTasks == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波任务向你赶来...");
        }
        return JSONResult.ok(pageDoingTasks);
    }

    @ApiOperation(value = "分页的获取已完成的任务", notes = "已完成的任务(4)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "已完成的任务集合"))
    @PostMapping("/tasks/did/{userId}")
    public JSONResult getDidTasks(@PathVariable("userId") String userId,
                                  @RequestBody PageInfo pageInfo) {
        PageTaskInfo pageDidTasks = iCsUserTaskService.getPageDidTasks(pageInfo, userId);
        if (pageDidTasks == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波任务向你赶来...");
        }
        return JSONResult.ok(pageDidTasks);
    }

    @ApiOperation(value = "分页的获取待完成的任务", notes = "待完成的任务(3)", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "待进行的任务集合"))
    @PostMapping("/tasks/future/{userId}")
    public JSONResult getFutureTasks(@PathVariable("userId") String userId,
                                     @RequestBody PageInfo pageInfo) {
        PageTaskInfo pageFutureTasks = iCsUserTaskService.getPageFutureTasks(pageInfo, userId);
        if (pageFutureTasks == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波任务向你赶来...");
        }
        return JSONResult.ok(pageFutureTasks);
    }

    @ApiOperation(value = "修改任务", notes = "修改任务对象的内容", httpMethod = "PUT")
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/modify/task")
    public JSONResult updateTask(@RequestBody CsUserTaskUpdateBo csUserTaskUpdateBo) {
        iCsUserTaskService.updateTasks(csUserTaskUpdateBo);
        return JSONResult.ok("更新成功");
    }

    @ApiOperation(value = "修改问题", notes = "修改问题的状态", httpMethod = "PUT")
    @ApiResponses(@ApiResponse(code = 200, message = "更新成功"))
    @PutMapping("/modify/question")
    public JSONResult updateQuestion(@RequestBody CsUserQuestionUpdateBo csUserQuestionUpdateBo) {
        iCsUserQuestionService.updateQuestionStatus(csUserQuestionUpdateBo);
        return JSONResult.ok("更新成功");
    }

    @ApiOperation(value = "获取任务个数", notes = "三种状态的任务", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "成功", response = TaskNumber.class))
    @GetMapping("/number/task")
    public JSONResult getTaskNumber() {
        return JSONResult.ok(iCsUserTaskService.getTaskNumber());
    }


    @ApiOperation(value = "获取问题个数", notes = "二种状态的问题", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "成功", response = QuestionNumber.class))
    @GetMapping("/number/issues")
    public JSONResult getQuestionNumber() {
        return JSONResult.ok(iCsUserQuestionService.getQuestionNumber());
    }
}
