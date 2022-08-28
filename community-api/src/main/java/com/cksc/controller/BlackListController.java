package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.dao.pojo.Blacklist;
import com.cksc.dao.pojo.Focus;
import com.cksc.service.BlackListService;
import com.cksc.vo.Result;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.BlackListParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/13 17:13
 * @description 黑名单控制
 */

//json数据进行交互
@RestController
@RequestMapping("blackList")
public class BlackListController {
    @Autowired
    private BlackListService blackListService;



    /**
     * 通过发起id、被拉黑id  查询浏览者对文章作者的拉黑屏蔽状态
     * @param blacklist
     * @return
     */
    @PostMapping("queryBlackState")
    @ApiOperation(value="查询浏览者的拉黑状态",notes = "通过发起id被拉黑id")
    @LogAnnotation(module="查询浏览者的拉黑状态",operator="通过发起id被拉黑id")
    public Result queryBlackState(@RequestBody Blacklist blacklist){
        return blackListService.queryBlackState(blacklist);
    }

    /**
     * 通过发起id、被拉黑id  添加拉黑信息
     * @param blacklist
     * @return
     */
    @PostMapping("sendBlackUser")
    @ApiOperation(value="添加拉黑信息",notes = "通过发起id被拉黑id")
    @LogAnnotation(module="添加拉黑信息",operator="通过发起id被拉黑id")
    public Result sendBlackUser(@RequestBody Blacklist blacklist){
        //int i = 10/0;  //测试统一异常拦截类效果
        return blackListService.sendBlackUser(blacklist);
    }


    ///**
    // * 根据用户ID拉黑屏蔽用户
    // * @param authorId
    // * @return
    // */
    //@PostMapping("sendBlackUser/{id}")
    //@ApiOperation(value="拉黑屏蔽用户",notes = "根据用户ID")
    //public Result sendBlackUser(@PathVariable("id") Long authorId){
    //    return blackListService.sendBlackUser(authorId);
    //}
    ///**
    // * 根据用户ID、被拉黑用户ID 去除拉黑名单
    // * @param blackListParam
    // * @return
    // */
    //@PostMapping("deleteBlackUser/{id}")
    //@ApiOperation(value="去除用户拉黑",notes = "根据用户ID、被拉黑用户ID参数")
    //public Result deleteBlackUser(@RequestBody BlackListParam blackListParam){
    //    return blackListService.deleteBlackUser(blackListParam);
    //}
    //
    //
    ///**
    // * 查询所有黑名单列表
    // * @return
    // */
    //@PostMapping("blackListAll")
    //@ApiOperation(value="查询所有黑名单列表",notes = "无条件")
    //public Result findBlackListAll(){
    //    return blackListService.findBlackListAll();
    //}
    ///**
    // * 根据用户ID查询用户的黑名单列表
    // * @return
    // */
    //@PostMapping("blackListUser/{uid}")
    //@ApiOperation(value="查询用户的黑名单列表",notes = "根据用户ID")
    //public Result findBlackListById(@PathVariable("uid") Long id){
    //    return blackListService.findBlackListById(id);
    //}




}
