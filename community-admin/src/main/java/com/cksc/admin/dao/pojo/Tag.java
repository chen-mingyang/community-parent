package com.cksc.admin.dao.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 标签类
 */

@Data
public class Tag {

    private Long id;

    private String avatar;

    private String tagName;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private Long uid;

}
