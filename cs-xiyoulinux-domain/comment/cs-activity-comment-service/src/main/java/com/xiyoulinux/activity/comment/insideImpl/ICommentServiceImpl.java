package com.xiyoulinux.activity.comment.insideImpl;

import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

/**
 * @author qkm
 */
@Slf4j
@DubboService
public class ICommentServiceImpl implements ICommentService {

    private final ICsUserActivityCommentService iCsUserActivityCommentService;

    public ICommentServiceImpl(ICsUserActivityCommentService iCsUserActivityCommentService) {
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
