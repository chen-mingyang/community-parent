package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.TeamUser;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.TeamParam;
import com.cksc.admin.vo.params.TeamUserParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:39
 * @description 团队成员服务接口
 */
public interface TeamUserService {
    /**
     * 通过uid 查看用户是否有加入团队
     * @param uid
     * @return
     */
    Result getUserTeamState(Long uid);
    /**
     * 通过tid、nickname 获取团队成员表
     * @param teamUserParam
     * @return
     */
    Result getTeamUserList(TeamUserParam teamUserParam);
    /**
     * 通过成员表唯一ID 删除
     * @param tuId
     * @return
     */
    Result deleteTeamUserBytuId(Long tuId);
    /**
     * 通过表单信息 新增团队成员 根据uid 或 名称
     * @param teamUser
     * @return
     */
    Result addTeamUser(TeamUser teamUser);

    /**
     * 通过表单信息 加入团队 根据tid 或 名称
     * @param teamParam
     * @return
     */
    Result addTeam(TeamParam teamParam);


}
