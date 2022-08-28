package com.cksc.admin.vo.params;

import lombok.Data;

import java.util.Date;


/**
 * 评论-回复参数
 */
@Data
public class CommentPageParam {

    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;

    private Date updateTime;
}
