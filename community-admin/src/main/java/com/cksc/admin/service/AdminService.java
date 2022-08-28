//package com.cksc.admin.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//import com.cksc.admin.dao.mapper.AdminMapper;
//import com.cksc.admin.dao.pojo.Admin;
//import com.cksc.admin.dao.pojo.Permission;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AdminService {
//
//    @Autowired
//    private AdminMapper adminMapper;
//
//    /**
//     * 根据用户名查询用户
//     * @param username
//     * @return
//     */
//    public Admin findAdminByUsername(String username){
//        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Admin::getUsername,username);
//        queryWrapper.last("limit 1");
//        Admin admin = (Admin) adminMapper.selectOne(queryWrapper);
//        return admin;
//    }
//
//    /**
//     * 多表嵌套查询 通过管理员ID 先查询管理员-权限关系表得到权限ID列表，最后查询拿到权限信息列表
//     * @param adminId
//     * @return
//     */
//    public List<Permission> findPermissionByAdminId(Long adminId) {
//        //SELECT * FROM `ms_permission` where id in (select permission_id from ms_admin_permission where admin_id=1)
//        return adminMapper.findPermissionByAdminId(adminId);
//    }
//}
