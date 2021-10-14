package com.xiyoulinux.bo;

import com.xiyoulinux.bo.CsUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息和动态id/评论id")
public class CsUserInfoAndId {

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;

    /**
     * 动态/评论id
     */
    @ApiModelProperty(value = "动态/评论id")
    private Id id;

    /**
     * 动态/评论id
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "动态/评论id")
    public static class Id {
        /**
         * 动态/评论id
         */
        @ApiModelProperty(value = "动态/评论id")
        private String id;
    }
}
