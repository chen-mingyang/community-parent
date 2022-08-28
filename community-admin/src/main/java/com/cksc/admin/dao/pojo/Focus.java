package com.cksc.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Focus {
    @TableId(type = IdType.ASSIGN_ID)
    private Long fid;

    private Long fsUid;

    private Long frUid;

    private Date createTime;


}