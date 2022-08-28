package com.cksc.service;


import com.cksc.vo.Result;
import com.cksc.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id 查询所有的评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);
    /**
     * 根据传来的参数 创建评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
