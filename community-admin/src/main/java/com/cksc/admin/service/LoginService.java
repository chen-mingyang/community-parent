package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.LoginParam;

//@Transactional //事务注解 加上保证注册程序出错 有数据回滚，理论上加在service接口处
public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    ///**
    // * 检测token
    // * @param token
    // * @return
    // */
    //SysUser checkToken(String token);
    //
    ///**
    // * 退出登录
    // * @param token
    // * @return
    // */
    //Result logout(String token);
    ///**
    // * 注册
    // * @param loginParam
    // * @return
    // */
    //Result register(LoginParam loginParam);



}
