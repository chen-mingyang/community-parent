package com.cksc.service;


import com.cksc.vo.CategoryVo;
import com.cksc.vo.Result;

public interface CategoryService {

    /**
     * 根据分类ID查询分类信息
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 查询所有分类信息
     * @return
     */
    Result findAll();

    /**
     * 查询所有分类信息-详情
     * @return
     */
    Result findAllDetail();

    Result categoryDetailById(Long id);
}
