package com.xiyoulinux.file.service;

import java.util.HashMap;
import java.util.List;

/**
 * @author qkm
 */
public interface GetFileService {
    /**
     * 根据文件id查找文件
     *
     * @param activityIdList 动态id集合
     * @return 所有的文件
     */
    HashMap<String, List<String>> getFileUrlByActivityId(List<String> activityIdList);

    /**
     * 根据评论id查找文件
     *
     * @param commentIdList 评论id集合
     * @return 所有的文件
     */
    HashMap<String, List<String>> getFileUrlByCommentId(List<String> commentIdList);
}
