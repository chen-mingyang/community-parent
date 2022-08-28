package com.cksc.controller;


import com.cksc.common.aop.LogAnnotation;
import com.cksc.common.cache.Cache;
import com.cksc.service.CategoryService;
import com.cksc.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类信息
     * @return
     */
    @GetMapping
    @ApiOperation(value="查询所有分类信息",notes = "无条件")
    @LogAnnotation(module="查询所有分类信息",operator="无条件")
    @Cache(expire = 2 * 60 * 1000,name = "categories") //切点 缓存redis
    public Result categories(){
        return categoryService.findAll();
    }

    /**
     * 查询所有分类信息-详情
     * @return
     */
    @GetMapping("detail")
    @ApiOperation(value="查询所有分类信息-详情",notes = "无条件")
    @LogAnnotation(module="查询所有分类信息-详情",operator="无条件")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    /**
     * 根据ID查询特定分类信息详情
     * @param id
     * @return
     */
    ///category/detail/{id}
    @GetMapping("detail/{id}")
    @ApiOperation(value="查询特定分类信息详情",notes = "根据ID")
    @LogAnnotation(module="查询特定分类信息详情-详情",operator="根据ID")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }
}
