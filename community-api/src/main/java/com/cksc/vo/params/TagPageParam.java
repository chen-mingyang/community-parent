package com.cksc.vo.params;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 17:21
 * @description 标签分页参数
 */
@Data
public class TagPageParam {

    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private Long id;

    private String avatar;

    private String tagName;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private Long uid;

}
