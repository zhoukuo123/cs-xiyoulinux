package com.xiyoulinux.activity.controller;

import com.xiyoulinux.file.service.IUploadFileService;
import com.xiyoulinux.pojo.JSONResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qkm
 */
@RestController
@Slf4j
@RequestMapping("/files")
public class UploadFile {

    private final IUploadFileService iUploadFileService;

    public UploadFile(IUploadFileService iUploadFileService) {
        this.iUploadFileService = iUploadFileService;
    }

    @ApiOperation(value = "上传文件", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "文件url"))
    @PostMapping("/upload")
    public JSONResult uploadOSS(@RequestParam MultipartFile[] file) {
        System.out.println(file.length);
        List<String> fileUrl = null;
        if (file != null && file.length != 0) {
            //上传评论内容中的文件信息到文件服务
            List<byte[]> bytes = new ArrayList<>();
            for (MultipartFile file1 : file) {
                try {
                    bytes.add(file1.getBytes());
                } catch (IOException e) {
                    log.error("upload pic convert byte error");
                    throw new RuntimeException("upload pic convert byte error");
                }
            }
            //调用图片服务将图片上传到云服务器
            fileUrl = iUploadFileService.uploadOSS(bytes);
        }
        return JSONResult.ok(fileUrl);
    }
}
