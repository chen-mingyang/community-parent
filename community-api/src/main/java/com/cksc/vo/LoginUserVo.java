package com.cksc.vo;

import lombok.Data;

import java.util.Date;

//登录用户信息VO类
@Data
public class LoginUserVo {
    private String id;

    private String account;

    private String nickname;

    private String briefly;

    private String avatar;

    private String email;

    private String wechat;

    private String qq;

    private String phoneNumber;

    private Integer userType;

    private Integer userState;

    private String createDate;

    private String updateTime;

    private String lastLogin;

    //private String id;
    //
    //private String account;
    //
    //private String nickname;
    //
    //private String avatar;
}
