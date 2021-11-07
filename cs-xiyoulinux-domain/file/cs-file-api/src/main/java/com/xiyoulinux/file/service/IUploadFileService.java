package com.xiyoulinux.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author qkm
 */
public interface IUploadFileService {

    /**
     * 上传oss
     *
     * @param files  文件列表
     * TODO
     */
    List<String> uploadOSS(MultipartFile[] files);
}
