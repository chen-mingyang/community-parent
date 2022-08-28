package com.cksc.vo;

import com.cksc.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 20:53
 * @description 团队成员VO类
 */
@Data
public class TeamUserVo {
    private String tuId;

    private String tid;

    private UserVo sysUser;

    private Integer tuType;

    private Integer tuState;

    private Date createTime;

    private Date updateTime;
}
