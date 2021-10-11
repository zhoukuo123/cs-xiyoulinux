package com.xiyoulinux.activity.comment.controller;

import com.xiyoulinux.activity.comment.pojo.CsUserActivityComment;
import com.xiyoulinux.activity.comment.CsUserActivityCommentService;
import com.xiyoulinux.activity.comment.vo.CsActivityCommentVo;
import com.xiyoulinux.vo.GlobalResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qkm
 */
@RestController
@RequestMapping("/activity-comment")
public class ActivityCommentController {

    @Resource
    private CsUserActivityCommentService csUserActivityCommentService;

    @GetMapping("/{activityId}")
    public List<CsActivityCommentVo> getCommentsByActivityId(@PathVariable("activityId")
                                                                     String activityId) {
        return csUserActivityCommentService.getCommentsByActivityId(activityId);
    }

    @DeleteMapping("/{id}")
    public GlobalResponseEntity<CsUserActivityComment> deleteCommentsByActivityId(@PathVariable("id")
                                                                                          String id) {
        csUserActivityCommentService.deleteCommentsByActivityId(id);
        return new GlobalResponseEntity<>();
    }

    @PutMapping("/like/{id}")
    public GlobalResponseEntity<CsUserActivityComment> likesCommentById(@PathVariable("id")
                                                                                String id) {
        csUserActivityCommentService.likesComment(id);
        return new GlobalResponseEntity<>();
    }

    @PutMapping("/dislike/{id}")
    public GlobalResponseEntity<CsUserActivityComment> dislikeCommentById(@PathVariable("id")
                                                                                  String id) {
        csUserActivityCommentService.dislikeComment(id);
        return new GlobalResponseEntity<>();
    }

    @PostMapping("/")
    public GlobalResponseEntity<CsUserActivityComment> addComment(@RequestBody CsUserActivityComment comment) {
        csUserActivityCommentService.addComment(comment);
        return new GlobalResponseEntity<>();
    }
}
