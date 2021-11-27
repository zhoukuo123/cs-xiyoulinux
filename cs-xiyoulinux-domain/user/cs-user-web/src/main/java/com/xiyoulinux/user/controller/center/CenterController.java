package com.xiyoulinux.user.controller.center;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.user.pojo.CsUser;
import com.xiyoulinux.user.pojo.bo.center.CenterUserBO;
import com.xiyoulinux.user.service.center.CenterUserService;
import com.xiyoulinux.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author CoderZk
 */
@Api(value = "center - 用户中心", tags = "用户中心展示的相关接口")
@RestController
@RequestMapping("/center")
public class CenterController {

    @Resource
    private CenterUserService centerUserService;

    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public JSONResult userInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {
        // 使用redis缓存用户信息, 因为这部分数据经常访问, 而且不经常修改, 适合使用redis缓存
        // 缓存一致性: 缓存旁路模式(先更新数据库, 然后删除缓存)

        // 这种办法有很小概率出现缓存和数据库不一致的情况, 解决办法可以: 设置key的过期时间, 一段时间key过期后
        // 就会从数据库拉取最新数据, 有短时间的缓存不一致
        String centerUserBOJSON = redisOperator.hget("USERID:" + userId, "value");
        // 缓存中没有
        if (centerUserBOJSON == null) {
            // 从数据库中获取数据, 然后存入redis缓存
            CsUser user = centerUserService.queryUserInfo(userId);

            CenterUserBO centerUserBO = new CenterUserBO();
            BeanUtils.copyProperties(user, centerUserBO);
            redisOperator.hset("USERID:" + userId, "value", JSON.toJSONString(centerUserBO));
            return JSONResult.ok(centerUserBO);
        } else {
            // 缓冲中有数据, 直接返回redis缓存中的数据
            return JSONResult.ok(centerUserBOJSON);
        }
    }
}
