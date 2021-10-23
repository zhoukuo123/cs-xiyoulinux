package com.xiyoulinux.file.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户图片表
 *
 * @author qkm
 * @TableName cs_user_file
 */
@TableName(value = "cs_user_file")
@Data
@AllArgsConstructor
public class CsUserFile implements Serializable {
    /**
     * 主键
     */
    @TableField(value = "id")
    private String id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 用户图片url
     */
    @TableField(value = "user_pic_url")
    private String userPicUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}