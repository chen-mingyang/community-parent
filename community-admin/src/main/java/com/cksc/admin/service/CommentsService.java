package com.cksc.admin.service;


import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.CommentPageParam;
import com.cksc.admin.vo.params.CommentParam;

public interface CommentsService {

    /**
     * 获取评论列表分页信息 传递参数 2022年3月24日01:03:05
     * @param commentPageParam
     * @return
     */
    Result getCommentList(CommentPageParam commentPageParam);
    /**
     * 根据评论ID 删除评论
     * @param id
     * @return
     */
    Result deleteCommentById(Long id);
}
