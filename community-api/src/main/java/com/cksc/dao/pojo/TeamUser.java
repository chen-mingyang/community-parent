package com.cksc.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class TeamUser {
    @TableId(type = IdType.ASSIGN_ID)
    private Long tuId;

    private Long tid;

    private Long uid;

    private Integer tuType;

    private Integer tuState;

    private Date createTime;

    private Date updateTime;
}