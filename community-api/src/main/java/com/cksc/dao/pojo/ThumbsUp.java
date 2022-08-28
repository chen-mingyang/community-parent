package com.cksc.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ThumbsUp {
    @TableId(type = IdType.ASSIGN_ID)
    private Long tuId;

    private Long uid;

    private Long apId;

    private Date createTime;

}