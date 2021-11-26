package com.xiyoulinux.file.impl;

import com.xiyoulinux.file.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
    public List<String> uploadOSS(List<byte[]> files) {
        String fileUrl = null;
        try {
            fileUrl = upload(files);
            log.info("upload files success");
        } catch (Exception e) {
            log.error("upload files error:[{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
//        return Arrays.asList(fileUrl.split(","));
        ArrayList<String> result = new ArrayList<>();
        result.add("https://t7.baidu.com/it/u=1819248061,230866778&fm=193&f=GIF");
        result.add("https://t7.baidu.com/it/u=2291349828,4144427007&fm=193&f=GIF");
        result.add("https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF");
        return result;
    }

    /**
     * 上传至阿里云oss
     *
     * @param files 文件集合
     */
    public String upload(List<byte[]> files) {
        InputStream inputStream = new ByteArrayInputStream(files.get(0));
        //阿里云oss
        return "";
    }
}
