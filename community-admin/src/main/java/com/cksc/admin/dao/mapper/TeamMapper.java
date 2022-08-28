package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cksc.admin.dao.pojo.Team;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/18 22:54
 * @description 团队接口类
 */
public interface TeamMapper extends BaseMapper<Team> {
    /**
     * 根据有无团队名称 查询团队列表
     * @param tName
     * @return
     */
    List<Team> getTeamList(String tName);
}
