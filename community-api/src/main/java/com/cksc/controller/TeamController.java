package com.cksc.controller;

import com.cksc.common.aop.LogAnnotation;
import com.cksc.dao.pojo.Team;
import com.cksc.dao.pojo.TeamUser;
import com.cksc.service.TeamUserService;
import com.cksc.vo.Result;
import com.cksc.vo.params.TeamParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/20 18:50
 * @description 团队控制类
 */

@RestController
@RequestMapping("team")
public class TeamController {
    @Autowired
    private TeamUserService teamUserService;
    /**
     * 通过表单信息 加入团队 根据uid 或 名称
     * @param teamParam
     * @return
     */
    @PostMapping("addTeam")
    @ApiOperation(value="加入团队",notes = "通过表单信息 ")
    @LogAnnotation(module="加入团队",operator="通过表单信息")
    public Result addTeam(@RequestBody TeamParam teamParam){
        return teamUserService.addTeam(teamParam);
    }

}
