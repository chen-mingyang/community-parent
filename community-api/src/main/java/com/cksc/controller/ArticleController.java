package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.common.cache.Cache;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.Like;
import com.cksc.dao.pojo.ThumbsUp;
import com.cksc.service.ArticleService;
import com.cksc.service.LikeService;
import com.cksc.service.ThumbsUpService;
import com.cksc.vo.Result;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.PageParams;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章控制类
 */

//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ThumbsUpService thumbsUpService;
    @Autowired
    private LikeService likeService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @ApiOperation(value="文章列表",notes = "根据表单信息查询")
    //加上此注解 代表要对此接口记录日志 AOP
    @LogAnnotation(module="文章",operator="获取文章列表") //aop日志
    // 加缓存时，注意数据不同步问题，数据库有，缓存没有
    @Cache(expire = 2 * 60 * 1000,name = "listArticle") //aop缓存redis
    public Result listArticles(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return articleService.listArticles(pageParams);
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    @ApiOperation(value="最热文章",notes = "无条件")
    @LogAnnotation(module="最热文章",operator="获取最热文章列表") //aop日志
    @Cache(expire = 2 * 60 * 1000,name = "hotArticle") //切点 缓存redis
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    @ApiOperation(value="最新文章",notes = "无条件")
    @LogAnnotation(module="最新文章",operator="获取最新文章列表") //aop日志
    //@Cache(expire = 2 * 60 * 1000,name = "newsArticle")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 首页 文章归档
     * @return
     */
    @PostMapping("listArchives")
    @ApiOperation(value="文章归档",notes = "无条件")
    @LogAnnotation(module="文章归档",operator="获取文章归档列表") //aop日志
    //@Cache(expire = 2 * 60 * 1000,name = "listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 根据文章ID查询文章详情
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    @ApiOperation(value="查询文章详情",notes = "根据DI查询文章详情")
    @LogAnnotation(module="查询文章详情",operator="获取文章详情") //aop日志
    @Cache(expire = 2 * 60 * 1000,name = "view_article")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 根据传参发布文章 /articles/publish
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    @ApiOperation(value="发布文章",notes = "根据传参")
    @LogAnnotation(module="发布文章",operator="发布文章信息") //aop日志
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

    /**
     * 发布文章后，跳转到文章详情页
     * @param articleId
     * @return
     */
    @PostMapping("{id}")
    @ApiOperation(value="发布文章后",notes = "根据文章ID跳转")
    @LogAnnotation(module="跳转文章详情",operator="跳转文章详情") //aop日志
    public Result articleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 通过uid、文章id  查询浏览者对文章的点赞状态
     * @param thumbsUp
     * @return
     */
    @PostMapping("queryThumbsUpState")
    @ApiOperation(value="查询浏览者对文章的点赞状态",notes = "通过uid、文章id")
    @LogAnnotation(module="文章的点赞状态",operator="文章的点赞状态") //aop日志
    public Result queryThumbsUpState(@RequestBody ThumbsUp thumbsUp){
        return thumbsUpService.queryThumbsUpState(thumbsUp);
    }

    /**
     * 通过uid、文章id  添加点赞信息
     * @param thumbsUp
     * @return
     */
    @PostMapping("addThumbsUp")
    @ApiOperation(value="添加点赞信息",notes = "通过uid、文章id")
    @LogAnnotation(module="添加点赞信息",operator="添加点赞信息") //aop日志
    public Result addThumbsUp(@RequestBody ThumbsUp thumbsUp){

        return thumbsUpService.addThumbsUp(thumbsUp);
    }

    /**
     * 通过uid、文章id  删除点赞信息
     * @param thumbsUp
     * @return
     */
    @PostMapping("deleteThumbsUp")
    @ApiOperation(value="删除点赞信息",notes = "通过uid、文章id")
    @LogAnnotation(module="删除点赞信息",operator="删除点赞信息") //aop日志
    public Result deleteThumbsUp(@RequestBody ThumbsUp thumbsUp){

        return thumbsUpService.deleteThumbsUp(thumbsUp);
    }

    /**
     * 通过uid、文章id  查询浏览者对文章的收藏状态
     * @param like
     * @return
     */
    @PostMapping("queryLikeState")
    @ApiOperation(value="查询浏览者对文章的收藏状态",notes = "通过uid、文章id")
    @LogAnnotation(module="文章的收藏状态",operator="文章的收藏状态") //aop日志
    public Result queryLikeState(@RequestBody Like like){

        return likeService.queryLikeState(like);
    }

    /**
     * 通过uid、文章id  添加收藏信息
     * @param like
     * @return
     */
    @PostMapping("addLike")
    @ApiOperation(value="添加收藏信息",notes = "通过uid、文章id")
    @LogAnnotation(module="添加收藏信息",operator="添加收藏信息") //aop日志
    public Result addLike(@RequestBody Like like){

        return likeService.addLike(like);
    }

    /**
     * 通过uid、文章id  删除收藏信息
     * @param like
     * @return
     */
    @PostMapping("deleteLike")
    @ApiOperation(value="删除收藏信息",notes = "通过uid、文章id")
    @LogAnnotation(module="删除收藏信息",operator="删除收藏信息") //aop日志
    public Result deleteLike(@RequestBody  Like like){

        return likeService.deleteLike(like);
    }
}

