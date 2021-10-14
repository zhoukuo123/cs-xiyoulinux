package com.xiyoulinux.activity.comment.insideImpl;

import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qkm
 */
@Slf4j
@DubboService
public class CommentServiceImpl implements CommentService {
    @Resource
    private ICsUserActivityCommentService ICsUserActivityCommentService;

    @Override
    public Map<String,Long> getCommentNumber(List<String> activityIdList) {
        return ICsUserActivityCommentService.getCommentNumber(activityIdList);
    }

    @Override
    public void deleteComments(String activityId) {
        ICsUserActivityCommentService.deleteComments(activityId);
    }
}
