package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.Category;
import com.cksc.admin.vo.CategoryVo;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.CategoryPageParam;

public interface CategoryService {

    /**
     * 根据分类ID查询分类信息
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 获取分类列表分页信息 传递参数
     * @param categoryPageParam
     * @return
     */
    Result getCategoryList(CategoryPageParam categoryPageParam);
    /**
     * 个人主页-分类管理 删除问分类 根据用户ID
     * @param id
     * @return
     */
    Result deleteCategoryById(Long id);
    /**
     * 通过表单信息 新增分类
     * @param category
     * @return
     */
    Result addCategory(Category category);
    /**
     * 通过表单信息 编辑分类 根据分类ID
     * @param category
     * @return
     */
    Result updateCategory(Category category);
    /**
     * 通过表单信息 更新分类状态
     * @param category
     * @return
     */
    Result changeCategoryState(Category category);
}
