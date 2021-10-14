package com.xiyoulinux.activity.comment.controller;

import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.pojo.CsUserActivityComment;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.vo.CsUserActivityCommentVo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.bo.CsUserInfoAndId;
import com.xiyoulinux.vo.GlobalResponseEntity;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value = "获取所有的评论", notes = "根据动态id")
    @ApiResponses(@ApiResponse(code = 200, message = "评论集合", response = CsUserActivityCommentVo.class))
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "activityId", value = "动态id")
    })
    @GetMapping("/{activityId}/{page}")
    public PageCommentInfo getCommentsByActivityId(@PathVariable("activityId")
                                                                         String activityId,
                                                   @PathVariable("page") int page) {
        return iCsUserActivityCommentService.getPageCommentsByActivityId(activityId,page);
    }


    @ApiOperation(value = "给评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "评论id")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "点赞成功")
    })
    @PutMapping("/like/{id}")
    public GlobalResponseEntity likesCommentById(@PathVariable("id")
                                                                                String id) {
        iCsUserActivityCommentService.likesComment(id);
        return new GlobalResponseEntity<>();
    }


    @ApiOperation(value = "给评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "评论id")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "取消点赞"))
    @PutMapping("/dislike/{id}")
    public GlobalResponseEntity dislikeCommentById(@PathVariable("id")
                                                                                  String id) {
        iCsUserActivityCommentService.dislikeComment(id);
        return new GlobalResponseEntity<>();
    }


    @ApiOperation(value = "增加评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "comment", value = "评论对象"),
            @ApiImplicitParam(paramType = "data", name = "files", value = "评论内容中的文件信息")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "用户信息", response = CsUserInfoAndId.class))
    @PostMapping("/")
    public CsUserInfoAndId addComment(@RequestBody CsUserActivityCommentBo comment
 //           , @RequestPart("files") MultipartFile[] files
    ) {
        return iCsUserActivityCommentService.addComment(comment, null);

    }
}
