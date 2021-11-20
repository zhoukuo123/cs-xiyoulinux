package com.xiyoulinux.file.service;
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
    List<String> uploadOSS(List<byte[]> files);
}
