package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.SysUserMapper;
import com.cksc.dao.mapper.TeamUserMapper;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.SysUser;
import com.cksc.dao.pojo.Team;
import com.cksc.dao.pojo.TeamUser;
import com.cksc.service.TeamService;
import com.cksc.service.TeamUserService;
import com.cksc.vo.*;
import com.cksc.vo.params.TeamParam;
import com.cksc.vo.params.TeamUserParam;
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
 * @date 2022/3/18 22:41
 * @description 团队成员服务实现类
 */
@Service
public class TeamUserServiceImpl implements TeamUserService {
    @Autowired
    private TeamUserMapper teamUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TeamService teamService;

    //TeamUser 转移到 TeamUserVo
    public TeamUserVo copy(TeamUser teamUser){
        TeamUserVo teamUserVo = new TeamUserVo();
        BeanUtils.copyProperties(teamUser,teamUserVo);
        //查询用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId,teamUser.getUid());
        queryWrapper.select(SysUser::getId,SysUser::getNickname);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);


        //这里返回UserVo 里面ID为String类型，所以不丢失精度
        //teamUserVo.setSysUser(sysUser);
        UserVo userVo = new UserVo();
        //对象复制
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        teamUserVo.setSysUser(userVo);

        teamUserVo.setTid(String.valueOf(teamUser.getTid()));
        teamUserVo.setTuId(String.valueOf(teamUser.getTuId()));
        return teamUserVo;
    }
    //List<TeamUser> 转移到 List<TeamUserVo>
    public List<TeamUserVo> copyList(List<TeamUser> teamUserList){
        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        //循环转换对象
        for (TeamUser teamUser : teamUserList) {
            teamUserVoList.add(copy(teamUser));
        }
        return teamUserVoList;
    }


    @Override
    public Result getUserTeamState(Long uid) {
        LambdaQueryWrapper<TeamUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamUser::getUid,uid);
        Integer count = teamUserMapper.selectCount(queryWrapper);
        return Result.success(count);
    }

    @Override
    public Result getTeamUserList(TeamUserParam teamUserParam) {
        TeamUserListVo teamUserListVo = new TeamUserListVo();
        //Mybatis-plus分页
        Page<TeamUser> page = new Page<>(teamUserParam.getPage(),teamUserParam.getPageSize());
        //apType=1 文章
        IPage<TeamUser> focusIPage = teamUserMapper.getTeamUserList(
                page,
                teamUserParam.getTid(),
                teamUserParam.getNickname());
        List<TeamUser> records = focusIPage.getRecords(); //在分页中得到List<TeamUser>

        teamUserListVo.setTotal((int) focusIPage.getTotal());

        List<TeamUserVo> teamUserVoList = copyList(records);
        teamUserListVo.setTeamUserVoList(teamUserVoList);
        //返回VO
        return Result.success(teamUserListVo);
    }

    @Override
    public Result deleteTeamUserBytuId(Long tuId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("tu_id",tuId);
        teamUserMapper.deleteByMap(map);
        return Result.success("删除成功");

    }

    @Override
    public Result addTeamUser(TeamUser teamUser) {

        teamUser.setTuType(2);
        teamUser.setTuState(1);
        teamUser.setCreateTime(new Date());
        teamUser.setUpdateTime(new Date());
        teamUserMapper.insert(teamUser);
        return Result.success("新增成功");
    }

    @Override
    public Result addTeam(TeamParam teamParam) {
        //team 只有uid、tname 先得到team 再验证是否已经加入
        Team team1=teamService.findTeamByName(teamParam.getTName());
        if (team1 == null){
            Result.fail(404,"没有找到此团队");
        }
        LambdaQueryWrapper<TeamUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamUser::getUid,teamParam.getUid());
        queryWrapper.eq(TeamUser::getTid,team1.getTid());
        Integer count = teamUserMapper.selectCount(queryWrapper);
        if (count != 0){
            Result.fail(404,"已加入此团队");
        }

        TeamUser teamUser=new TeamUser();
        teamUser.setTid(team1.getTid());
        teamUser.setUid(teamParam.getUid());
        teamUser.setTuType(2);
        teamUser.setTuState(1);
        teamUser.setCreateTime(new Date());
        teamUser.setUpdateTime(new Date());
        teamUserMapper.insert(teamUser);

        return Result.success("新增成功");
    }


}
