package com.cksc.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;


@Data
public class Team {
    @TableId(type = IdType.ASSIGN_ID)
    private Long tid;

    private String tName;

    private String tBriefly;

    private String tHeadshot;

    private String tEmail;

    private Long teamType;

    private Integer teamState;

    private Date createTime;

    private Date updateTime;

    private Long uid;
}