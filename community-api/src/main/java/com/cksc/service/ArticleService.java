package com.cksc.service;

import com.cksc.dao.pojo.Focus;
import com.cksc.vo.Result;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.PageParams;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章服务类
 */

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticles(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);
    /**
     * 文章发布服务 发布和编辑两用接口
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    /**
     * 分页查询 根据用户ID、标题 查询分页文章列表
     * @param pageParams
     * @return
     */
    Result getUserArticleList(PageParams pageParams);

    /**
     * 个人主页-文章管理 删除文章
     * @param id
     * @return
     */
    Result deleteUserArticleById(Long id);

}
