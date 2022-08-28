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
 * @date 2022/3/14 12:22
 * @description 问题接口类
 */
public interface ProblemMapper extends BaseMapper<Article> {
    //问题归档-根据年月日归档
    List<Archives> archiveProblems();

    //根据归档年月查询对应的问题列表 自写SQL 自定义分页无须在xml做分页处理，只需要查询列表即可
    IPage<Article> listProblems(Page<Article> page,
                               Integer apType,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

    //个人主页下的文章列表分页
    IPage<Article> getUserProblemList(Page<Article> page,
                                      Integer apType,
                                      Long authorId,
                                      Long tid,
                                      String title);}
