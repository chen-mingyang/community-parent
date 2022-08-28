package com.cksc.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Like {
    @TableId(type = IdType.ASSIGN_ID)
    private Long liId;

    private Long apId;

    private Long uid;

    private Date createTime;
}