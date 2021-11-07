package com.xiyoulinux.file.Impl;

import com.xiyoulinux.file.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author qkm
 */
@DubboService
@Slf4j
public class UploadFileServiceImpl implements IUploadFileService {

    /**
     * @param files 文件列表
     */
    @Override
    public List<String> uploadOSS(MultipartFile[] files) {
        String fileUrl = null;
        try {
            fileUrl = upload(files);
            log.info("upload comment files success");
        } catch (Exception e) {
            log.error("upload comment files error");
            throw new RuntimeException(e.getMessage());
        }
        return Arrays.asList(fileUrl.split(","));
    }

    /**
     * 上传至阿里云oss
     *
     * @param files 文件集合
     */
    public String upload(MultipartFile[] files) {
        //阿里云oss
        return "";
    }
}
