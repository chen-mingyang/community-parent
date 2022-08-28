package com.cksc.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 权限表
 */
@Data
public class Permission {

    //后端管理不用分布式ID 加@TableId(type = IdType.AUTO)
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String description;
}
