package com.xiyoulinux.activity.vo;

import com.xiyoulinux.activity.pojo.CsUserActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsActivityVo {
    /**
     * 动态信息
     */
    private CsUserActivity csUserActivity;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户图片
     */
    private String userPic;
}
