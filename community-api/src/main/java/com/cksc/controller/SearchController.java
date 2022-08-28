package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.service.SearchService;
import com.cksc.vo.Result;
import com.cksc.vo.params.PageParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/20 17:22
 * @description 搜索控制类
 */
@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 主页-搜索服务
     * @param pageParams
     * @return
     */
    @PostMapping("getSearchIndexList")
    @ApiOperation(value="个人主页-文章管理 文章列表",notes = "根据表单信息查询")
    @LogAnnotation(module="个人主页-文章管理 文章列表",operator="根据表单信息查询")
    public Result getSearchIndexList(@RequestBody PageParams pageParams){
        //int i = 10/0;  //测试统一异常拦截类效果
        return searchService.getSearchIndexList(pageParams);
    }
}
