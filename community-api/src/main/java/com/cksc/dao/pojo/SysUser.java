package com.cksc.dao.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description 用户类
 */

@Data
public class SysUser {
    //    @TableId(type = IdType.ASSIGN_ID) // 默认id类型
    // 以后 用户多了之后，要进行分表操作，id就需要用分布式id了
    //    @TableId(type = IdType.AUTO) 数据库自增
    private Long id;

    private String account;

    private String password;

    private String nickname;

    private String briefly;

    private String avatar;

    private String email;

    private String blog;

    private String bili;

    private String wechat;

    private String qq;

    private String phoneNumber;

    private Integer userType;

    private Integer userState;

    private Long createDate;

    private Date updateTime;

    private Long lastLogin;

    private String salt;
}
