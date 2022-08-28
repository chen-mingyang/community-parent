package com.cksc.admin.vo;

import com.cksc.admin.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

@Data
public class CategoryVo {

    private String id;

    private String avatar;

    private String categoryName;

    private String description;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private SysUser sysUser;
}
