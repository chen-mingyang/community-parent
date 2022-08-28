package com.cksc.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.dos.Archives;
import com.cksc.dao.pojo.Article;


import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章接口类
 */

//BaseMapper<Article>实现快速对Article单表增删改查
public interface ArticleMapper extends BaseMapper<Article> {

    //文章归档-根据年月日归档
    List<Archives> listArchives();

    //根据归档年月查询对应的文章列表 自写SQL 自定义分页无须在xml做分页处理，只需要查询列表即可
    //首页文章列表、分类/标签下的文章列表、归档下的文章列表、
    IPage<Article> listArticles(Page<Article> page,
                               Integer apType,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
    //个人主页下的文章列表分页
    IPage<Article> getUserArticleList(Page<Article> page,
                                      Integer apType,
                                Long authorId,
                                Long tid,
                                String title);

    /**
     * 主页-搜索服务 根据文章、问题标题
     * @param page
     * @param title
     * @return
     */
    IPage<Article> getSearchIndexList(Page<Article> page, String title);
}
