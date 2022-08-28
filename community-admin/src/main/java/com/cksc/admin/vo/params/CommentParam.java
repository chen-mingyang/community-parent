package com.cksc.admin.vo.params;

import lombok.Data;


/**
 * 评论-回复参数
 */
@Data
public class CommentParam {

    private Long articleId; //文章ID

    private String content;

    private Long parent; //父评论ID

    private Long toUserId;  //对谁进行评论
}
