package com.cksc.admin.service;


import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.ArticleParam;
import com.cksc.admin.vo.params.PageParams;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 文章服务类
 */

public interface ArticleService {

    /**
     * 通过有无title 获取文章管理列表
     * @param pageParams
     * @return
     */
    Result getArticleList(PageParams pageParams);

    /**
     * 通过ID 删除文章
     * @param id
     * @return
     */
    Result deleteArticleById(Long id);
    /**
     * 通过表单信息 改变文章状态
     * @param articleParam
     * @return
     */
    Result changeArticleState(ArticleParam articleParam);
}
