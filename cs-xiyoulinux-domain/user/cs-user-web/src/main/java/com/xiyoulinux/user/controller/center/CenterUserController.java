package com.xiyoulinux.user.controller.center;

import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.center.CenterUserBO;
import com.xiyoulinux.user.service.center.CenterUserService;
import com.xiyoulinux.utils.CookieUtils;
import com.xiyoulinux.utils.JsonUtils;
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

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

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
                    return JSONResult.errorMsg("图片格式不正确!");
                }

                // TODO 调用文件微服务
                path = fdfsService.uploadOSS(file, userId, suffix);
            }
        } else {
            return JSONResult.errorMsg("文件不能为空!");
        }

        if (StringUtils.isNotBlank(path)) {
            String finalUserFaceUrl = fileResource.getOssHost() + path;

            // 更新用户头像到数据库
            CsUser userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
//
            // 把用户信息放入cookie中, 前端更新用户信息展示
            CookieUtils.setCookie(request, response, "user",
                    JsonUtils.objectToJson(userResult), true);

        } else {
            return JSONResult.errorMsg("上传头像失败");
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        // 判断BindingResult是否包含错误的验证信息, 如果有, 则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JSONResult.errorMap(errorMap);
        }

        CsUser userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        // 把用户信息放入cookie中, 前端更新用户信息展示
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        return JSONResult.ok();
    }

    @ApiOperation(value = "查看该用户所发布的动态", notes = "查看该用户的相关动态", httpMethod = "GET")
    @GetMapping("/activity")
    public JSONResult activity(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        CsUser user = centerUserService.queryUserInfo(userId);

        // TODO 发起RPC调用动态中心获取动态, 之后封装为 CenterUserActivityVO 对象, 返回 List



        return JSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {

        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一个属性
            String errorField = error.getField();
            //  验证错误的信息
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
        }
        return map;
    }

}
