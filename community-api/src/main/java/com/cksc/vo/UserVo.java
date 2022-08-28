package com.cksc.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private String id;

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

    private String createDate;

    private String updateTime;

    private String lastLogin;

    private Integer likeCounts;

    private Integer upCounts;

    private Integer focusCounts;
}
