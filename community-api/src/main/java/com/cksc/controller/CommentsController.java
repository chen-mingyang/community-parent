package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.service.CommentsService;
import com.cksc.vo.Result;
import com.cksc.vo.params.CommentParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    /**
     * 根据文章ID展示文章评论
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    @ApiOperation(value="展示文章评论",notes = "根据文章ID")
    @LogAnnotation(module="展示文章评论-详情",operator="根据文章ID")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }


    /**
     * 根据传来的参数创建评论
     * @param commentParam
     * @return
     * 问题:前端评论后，要手动刷新才能看到评论，没有做刷新，可在前端完善
     */
    @PostMapping("create/change")
    @ApiOperation(value="创建评论",notes = "根据传来的参数")
    @LogAnnotation(module="创建评论",operator="根据传来的参数")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
