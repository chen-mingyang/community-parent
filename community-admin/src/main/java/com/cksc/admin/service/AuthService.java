//package com.cksc.admin.service;
//
//
//import com.cksc.admin.dao.pojo.Admin;
//import com.cksc.admin.dao.pojo.Permission;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
///**
// * 认证授权类
// */
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private AdminService adminService;
//
//    /**
//     * 认证注解类 HttpServletRequest获取请求、Authentication获取认证
//     * @param request
//     * @param authentication
//     * @return
//     */
//    public boolean auth(HttpServletRequest request, Authentication authentication){
//        //权限认证
//        //获取请求路径
//        String requestURI = request.getRequestURI();
//        //获取当前的用户信息
//        Object principal = authentication.getPrincipal();
//        //判断是否为空，是否为匿名用户
//        if (principal == null || "anonymousUser".equals(principal)){
//            //未登录
//            return false;
//        }
//        //拿到强转德用户
//        UserDetails userDetails = (UserDetails) principal;
//        //拿到用户名
//        String username = userDetails.getUsername();
//        //通过用户名查询用户信息
//        Admin admin = adminService.findAdminByUsername(username);
//        if (admin == null){
//            return  false;
//        }
//        if (1 == admin.getId()){
//            //超级管理员
//            return true;
//        }
//        Long id = admin.getId();
//        //多表嵌套查询 通过管理员ID 先查询管理员-权限关系表得到权限ID列表，最后查询拿到权限信息列表
//        List<Permission> permissionList = this.adminService.findPermissionByAdminId(id);
//        //注意处理问号请求路径，得到问号前不带参数的路径
//        requestURI = StringUtils.split(requestURI,'?')[0];
//        //循环操作
//        for (Permission permission : permissionList) {
//            //判断请求的路径，是否在用户的权限路径范围中
//            if (requestURI.equals(permission.getPath())){
//                return true;
//            }
//        }
//        return false;
//    }
//}
