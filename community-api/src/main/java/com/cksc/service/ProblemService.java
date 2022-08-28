package com.cksc.service;

import com.cksc.vo.Result;
import com.cksc.vo.params.ArticleParam;
import com.cksc.vo.params.PageParams;
import com.cksc.vo.params.ProblemParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/14 11:50
 * @description 问题服务接口类
 */
public interface ProblemService {
    /**
     * 分页查询 问题列表
     * @param pageParams
     * @return
     */
    Result listProblems(PageParams pageParams);

    /**
     * 最热问题
     * @param limit
     * @return
     */
    Result hotProblems(int limit);

    /**
     * 最新问题
     * @param limit
     * @return
     */
    Result newProblems(int limit);

    /**
     * 问题归档
     * @return
     */
    Result archiveProblems();

    /**
     * 查看问题详情
     * @param problemId
     * @return
     */
    Result findProblemById(Long problemId);
    /**
     * 问题发布服务 发布和编辑两用接口
     * @param problemParam
     * @return
     */
    Result publish(ProblemParam problemParam);

    /**
     * 分页查询 根据用户ID、标题 查询分页问题列表
     * @param pageParams
     * @return
     */
    Result getUserProblemList(PageParams pageParams);

    /**
     * 个人主页-问题管理 删除问题
     * @param id
     * @return
     */
    Result deleteUserProblemById(Long id);

}
