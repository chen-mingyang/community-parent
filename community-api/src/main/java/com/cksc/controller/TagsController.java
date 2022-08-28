package com.cksc.controller;


import com.cksc.common.aop.LogAnnotation;
import com.cksc.common.cache.Cache;
import com.cksc.service.TagService;
import com.cksc.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags") //路径映射
public class TagsController {
    @Autowired
    private TagService tagService;

    /**
     * 查询热门标签   请求路径/tags/hot
     * @return
     */
    @GetMapping("hot")
    @ApiOperation(value="查询热门标签",notes = "无条件")
    @LogAnnotation(module="查询热门标签",operator="无条件")
    //@Cache(expire = 2 * 60 * 1000,name = "hotTags") //切点 缓存redis
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }

    /**
     * 查询所有标签   请求路径/tags
     * @return
     */
    @GetMapping
    @ApiOperation(value="查询所有标签",notes = "无条件")
    @LogAnnotation(module="查询所有标签",operator="无条件")
    //@Cache(expire = 2 * 60 * 1000,name = "findAllTags") //切点 缓存redis
    public Result findAll(){
        return tagService.findAll();
    }

    /**
     * 查询所有标签-详情   请求路径/tags/detail
     * @return
     */
    @GetMapping("detail")
    @ApiOperation(value="查询所有标签-详情",notes = "无条件")
    @LogAnnotation(module="查询所有标签-详情",operator="无条件")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }
    /**
     * 根据ID查询特定标签-详情   请求路径/tags/detail/{id}
     * @return
     */
    @GetMapping("detail/{id}")
    @ApiOperation(value="查询特定标签-详情 ",notes = "根据ID")
    @LogAnnotation(module="查询所有标签-详情",operator="根据ID")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }

}
