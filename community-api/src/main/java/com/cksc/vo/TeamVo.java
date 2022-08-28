package com.cksc.vo;

import com.cksc.dao.pojo.Category;
import com.cksc.dao.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/19 15:03
 * @description 团队VO
 */
@Data
public class TeamVo {
    private String tid;

    private String tName;

    private String tBriefly;

    private String tHeadshot;

    private String tEmail;

    private Category category;

    private Integer teamState;

    private Date createTime;

    private Date updateTime;

    private UserVo sysUser;
}
