package com.zcxAgent.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * memory实体表
 */
@TableName(value ="memory")
@Data
@Builder
public class Memory {
    /**
     * id
     */
    @TableId
    private int id;

    /**
     * 会话id
     */
    private String conversation_id;

    /**
     * user / assistant / system
     */
    private String role;

    /**
     * 
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}