package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.Team;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.TeamParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:53
 * @description 团队服务接口类
 */
public interface TeamService {

    /**
     * 根据用户ID、团队名称 查询我的团队列表
     * @param teamParam
     * @return
     */
    Result getTeamList(TeamParam teamParam);
    /**
     * 通过用户ID、团队ID 判断删除 退出团队
     * @param tid
     * @return
     */
    Result deleteTeamByTid(Long tid);
    /**
     * 根据tid,teamState 改变团队状态
     * @param teamParam
     * @return
     */
    Result changeTeamState(TeamParam teamParam);

    Team findTeamByTid(Long tid);
}
