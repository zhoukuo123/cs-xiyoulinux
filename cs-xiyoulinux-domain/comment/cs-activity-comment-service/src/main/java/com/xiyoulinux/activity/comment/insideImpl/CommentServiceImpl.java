package com.xiyoulinux.activity.comment.insideImpl;

import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qkm
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final ICsUserActivityCommentService iCsUserActivityCommentService;

    public CommentServiceImpl(ICsUserActivityCommentService iCsUserActivityCommentService) {
        this.iCsUserActivityCommentService = iCsUserActivityCommentService;
    }

    @Override
    public Map<String, Long> getCommentNumber(List<String> activityIdList) {
        return iCsUserActivityCommentService.getCommentNumber(activityIdList);
    }

    @Override
    public void deleteComments(String activityId) {
        iCsUserActivityCommentService.deleteComments(activityId);
    }


}
