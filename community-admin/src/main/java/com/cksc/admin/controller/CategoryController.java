package com.cksc.admin.controller;


import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.dao.pojo.Category;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.service.CategoryService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.service.TagService;
import com.cksc.admin.utils.JwtUtils;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.CategoryPageParam;
import com.cksc.admin.vo.params.TagPageParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 获取分类列表分页信息 传递参数
     *
     * @param categoryPageParam
     * @return
     */
    @PostMapping("getCategoryList")
    @ApiOperation(value = "分类列表", notes = "根据表单信息查询")
    @LogAnnotation(module="分类列表",operator="根据表单信息查询")
    //@Cache(expire = 2 * 60 * 1000,name = "adminCategoryList") //aop缓存redis
    public Result getCategoryList(@RequestBody CategoryPageParam categoryPageParam) {
        return categoryService.getCategoryList(categoryPageParam);
    }

    /**
     * 个人主页-分类管理 删除分类
     *
     * @param id
     * @return
     */
    @PostMapping("deleteCategoryById/{id}")
    @ApiOperation(value = "删除分类", notes = "根据问题ID")
    @LogAnnotation(module="删除分类",operator="根据问题ID")
    public Result deleteCategoryById(@PathVariable("id") Long id) {
        return categoryService.deleteCategoryById(id);
    }

    /**
     * 通过表单信息 更新分类状态
     * @param category
     * @return
     */
    @PostMapping("changeCategoryState")
    @ApiOperation(value = "更新分类状态", notes = "通过表单信息")
    @LogAnnotation(module="更新分类状态",operator="通过表单信息")
    public Result changeCategoryState(@RequestBody Category category) {
        return categoryService.changeCategoryState(category);
    }

    /**
     * 通过表单信息 新增分类
     * @param category
     * @return
     */
    @PostMapping("addCategory")
    @ApiOperation(value = "新增分类", notes = "通过表单信息 ")
    @LogAnnotation(module="新增分类",operator="通过表单信息")
    public Result addCategory(@RequestBody Category category, HttpServletRequest request) {
        SysUser sysUser=new SysUser();

        //验证前端请求中的token
        Map<String, Object> stringObjectMap = JwtUtils.validateTokenAndGetClaims(request);
        if(stringObjectMap!= null) {
            //获取token中的用户ID 用户角色
            String userid = (String) stringObjectMap.get("userid");
            //根据找到的token，查询用户信息，返回给前端
            sysUser = sysUserService.findUserById(Long.valueOf(userid));

            category.setUid(sysUser.getId());
            return categoryService.addCategory(category);
        }

        return Result.fail(50000,"新增失败");
    }

    /**
     * 通过表单信息 编辑分类
     * @param category
     * @return
     */
    @PostMapping("updateCategory")
    @ApiOperation(value = "编辑分类", notes = "通过表单信息")
    @LogAnnotation(module="编辑分类",operator="通过表单信息")
    public Result updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

}