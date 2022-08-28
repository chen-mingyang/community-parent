package com.cksc.admin.service;


import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.ArticleParam;
import com.cksc.admin.vo.params.PageParams;
import com.cksc.admin.vo.params.ProblemParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:50
 * @description 问题服务接口类
 */
public interface ProblemService {
    /**
     * 通过有无title 获取问题管理列表
     * @param pageParams
     * @return
     */
    Result getProblemList(PageParams pageParams);


    /**
     * 通过ID 删除文章
     * @param id
     * @return
     */
    Result deleteProblemById(Long id);

    /**
     * 通过表单信息 改变问题状态
     * @param problemParam
     * @return
     */

    Result changeProblemState(ProblemParam problemParam);

}
