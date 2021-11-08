package com.xiyoulinux.activity.comment.vo;

import com.xiyoulinux.common.CsUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qkm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息和动态id/评论id")
public class CsUserInfoAndIdAndFileInfo {

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CsUserInfo csUserInfo;

    /**
     * 动态/评论id
     */
    @ApiModelProperty(value = "动态/评论id")
    private String id;

    /**
     * 文件url
     */
    private List<String> files;


}
