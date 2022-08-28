package com.cksc.admin.controller;


import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.service.ArticleService;
import com.cksc.admin.service.ProblemService;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.ArticleParam;
import com.cksc.admin.vo.params.PageParams;
import com.cksc.admin.vo.params.ProblemParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/22 17:29
 * @description 问题管理控制类
 */

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;


    /**
     * 通过有无title 获取问题管理列表
     * @param pageParams
     * @return
     */
    @PostMapping("getProblemList")
    @ApiOperation(value="获取问题管理列表",notes = "根据表单信息查询")
    @LogAnnotation(module="获取问题管理列表",operator="根据表单信息查询")
    //@Cache(expire = 2 * 60 * 1000,name = "adminProblemList") //aop缓存redis
    public Result getProblemList(@RequestBody PageParams pageParams){
        return problemService.getProblemList(pageParams);
    }

    /**
     * 通过ID 删除问题
     * @param id
     * @return
     */
    @PostMapping("deleteProblemById/{id}")
    @ApiOperation(value="删除问题",notes = "通过ID")
    @LogAnnotation(module="删除问题",operator="通过ID")
    public Result deleteProblemById(@PathVariable("id") Long id){
        return problemService.deleteProblemById(id);
    }

    /**
     * 通过表单信息 改变问题状态
     * @param problemParam
     * @return
     */
    @PostMapping("changeProblemState")
    @ApiOperation(value="改变问题状态",notes = "通过表单信息 ")
    @LogAnnotation(module="改变问题状态",operator="通过表单信息")
    public Result changeProblemState(@RequestBody ProblemParam problemParam){
        return problemService.changeProblemState(problemParam);
    }
}
