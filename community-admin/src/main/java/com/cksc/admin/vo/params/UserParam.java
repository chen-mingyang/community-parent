package com.cksc.admin.vo.params;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/16 0:59
 * @description 用户参数
 */
@Data
public class UserParam {
    private int page; //当前页数1

    private int pageSize; //每页显示数量10

    private Long id;

    private String account;

    private String nickname;

    private String briefly;

    private String avatar;

    private String email;

    private String wechat;

    private String qq;

    private String bili;

    private String blog;

    private String phoneNumber;

    private Integer userType;

    private Integer userState;

    //private Date createDate;
    //
    //private Date updateTime;
    //
    //private Date lastLogin;

    private Integer likeCounts;

    private Integer upCounts;

    private Integer focusCounts;
}
