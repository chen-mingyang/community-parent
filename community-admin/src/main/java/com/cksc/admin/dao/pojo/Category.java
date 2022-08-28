package com.cksc.admin.dao.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章分类类
 */

@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private Long uid;
}
