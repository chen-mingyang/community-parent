package com.cksc.admin.controller;

import com.cksc.admin.common.aop.LogAnnotation;
import com.cksc.admin.common.cache.Cache;
import com.cksc.admin.service.TeamService;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.TeamParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/22 23:47
 * @description 团队控制类
 */
@RestController
@PreAuthorize("hasAnyAuthority('admin','member')") //配置角色，拥有该角色的用户方可访问
@RequestMapping("team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 根据有无团队名称 查询团队列表
     * @param teamParam
     * @return
     */
    @PostMapping("getTeamList")
    @ApiOperation(value="查询团队列表",notes = "根据有无团队名称")
    @LogAnnotation(module="查询团队列表",operator="根据有无团队名称")
    //@Cache(expire = 2 * 60 * 1000,name = "adminTeamList") //aop缓存redis
    public Result getTeamList(@RequestBody TeamParam teamParam){

        return teamService.getTeamList(teamParam);
    }

    /**
     * 通过团队ID 删除团队
     * @param tid
     * @return
     */
    @PostMapping("deleteTeamByTid/{tid}")
    @ApiOperation(value="删除团队",notes = "通过团队ID")
    @LogAnnotation(module="删除团队",operator="通过团队ID")
    public Result deleteTeamByTid(@PathVariable("tid") Long tid){
        return teamService.deleteTeamByTid(tid);
    }

    /**
     * 根据tid,teamState 改变团队状态
     * @param teamParam
     * @return
     */
    @PostMapping("changeTeamState")
    @ApiOperation(value="改变团队状态",notes = "根据tid和teamState")
    @LogAnnotation(module="改变团队状态",operator="根据tid和teamState")
    public Result changeTeamState(@RequestBody TeamParam teamParam){
        return teamService.changeTeamState(teamParam);
    }

}
