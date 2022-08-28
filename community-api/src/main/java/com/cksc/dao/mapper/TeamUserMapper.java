package com.cksc.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.pojo.TeamUser;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:41
 * @description 团队成员接口类
 */
public interface TeamUserMapper extends BaseMapper<TeamUser> {
    /**
     * 通过tid、nickname 获取团队成员表
     * @param page
     * @param tid
     * @param nickname
     * @return
     */
    IPage<TeamUser> getTeamUserList(Page<TeamUser> page, Long tid, String nickname);
}
