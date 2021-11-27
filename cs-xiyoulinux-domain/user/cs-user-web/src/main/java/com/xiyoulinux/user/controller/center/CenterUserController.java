package com.xiyoulinux.user.controller.center;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.exception.business.PassportException;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.center.CenterUserBO;
import com.xiyoulinux.user.service.center.CenterUserService;
import com.xiyoulinux.utils.CookieUtils;
import com.xiyoulinux.utils.JsonUtils;
import com.xiyoulinux.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CoderZk
 */
@Api(value = "用户信息接口", tags = "用户信息相关接口")
@RestController
@RequestMapping("/userInfo")
public class CenterUserController {

    @Resource
    private CenterUserService centerUserService;

    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws PassportException {

        String path = "";

        // 开始文件上传
        if (file != null) {
            // 获得文件上传的文件名称
            String fileName = file.getOriginalFilename();

            if (StringUtils.isNotBlank(fileName)) {

                // 文件重命名 lalala-face.png -> ["lalala-face", "png"]
                String[] fileNameArr = fileName.split("\\.");

                // 获取文件的后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];

                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    throw new PassportException(ReturnCode.INVALID_PARAM.code, "图片格式不正确");
                }

                // TODO 调用文件微服务
////////////////                path = fdfsService.uploadOSS(file, userId, suffix);
            }
        } else {
            throw new PassportException(ReturnCode.INVALID_PARAM.code, "文件不能为空");
        }

        if (StringUtils.isNotBlank(path)) {
//////////////// FIX 临时注释            String finalUserFaceUrl = fileResource.getOssHost() + path;

            // 更新用户头像到数据库
//////////////// FIX 临时注释            CsUser userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
//
            // 把用户信息放入cookie中, 前端更新用户信息展示
//////////////// FIX 临时注释            CookieUtils.setCookie(request, response, "user",
//////////////// FIX 临时注释                    JsonUtils.objectToJson(userResult), true);

        } else {
            throw new PassportException(ReturnCode.ERROR.code, "上传头像失败");
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            HttpServletRequest request, HttpServletResponse response) {

        // 缓存一致性: 缓存旁路模式(先更新数据库, 然后删除缓存)
        // 遇到写请求, 先更新数据库
        CsUser userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        // 然后删除缓存, del 如果key不存在, 会忽略, 不会报错
        redisOperator.delOne("USERID:" + userId);


        // 把用户信息放入cookie中, 前端更新用户信息展示
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        return JSONResult.ok();
    }

    @ApiOperation(value = "查看该用户所发布的动态", notes = "查看该用户的相关动态", httpMethod = "GET")
    @GetMapping("/activity")
    public JSONResult activity(
            @RequestParam String userId) {

        CsUser user = centerUserService.queryUserInfo(userId);

        // TODO 发起RPC调用动态中心获取动态, 之后封装为 CenterUserActivityVO 对象, 返回 List



        return JSONResult.ok();
    }

}
