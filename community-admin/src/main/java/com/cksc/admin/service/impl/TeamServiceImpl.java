package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cksc.admin.dao.mapper.CategoryMapper;
import com.cksc.admin.dao.mapper.SysUserMapper;
import com.cksc.admin.dao.mapper.TeamMapper;
import com.cksc.admin.dao.pojo.Category;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Team;
import com.cksc.admin.service.TeamService;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.TeamVo;
import com.cksc.admin.vo.params.TeamParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/23 0:00
 * @description 团队服务实现类
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    //Team 转移到 TeamVo
    public TeamVo copy(Team team){
        TeamVo teamVo = new TeamVo();
        BeanUtils.copyProperties(team,teamVo);

        LambdaQueryWrapper<Category> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Category::getId,team.getTeamType());
        queryWrapper1.select(Category::getId,Category::getCategoryName);
        Category category = categoryMapper.selectOne(queryWrapper1);
        teamVo.setCategory(category);


        //查询用户
        LambdaQueryWrapper<SysUser> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(SysUser::getId,team.getUid());
        queryWrapper2.select(SysUser::getId,SysUser::getNickname);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper2);
        teamVo.setSysUser(sysUser);

        teamVo.setTid(String.valueOf(team.getTid()));
        return teamVo;
    }
    //List<Team> 转移到 List<TeamVo>
    public List<TeamVo> copyList(List<Team> teamList){
        List<TeamVo> teamVoList = new ArrayList<>();
        //循环转换对象
        for (Team team : teamList) {
            teamVoList.add(copy(team));
        }
        return teamVoList;
    }


    @Override
    public Result getTeamList(TeamParam teamParam) {
        List<Team> teamList=teamMapper.getTeamList(teamParam.getTName());
        List<TeamVo> teamVoLisT=copyList(teamList);
        return Result.success(teamVoLisT);
    }

    @Override
    public Result deleteTeamByTid(Long tid) {
        //删除团队 注意删除与团队相关的文章、问题、团队成员表
        HashMap<String,Object> map = new HashMap<>();
        map.put("tid",tid);
        teamMapper.deleteByMap(map);
        return Result.success("删除成功");
    }

    @Override
    public Result changeTeamState(TeamParam teamParam) {
        Team team = new Team();
        team.setTid(teamParam.getTid());
        team.setTeamState(teamParam.getTeamState());
        team.setUpdateTime(new Date());
        teamMapper.updateById(team);
        return Result.success("改变团队状态成功");
    }

    @Override
    public Team findTeamByTid(Long tid) {
        Team team = teamMapper.selectById(tid);
        return team;
    }
}
