package com.cksc.utils;


import com.cksc.dao.pojo.SysUser;

/**
 * 本地线程保存用户信息
 */

public class UserThreadLocal {

    //声明私有的类，不能在其他地方new出来
    private UserThreadLocal(){}
    //线程变量隔离 开启一个线程 把用户信息存到线程中 线程安全的一个解决方法
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();
    //放入
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    //取出来
    public static SysUser get(){
        return LOCAL.get();
    }
    //删除
    public static void remove(){
        LOCAL.remove();
    }
}
