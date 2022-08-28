//package com.cksc.admin.service;
//
//
//import com.cksc.admin.dao.pojo.Admin;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
///**
// * 用户登录认证服务类
// */
//
//@Component
//public class SecurityUserService implements UserDetailsService {
//
//    @Autowired
//    private AdminService adminService;
//
//    /**
//     * 登录认证功能 继承security的用户详情服务 抛出UsernameNotFoundException异常
//     * @param username
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //登录的时候，会把username 传递到这里
//        //通过username查询 admin表，如果 admin存在 将密码告诉spring security
//        //如果不存在 返回null 认证失败了
//        Admin admin = this.adminService.findAdminByUsername(username);
//        if (admin == null){
//            //登录失败
//            return null;
//        }
//        //将密码传到spring security，在security认证中验证用户的密码 new Security下的User
//        UserDetails userDetails = new User(username,admin.getPassword(),new ArrayList<>());
//        return userDetails;
//    }
//}
