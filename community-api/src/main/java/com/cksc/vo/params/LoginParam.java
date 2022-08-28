package com.cksc.vo.params;

import lombok.Data;


//登录参数
@Data //自动生成简化SET和GET
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
