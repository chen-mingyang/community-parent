package com.cksc.vo;

import com.cksc.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:53
 * @description 团队成员VO类
 */
@Data
public class TeamUserListVo {
    private List<TeamUserVo> teamUserVoList;
    private int total;

}
