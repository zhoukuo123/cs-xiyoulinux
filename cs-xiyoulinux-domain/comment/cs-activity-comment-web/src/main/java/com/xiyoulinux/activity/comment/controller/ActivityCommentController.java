package com.xiyoulinux.activity.comment.controller;

import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.bo.CsUserLikesBo;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.vo.CsUserActivityCommentVo;
import com.xiyoulinux.activity.comment.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author qkm
 */
@RestController
@Api(tags = "动态评论中心")
@RequestMapping("/activity-comment")
@SuppressWarnings("all")
public class ActivityCommentController {

    @Resource
    private ICsUserActivityCommentService iCsUserActivityCommentService;

    @ApiOperation(value = "获取评论", notes = "根据动态id ")
    @ApiResponses(@ApiResponse(code = 200, message = "评论集合", response = CsUserActivityCommentVo.class))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageInfo", value = "页对象", required = true, paramType = "body"),
            @ApiImplicitParam(paramType = "path", name = "activityId", value = "动态id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/get/{activityId}/{userId}")
    public JSONResult getCommentsByActivityId(@RequestBody PageInfo pageInfo,
                                              @PathVariable("activityId") String activityId,
                                              @PathVariable("userId") String userId) {

        PageCommentInfo pageCommentsByActivityIdAndUserId = iCsUserActivityCommentService.
                getPageCommentsByActivityIdAndUserId(pageInfo, activityId, userId);
        if (pageCommentsByActivityIdAndUserId == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波评论正在赶来...");
        }
        return JSONResult.ok(pageCommentsByActivityIdAndUserId);
    }

    @ApiOperation(value = "获取评论orderByLikes", notes = "根据动态id获取根据likes降序的评论 ")
    @ApiResponses(@ApiResponse(code = 200, message = "评论集合", response = CsUserActivityCommentVo.class))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageInfo", value = "页对象", required = true, paramType = "body"),
            @ApiImplicitParam(paramType = "path", name = "activityId", value = "动态id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/orderbyLikes/{activityId}/{userId}")
    public JSONResult getPageCommentsByActivityIdAndUserIdOrderByLikes(@RequestBody PageInfo pageInfo,
                                                                       @PathVariable("activityId") String activityId,
                                                                       @PathVariable("userId") String userId) {
        PageCommentInfo pageCommentsByActivityIdAndUserId = iCsUserActivityCommentService.
                getPageCommentsByActivityIdAndUserIdOrderByLikes(pageInfo, activityId, userId);
        if (pageCommentsByActivityIdAndUserId == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code, "一大波评论正在赶来...");
        }
        return JSONResult.ok(pageCommentsByActivityIdAndUserId);
    }

    @ApiOperation(value = "给评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "csUserLikesBo", value = "点赞对象", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "点赞成功")
    })
    @PutMapping("/like")
    public JSONResult likesCommentById(@RequestBody CsUserLikesBo csUserLikesBo) {
        return JSONResult.ok(iCsUserActivityCommentService.likesComment(csUserLikesBo));
    }

    @ApiOperation(value = "给评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "评论id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "取消点赞"))
    @PutMapping("/dislike/{commentId}/{userId}")
    public JSONResult dislikeCommentById(@PathVariable("commentId") String commentId
            , @PathVariable("userId") String userId
    ) {
        return JSONResult.ok(iCsUserActivityCommentService.dislikeComment(commentId, userId));
    }

    @ApiOperation(value = "增加评论")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "comment", value = "评论对象", required = true)
//            @ApiImplicitParam(paramType = "data", name = "files", value = "评论内容中的文件信息")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "用户信息", response = CsUserInfoAndIdAndFileInfo.class))
    @PostMapping("/add")
    public JSONResult addComment(@RequestBody CsUserActivityCommentBo comment
                                 //           , @RequestPart("files") MultipartFile[] files
    ) {
        return JSONResult.ok(iCsUserActivityCommentService.addComment(comment, null));

    }
}
