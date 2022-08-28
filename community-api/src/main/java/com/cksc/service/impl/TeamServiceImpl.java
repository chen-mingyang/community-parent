package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cksc.dao.mapper.CategoryMapper;
import com.cksc.dao.mapper.SysUserMapper;
import com.cksc.dao.mapper.TeamMapper;
import com.cksc.dao.mapper.TeamUserMapper;
import com.cksc.dao.pojo.Category;
import com.cksc.dao.pojo.SysUser;
import com.cksc.dao.pojo.Team;
import com.cksc.dao.pojo.TeamUser;
import com.cksc.service.TeamService;
import com.cksc.vo.Result;
import com.cksc.vo.TeamUserVo;
import com.cksc.vo.TeamVo;
import com.cksc.vo.UserVo;
import com.cksc.vo.params.TeamParam;
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
 * @date 2022/3/18 22:54
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

    @Autowired private TeamUserMapper teamUserMapper;

    //TeamUser 转移到 TeamUserVo
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


        ////这里返回UserVo 里面ID为String类型，所以不丢失精度
        //teamVo.setSysUser(sysUser);
        UserVo userVo = new UserVo();
        //对象复制
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        teamVo.setSysUser(userVo);

        teamVo.setTid(String.valueOf(team.getTid()));
        return teamVo;
    }
    //List<TeamUser> 转移到 List<TeamUserVo>
    public List<TeamVo> copyList(List<Team> teamList){
        List<TeamVo> teamVoList = new ArrayList<>();
        //循环转换对象
        for (Team team : teamList) {
            teamVoList.add(copy(team));
        }
        return teamVoList;
    }

    @Override
    public Team findTeamById(Long tid) {
        Team team = teamMapper.selectById(tid);
        return team;
    }

    @Override
    public Team findTeamByName(String tname) {
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Team::getTName,tname);
        Team team = teamMapper.selectOne(queryWrapper);
        return team;
    }

    @Override
    public Result getTeamByUid(Long uid) {
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Team::getUid,uid);
        Team team = teamMapper.selectOne(queryWrapper);
        TeamVo teamVo=copy(team);
        return Result.success(teamVo);
    }

    @Override
    public Result createTeam(TeamParam teamParam) {
        Team team=new Team();

        team.setUid(teamParam.getUid());
        team.setTName(teamParam.getTName());
        team.setTHeadshot(teamParam.getTHeadshot());
        team.setTBriefly(teamParam.getTBriefly());
        team.setTEmail(teamParam.getTEmail());
        team.setTeamType(teamParam.getCategory().getId());
        team.setTeamState(1);
        team.setCreateTime(new Date());
        team.setUpdateTime(new Date());
        teamMapper.insert(team);

        Long tid=team.getTid();
        TeamUser teamUser=new TeamUser();
        teamUser.setUid(team.getUid());
        teamUser.setTid(tid);
        teamUser.setTuType(1);
        teamUser.setTuState(1);
        teamUser.setCreateTime(new Date());
        teamUser.setUpdateTime(new Date());
        teamUserMapper.insert(teamUser);

        return Result.success("创建成功");
    }

    @Override
    public Result updateTeam(TeamParam teamParam) {
        Team team=new Team();

        team.setTid(teamParam.getTid());
        team.setUid(teamParam.getUid());
        team.setTName(teamParam.getTName());
        team.setTHeadshot(teamParam.getTHeadshot());
        team.setTBriefly(teamParam.getTBriefly());
        team.setTEmail(teamParam.getTEmail());
        team.setTeamType(teamParam.getCategory().getId());
        team.setTeamState(1);
        team.setCreateTime(new Date());
        team.setUpdateTime(new Date());
        teamMapper.updateById(team);
        return Result.success("编辑成功");
    }

    @Override
    public Result getUserTeamList(TeamParam teamParam) {
        List<Team> teamList=teamMapper.getUserTeamList(teamParam.getUid(),teamParam.getTName());
        List<TeamVo> teamVoLisT=copyList(teamList);
        return Result.success(teamVoLisT);
    }

    @Override
    public Result deleteUserTeamById(TeamParam teamParam) {
        //1 通过tid查询团队信息
        //2 如果团队信息的uid和传过来的uid相等 删除的是自己创建的团队 反之是我参与的团队
        Team team = teamMapper.selectById(teamParam.getTid());
        if (team.getUid().equals(teamParam.getUid())){
            HashMap<String,Object> map = new HashMap<>();
            map.put("tid",team.getTid());
            teamUserMapper.deleteByMap(map);

            //没有级联 所以要删除一系列的文章、问题

            teamMapper.deleteById(team.getTid());
        }else{
            HashMap<String,Object> map = new HashMap<>();
            map.put("uid",team.getUid());
            map.put("tid",team.getTid());
            teamUserMapper.deleteByMap(map);
        }
        return Result.success("删除成功");
    }

    @Override
    public Result getTeamByTid(Long tid) {
        Team team = teamMapper.selectById(tid);
        TeamVo teamVo =copy(team);
        return Result.success(teamVo);
    }
}
