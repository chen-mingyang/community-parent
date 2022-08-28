package com.cksc.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cksc.dao.pojo.Team;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:54
 * @description 团队接口类
 */
public interface TeamMapper extends BaseMapper<Team> {
    /**
     * 根据用户ID、团队名称 查询我的团队列表
     * @param uid
     * @param tName
     * @return
     */
    List<Team> getUserTeamList(Long uid, String tName);
}
