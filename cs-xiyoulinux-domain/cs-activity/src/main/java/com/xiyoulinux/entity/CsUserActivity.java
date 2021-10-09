package com.xiyoulinux.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户动态表
 * @TableName cs_user_dynamic
 * @author qkm
 */
@TableName(value ="cs_user_dynamic")
@Data
public class CsUserActivity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 用户Id
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 问题or动态or讲座or任务的内容
     */
    @TableField(value = "dynamic_content")
    private String dynamic_content;

    /**
     * 创建时间
     */
    @TableField(value = "dynamic_create_time")
    private Date dynamic_create_time;

    /**
     * 问题是否解决(0代表未解决)
     */
    @TableField(value = "dynamic_is_solve")
    private Integer dynamic_is_solve;

    /**
     * 问题or动态or讲座or任务(0/1/2/3)
     */
    @TableField(value = "dynamic_type")
    private Integer dynamic_type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}