package com.cksc.admin.controller;


import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.service.ArticleService;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.ArticleParam;
import com.cksc.admin.vo.params.PageParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/22 17:29
 * @description 文章管理控制类
 */

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    /**
     * 通过有无title 获取文章管理列表
     * @param pageParams
     * @return
     */
    @PostMapping("getArticleList")
    @ApiOperation(value="获取文章管理列表",notes = "根据表单信息查询")
    //加上此注解 代表要对此接口记录日志 AOP
    @LogAnnotation(module="获取文章管理列表",operator="根据表单信息查询") //aop日志
    // 加缓存时，注意数据不同步问题，数据库有，缓存没有
    //@Cache(expire = 2 * 60 * 1000,name = "adminArticleList") //aop缓存redis
    public Result getArticleList(@RequestBody PageParams pageParams){
        return articleService.getArticleList(pageParams);
    }

    /**
     * 通过ID 删除文章
     * @param id
     * @return
     */
    @PostMapping("deleteArticleById/{id}")
    @ApiOperation(value="删除文章",notes = "通过ID")
    @LogAnnotation(module="删除文章",operator="通过ID")
    public Result deleteArticleById(@PathVariable("id") Long id){
        return articleService.deleteArticleById(id);
    }

    /**
     * 通过表单信息 改变文章状态
     * @param articleParam
     * @return
     */
    @PostMapping("changeArticleState")
    @ApiOperation(value="改变文章状态",notes = "通过表单信息 ")
    @LogAnnotation(module="改变文章状态",operator="通过表单信息")
    public Result changeArticleState(@RequestBody ArticleParam articleParam){
        return articleService.changeArticleState(articleParam);
    }
}
