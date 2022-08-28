package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Article;


import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章接口类
 */

//BaseMapper<Article>实现快速对Article单表增删改查
public interface ArticleMapper extends BaseMapper<Article> {

    //文章管理列表 自写SQL 自定义分页无须在xml做分页处理，只需要查询列表即可
    IPage<Article> getArticleList(Page<Article> page,
                                      Integer apType,
                                String title);

}
