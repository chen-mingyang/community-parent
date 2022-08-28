package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cksc.dao.mapper.ArticleMapper;
import com.cksc.dao.mapper.CommentMapper;
import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.Comment;
import com.cksc.dao.pojo.SysUser;
import com.cksc.service.CommentsService;
import com.cksc.service.SysUserService;
import com.cksc.utils.UserThreadLocal;
import com.cksc.vo.CommentVo;
import com.cksc.vo.Result;
import com.cksc.vo.UserVo;
import com.cksc.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1. 根据文章id 查询 评论列表 从 comment 表中查询
         * 2. 根据作者的id 查询作者的信息
         * 3. 判断 如果 level = 1 要去查询它有没有子评论
         * 4. 如果有 根据评论id 进行查询 （parent_id）
         */
        //查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);//先查询第1层评论
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        List<CommentVo> commentVoList = copyList(comments);  //转换成VO对象
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        //登录后，本地线程中有用户信息，从本地线程中取出用户信息
        SysUser sysUser = UserThreadLocal.get();

        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());

        //检查是否为第一次评论，还是回复评论
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();  //对谁进行评论
        comment.setToUid(toUserId == null ? 0 : toUserId);

        this.commentMapper.insert(comment);

        //更新文章 评论数
        Article article = articleMapper.selectById(commentParam.getArticleId());
        int commentCounts = article.getCommentCounts();
        article.setCommentCounts(commentCounts+1);

        //可以丢到线程池中 异步进行 加乐观锁
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,commentParam.getArticleId());
        updateWrapper.eq(Article::getLikeCounts,commentCounts);
        articleMapper.update(article,updateWrapper);

        //可以返回评论成功
        return Result.success(null);
    }

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
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        //子评论
        Integer level = comment.getLevel();
        if (1 == level){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        if (level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo); //当level>1时，就会查询to User
        }
        return commentVo;
    }

    //根据ParentId查询子评论
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        //再调用copyList，将to User传入到子评论每个CommentVo中
        List<CommentVo> commentVoList=copyList(commentMapper.selectList(queryWrapper));
        return commentVoList;
    }
}
