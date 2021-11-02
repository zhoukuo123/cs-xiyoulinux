package com.xiyoulinux.activity.comment.controller;

import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.vo.CsUserActivityCommentVo;
import com.xiyoulinux.activity.comment.vo.CsUserInfoAndIdAndFileInfo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.common.GlobalResponseEntity;
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
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(paramType = "path", name = "activityId", value = "动态id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/{activityId}/{userId}/{page}")
    public GlobalResponseEntity getCommentsByActivityId(@PathVariable("activityId") String activityId,
                                                        @PathVariable("page") int page
            , @PathVariable("userId") String userId
    ) {

        PageCommentInfo pageCommentsByActivityIdAndUserId = iCsUserActivityCommentService.
                getPageCommentsByActivityIdAndUserId(activityId, page, userId);
        if (pageCommentsByActivityIdAndUserId == null) {
            return new GlobalResponseEntity(501, "一大波评论正在赶来...");
        }
        return new GlobalResponseEntity(pageCommentsByActivityIdAndUserId);
    }

    @ApiOperation(value = "给评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "评论id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "点赞成功")
    })
    @PutMapping("/like/{userId}/{id}")
    public GlobalResponseEntity likesCommentById(@PathVariable("id") String commentId
            , @PathVariable("userId") String userId
    ) {
        return new GlobalResponseEntity<>(iCsUserActivityCommentService.likesComment(commentId, userId));
    }

    @ApiOperation(value = "给评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "评论id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "取消点赞"))
    @PutMapping("/dislike/{userId}/{id}")
    public GlobalResponseEntity dislikeCommentById(@PathVariable("id") String commentId
            , @PathVariable("userId") String userId
    ) {
        return new GlobalResponseEntity<>(iCsUserActivityCommentService.dislikeComment(commentId, userId));
    }

    @ApiOperation(value = "增加评论")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "comment", value = "评论对象", required = true)
//            @ApiImplicitParam(paramType = "data", name = "files", value = "评论内容中的文件信息")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "用户信息", response = CsUserInfoAndIdAndFileInfo.class))
    @PostMapping("/")
    public CsUserInfoAndIdAndFileInfo addComment(@RequestBody CsUserActivityCommentBo comment
                                                 //           , @RequestPart("files") MultipartFile[] files
    ) {
        return iCsUserActivityCommentService.addComment(comment, null);

    }
}
