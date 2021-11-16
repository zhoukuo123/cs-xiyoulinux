package com.xiyoulinux.activity.controller;

import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import com.xiyoulinux.search.service.ISearchService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;


/**
 * @author qkm
 */
@RestController
@RequestMapping("/activity/search")
@SuppressWarnings("all")
public class SearchController {

    @DubboReference
    private  ISearchService iSearchService;

    @ApiOperation(value = "根据key搜索", notes = "活动", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "pageInfo", value = "页对象", required = true, paramType = "body")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "包含key的活动"))
    @GetMapping("/key/{userId}")
    public JSONResult searchByKey(@RequestBody PageInfo pageInfo,
                                  @PathVariable("userId") String userId) {
        PageActivityInfo pageActivityInfo = iSearchService.searchByKey(pageInfo, userId);
        if (pageActivityInfo == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code,"正在搜索中...请稍候");
        }
        return JSONResult.ok(pageActivityInfo);
    }

    @ApiOperation(value = "根据key搜索按照活动的创建时间排序", notes = "活动", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "pageInfo", value = "页对象", required = true, paramType = "body")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "包含key的活动"))
    @GetMapping("/key/orderByTime/{userId}")
    public JSONResult searchByKeyOrderByCreateTime(@RequestBody PageInfo pageInfo,
                                                         @PathVariable("userId") String userId) {
        PageActivityInfo pageActivityInfo = iSearchService.searchByKeyOrderByCreateTime(pageInfo, userId);
        if (pageActivityInfo == null) {
            return JSONResult.errorMsg(ReturnCode.DEGRADATION.code,"正在搜索中...请稍候");
        }
        return JSONResult.ok(pageActivityInfo);
    }

    @ApiOperation(value = "根据title自动补全", notes = "title", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key", required = true, paramType = "path", dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "与key同拼音或者字的标题"))
    @GetMapping("/completion/{key}")
    public JSONResult searchBoxAutoCompletion(@PathVariable("key") String key) {
        return JSONResult.ok(iSearchService.searchBoxAutoCompletion(key));
    }

}
