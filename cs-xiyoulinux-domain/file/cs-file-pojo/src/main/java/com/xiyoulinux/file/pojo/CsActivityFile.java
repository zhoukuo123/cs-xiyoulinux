package com.xiyoulinux.file.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 动态内容包含的图片
 *
 * @author qkm
 * @TableName cs_activity_file
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cs_activity_file")
public class CsActivityFile implements Serializable {
    private static final long serialVersionUID = 167100518362196511L;
    /**
     * 主键id
     */
    @TableField(value = "id")
    private String id;

    /**
     * 动态id
     */
    @TableField(value = "activity_id")
    private String activityId;

    /**
     * 动态内容包含的文件url
     */
    @TableField(value = "activity_file_url")
    private String activityFileUrl;
}