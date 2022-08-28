package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.common.cache.Cache;
import com.cksc.service.ArticleService;
import com.cksc.service.ProblemService;
import com.cksc.vo.Result;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.PageParams;
import com.cksc.vo.params.ProblemParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:30
 * @description 问题控制类
 */
@RestController
@RequestMapping("problems")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    /**
     * 首页 问题列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @ApiOperation(value="问题列表",notes = "根据表单信息查询")
    //加上此注解 代表要对此接口记录日志 AOP
    @LogAnnotation(module="问题列表",operator="根据表单信息查询")
    @Cache(expire = 2 * 60 * 1000,name = "listProblem") //aop缓存redis
    public Result listProblems(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return problemService.listProblems(pageParams);
    }

    /**
     * 首页 最热问题
     * @return
     */
    @PostMapping("hot")
    @ApiOperation(value="最热问题",notes = "无条件")
    @LogAnnotation(module="最热问题",operator="无条件")
    //@Cache(expire = 2 * 60 * 1000,name = "hotProblem") //切点 缓存redis
    public Result hotProblems(){
        int limit = 5;
        return problemService.hotProblems(limit);
    }

    /**
     * 首页 最新问题
     * @return
     */
    @PostMapping("new")
    @ApiOperation(value="最新问题",notes = "无条件")
    @LogAnnotation(module="最新问题",operator="无条件")
    //@Cache(expire = 2 * 60 * 1000,name = "newsProblem")
    public Result newProblems(){
        int limit = 5;
        return problemService.newProblems(limit);
    }

    /**
     * 首页 问题归档
     * @return
     */
    @PostMapping("archiveProblems")
    @ApiOperation(value="问题归档",notes = "无条件")
    @LogAnnotation(module="问题归档",operator="无条件")
    //@Cache(expire = 2 * 60 * 1000,name = "archiveProblems")
    public Result archiveProblems(){
        return problemService.archiveProblems();
    }

    /**
     * 根据问题ID查询问题详情
     * @param problemId
     * @return
     */
    @PostMapping("view/{id}")
    @ApiOperation(value="查询问题详情",notes = "根据DI查询问题详情")
    @LogAnnotation(module="查询问题详情",operator="根据DI查询问题详情")
    @Cache(expire = 2 * 60 * 1000,name = "view_problem")
    public Result findProblemById(@PathVariable("id") Long problemId){
        return problemService.findProblemById(problemId);
    }

    /**
     * 根据传参发布问题
     * @param problemParam
     * @return
     */
    @PostMapping("publish")
    @ApiOperation(value="发布问题",notes = "根据传参")
    @LogAnnotation(module="发布问题",operator="根据传参")
    public Result publish(@RequestBody ProblemParam problemParam){

        return problemService.publish(problemParam);
    }

    /**
     * 发布文章后，跳转到文章详情页
     * @param problemId
     * @return
     */
    @PostMapping("{id}")
    @ApiOperation(value="发布文章后",notes = "根据文章ID跳转")
    @LogAnnotation(module="发布文章后",operator="根据文章ID跳转")
    public Result problemById(@PathVariable("id") Long problemId){
        return problemService.findProblemById(problemId);
    }
}
