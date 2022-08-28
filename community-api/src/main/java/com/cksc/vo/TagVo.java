package com.cksc.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 9:54
 * @description 文章标签VO类
 */

@Data
public class TagVo {

    private String id;

    private String avatar;

    private String tagName;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private String uid;

}
