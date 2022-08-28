package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Comment;


public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Comment> getCommentList(Page<Comment> page, String content, Long authorId, Long articleId);
}
