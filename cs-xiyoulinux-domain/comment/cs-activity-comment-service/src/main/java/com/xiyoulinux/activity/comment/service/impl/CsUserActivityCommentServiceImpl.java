package com.xiyoulinux.activity.comment.service.impl;

//import com.alibaba.fescar.spring.annotation.GlobalTransactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyoulinux.activity.comment.bo.ActivityCommentNumber;
import com.xiyoulinux.activity.comment.bo.CsUserActivityCommentBo;
import com.xiyoulinux.activity.comment.inter.InterService;
import com.xiyoulinux.activity.comment.service.ICsUserActivityCommentService;
import com.xiyoulinux.activity.comment.mapper.CsUserCommentMapper;
import com.xiyoulinux.activity.comment.pojo.CsUserActivityComment;
import com.xiyoulinux.activity.comment.vo.CsUserActivityCommentVo;
import com.xiyoulinux.activity.comment.vo.PageCommentInfo;
import com.xiyoulinux.bo.CsUserInfo;
import com.xiyoulinux.bo.CsUserInfoAndId;
import com.xiyoulinux.file.service.GetFileService;
import com.xiyoulinux.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import com.xiyoulinux.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@Service
@Slf4j
public class CsUserActivityCommentServiceImpl implements ICsUserActivityCommentService {

    @Resource
    private CsUserCommentMapper csUserCommentMapper;

    @DubboReference
    private GetFileService getFileService;

    @DubboReference
    private UploadFileService uploadFileService;

    @Resource
    private Sid sid;

    @Resource
    private InterService interService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    @GlobalTransactional
    public PageCommentInfo getPageCommentsByActivityId(String activityId, int page) {
        IPage<CsUserActivityComment> csUserActivityCommentPage = new Page<>(page, 10);
        IPage<CsUserActivityComment> csUserActivityCommentIPage =
                csUserCommentMapper.selectPageByActivityId(activityId, csUserActivityCommentPage);
        log.info("Get comment --- all comments [{}] of activity [{}] has not userInfo and files",
                csUserActivityCommentIPage.getRecords(), activityId);
        PageCommentInfo pageCommentInfo = new PageCommentInfo();
        pageCommentInfo.setCommentInfos(getCsActivityCommentVo(csUserActivityCommentIPage.getRecords()));
        pageCommentInfo.setHasMore(csUserActivityCommentIPage.getPages() > page);
        return pageCommentInfo;
    }

    @Override
    public void likesComment(String id) {
        //可能会加分布式锁
        int version = csUserCommentMapper.likesComment(id);
        log.info("Update like ---- like comment [{}}]", id);
    }

    @Override
    public void dislikeComment(String id) {
        //可能会加分布式锁
        int version = csUserCommentMapper.dislikeComment(id);
        log.info("Update like ---- disLike comment [{}}]", id);

    }

    @Override
    public CsUserInfoAndId addComment(CsUserActivityCommentBo comment, MultipartFile[] files) {
        CsUserActivityComment csUserActivityComment = new CsUserActivityComment();
        BeanUtils.copyProperties(comment, csUserActivityComment);
        csUserActivityComment.setId(sid.nextShort());
        log.info("Insert comment -- get CsUserActivityComment object [{}]", csUserActivityComment);

        //插入评论
        csUserCommentMapper.insert(csUserActivityComment);
        log.info("Insert comment -- add one comment [{}] to database", comment);

        if (files != null) {
            //上传评论内容中的文件信息到文件服务
            uploadFileService.uploadCommentFile(files, csUserActivityComment.getId(),
                    csUserActivityComment.getActivityId());
        }

        CsUserInfo csUserInfo = interService.interCallPeople(csUserActivityComment.getUserId());
        CsUserInfoAndId userAndActivityId = new CsUserInfoAndId();
        userAndActivityId.setCsUserInfo(csUserInfo);
        userAndActivityId.setId(new CsUserInfoAndId.Id(csUserActivityComment.getId()));
        return userAndActivityId;
    }

    @Override
    public Map<String, Long> getCommentNumber(List<String> activityIdList) {
        List<ActivityCommentNumber> commentNumberByActivityId =
                csUserCommentMapper.getCommentNumberByActivityId(activityIdList);
        if (null == commentNumberByActivityId || commentNumberByActivityId.size() == 0) {
            log.info("Get comment number --- but activity [{}] has not comment", activityIdList);
            return null;
        }
        HashMap<String, Long> commentMap = new HashMap<>(16);
        for (ActivityCommentNumber activityCommentNumber : commentNumberByActivityId) {
            commentMap.put(activityCommentNumber.getActivityId(), activityCommentNumber.getCommentNumbers());
        }
        log.info("Get comments number --- comment number [{}] of activity [{}]", commentMap, activityIdList);
        return commentMap;
    }

    @Override
    public void deleteComments(String activityId) {
        csUserCommentMapper.deleteByActivityId(activityId);
        log.info("Delete comment --- all comments of activity [{}]", activityId);
    }

    /**
     * 根据id从微服务获取用户、文件信息
     *
     * @return 评论信息集合
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<CsUserActivityCommentVo> getCsActivityCommentVo(List<CsUserActivityComment> commentList) {
        //用户id
        Set<String> idList = commentList.stream().map(CsUserActivityComment::getUserId).collect(Collectors.toSet());
        log.info("Fill comment object --- get all userId [{}] of comments [{}]", idList, commentList);

        //评论id
        List<String> commentIdList = commentList.stream().map(CsUserActivityComment::getId).collect(Collectors.toList());
        log.info("Fill comment object --- get all commentId [{}] of comments [{}]", commentIdList, commentList);

        //调用用户服务获取用户信息
        Map<String, CsUserInfo> userMap = interService.interCallPeopleList(idList);
        log.info("Fill comment object --- get all userInfo [{}] of comment [{}]", userMap, commentList);

        //调用评论服务获取评论的文件信息信息
        Map<String, List<String>> fileUrlByActivityIdMap = interService.interCallFile(commentIdList);
        log.info("Fill comment object --- get all files [{}] of comments [{}]", fileUrlByActivityIdMap, commentList);

        //构造评论vo
        List<CsUserActivityCommentVo> csActivityVos = new ArrayList<>();
        commentList.forEach(comment -> {
            CsUserActivityCommentVo commentVo = new CsUserActivityCommentVo();
            CsUserActivityCommentVo.ActivityComment simpleComment = CsUserActivityCommentVo.ActivityComment.to(comment);
            commentVo.setActivityComment(simpleComment);
            commentVo.setCsUserInfo(userMap.get(comment.getUserId()));
            commentVo.setActivityPicturesUrl(fileUrlByActivityIdMap != null ? fileUrlByActivityIdMap.get(comment.getId()) :
                    null);
            csActivityVos.add(commentVo);
        });
        log.info("Fill comment object over --- comment objects that populate attributes [{}]", commentList);
        return csActivityVos;
    }

}




