package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.mapper.CategoryMapper;
import com.cksc.admin.dao.pojo.Category;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.service.CategoryService;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.vo.CategoryVo;
import com.cksc.admin.vo.PageResult;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.TagVo;
import com.cksc.admin.vo.params.CategoryPageParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SysUserService sysUserService;

    public CategoryVo copy(Category category){
        SysUser sysUser = sysUserService.findUserById(category.getUid());
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        categoryVo.setSysUser(sysUser);
        return categoryVo;
    }
    //List<Category> 转移到 List<CategoryVo>
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }


    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    @Override
    public Result getCategoryList(CategoryPageParam categoryPageParam) {
        PageResult pageResult = new PageResult();
        //Mybatis-plus分页
        Page<Category> page = new Page<>(categoryPageParam.getPage(),categoryPageParam.getPageSize());
        //apType=1 文章
        IPage<Category> categoryIPage = categoryMapper.getCategoryList(page,categoryPageParam.getCategoryName());

        List<Category> records = categoryIPage.getRecords(); //在分页中得到List<Category>
        pageResult.setTotal((int) categoryIPage.getTotal());
        List<CategoryVo> categoryVoList = copyList(records);

        pageResult.setList(categoryVoList);
        //返回VO
        return Result.success(pageResult);
    }

    @Override
    public Result deleteCategoryById(Long id) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        //注意 删除 标签-文章问题表中的关联
        categoryMapper.deleteByMap(map);
        return Result.success("删除成功");
    }

    @Override
    public Result addCategory(Category category) {
        //从token中获取当前用户ID
        category.setCreateTime(new Date());
        category.setState(1);
        categoryMapper.insert(category);
        return Result.success("新增成功");
    }

    @Override
    public Result updateCategory(Category category) {
        category.setUpdateTime(new Date());
        categoryMapper.updateById(category);
        return Result.success("编辑成功");
    }

    @Override
    public Result changeCategoryState(Category category) {
        category.setUpdateTime(new Date());
        categoryMapper.updateById(category);
        return Result.success("改变成功");
    }


}
