package com.cksc.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Blacklist {
    @TableId(type = IdType.ASSIGN_ID)
    private Long bid;

    private Long bsUid;

    private Long brUid;

    private Date createTime;
}