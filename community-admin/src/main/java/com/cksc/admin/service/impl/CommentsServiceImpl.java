package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.mapper.ArticleMapper;
import com.cksc.admin.dao.mapper.CommentMapper;
import com.cksc.admin.dao.pojo.Article;
import com.cksc.admin.dao.pojo.Comment;
import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.service.CommentsService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.vo.*;
import com.cksc.admin.vo.params.CommentPageParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleMapper articleMapper;

    //List<Comment> 转移到 List<CommentVo> 首页文章列表用
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    //Comment对象 转移到 CommentVo
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        //对象复制
        BeanUtils.copyProperties(comment,commentVo);

        commentVo.setId(String.valueOf(comment.getId())); //int转String
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        //所属文章
        Article article=articleMapper.selectById(comment.getArticleId());
        commentVo.setArticle(article);

        //子评论
        Integer level = comment.getLevel();
        //to User 给谁评论
        if (level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo); //当level>1时，就会查询to User
        }

        return commentVo;
    }

    @Override
    public Result getCommentList(CommentPageParam commentPageParam) {
        PageResult pageResult = new PageResult();
        //Mybatis-plus分页
        Page<Comment> page = new Page<>(commentPageParam.getPage(),commentPageParam.getPageSize());

        IPage<Comment> articleIPage = commentMapper.getCommentList(page,
                commentPageParam.getContent(),
                commentPageParam.getAuthorId(),
                commentPageParam.getArticleId()
                );

        List<Comment> records = articleIPage.getRecords(); //在分页中得到List<Comment>
        pageResult.setTotal((int) articleIPage.getTotal());
        List<CommentVo> tagVoList = copyList(records);

        pageResult.setList(tagVoList);
        //返回VO
        return Result.success(pageResult);
    }

    @Override
    public Result deleteCommentById(Long id) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        //注意 删除 评论-父回复的问题表中的关联
        commentMapper.deleteByMap(map);
        return Result.success("删除成功");
    }
}
