package com.cksc.service;

import com.cksc.dao.pojo.Team;
import com.cksc.vo.Result;
import com.cksc.vo.params.TeamParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:53
 * @description 团队服务接口类
 */
public interface TeamService {

    /**
     * 返回Team
     */
    Team findTeamById(Long tid);

    /**
     * 返回Team
     */
    Team findTeamByName(String tname);

    /**
     * 通过id 获取用户创建的团队信息
     * @param uid
     * @return
     */
    Result getTeamByUid(Long uid);
    /**
     * 通过表单信息 新增团队
     * @param teamParam
     * @return
     */
    Result createTeam(TeamParam teamParam);
    /**
     * 通过表单信息 编辑团队
     * @param teamParam
     * @return
     */
    Result updateTeam(TeamParam teamParam);
    /**
     * 根据用户ID、团队名称 查询我的团队列表
     * @param teamParam
     * @return
     */
    Result getUserTeamList(TeamParam teamParam);
    /**
     * 通过用户ID、团队ID 判断删除 退出团队
     * @param teamParam
     * @return
     */
    Result deleteUserTeamById(TeamParam teamParam);
    /**
     * 通过tid 获取团队信息
     * @param tid
     * @return
     */
    Result getTeamByTid(Long tid);


}
