//package com.cksc.admin.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//
//import com.cksc.admin.dao.mapper.PermissionMapper;
//import com.cksc.admin.vo.params.PageParam;
//import com.cksc.admin.dao.pojo.Permission;
//import com.cksc.admin.vo.PageResult;
//import com.cksc.admin.vo.Result;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class PermissionService {
//    @Autowired
//    private PermissionMapper permissionMapper;
//
//    /**
//     * 权限列表信息分页
//     * @param pageParam
//     * @return
//     */
//    public Result listPermission(PageParam pageParam) {
//        /**
//         * 要的数据，管理台 表的所有的字段 Permission
//         * 分页查询
//         */
//
//        //传入当前页数、每页多少数
//        Page<Permission> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
//        //创建查询条件
//        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(pageParam.getQueryString())){
//            queryWrapper.eq(Permission::getName,pageParam.getQueryString());
//        }
//        //查询分页
//        Page<Permission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
//        //分页返回结果-list、total
//        PageResult<Permission> pageResult = new PageResult<>();
//        pageResult.setList(permissionPage.getRecords());
//        pageResult.setTotal((int) permissionPage.getTotal());
//        return Result.success(pageResult);
//    }
//    /**
//     * 增加权限
//     * @param permission
//     * @return
//     */
//    public Result add(Permission permission) {
//        this.permissionMapper.insert(permission);
//        return Result.success(null);
//    }
//    /**
//     * 更新权限
//     * @param permission
//     * @return
//     */
//    public Result update(Permission permission) {
//        this.permissionMapper.updateById(permission);
//        return Result.success(null);
//    }
//
//    /**
//     * 根据ID删除权限
//     * @param id
//     * @return
//     */
//    public Result delete(Long id) {
//        this.permissionMapper.deleteById(id);
//        return Result.success(null);
//    }
//}
