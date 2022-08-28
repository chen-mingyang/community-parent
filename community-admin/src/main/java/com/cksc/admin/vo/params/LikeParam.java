package com.cksc.admin.vo.params;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 21:10
 * @description 收藏 点赞接收类
 */
@Data
public class LikeParam {
    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private String title;

    private Long liId;

    private Long apId;

    private Long uid;

    private Date createTime;
}
