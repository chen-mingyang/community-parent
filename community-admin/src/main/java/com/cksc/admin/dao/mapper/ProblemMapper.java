package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Article;

import java.util.List;


/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 12:22
 * @description 问题接口类
 */
public interface ProblemMapper extends BaseMapper<Article> {

    //个人主页下的文章列表分页
    IPage<Article> getProblemList(Page<Article> page,
                                      Integer apType,
                                      String title);}
