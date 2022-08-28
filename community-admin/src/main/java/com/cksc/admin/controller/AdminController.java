//package com.cksc.admin.controller;
//
//
//import com.cksc.admin.vo.params.PageParam;
//import com.cksc.admin.dao.pojo.Permission;
//import com.cksc.admin.service.PermissionService;
//import com.cksc.admin.vo.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("admin")
//public class AdminController {
//
//    @Autowired
//    private PermissionService permissionService;
//
//    /**
//     * 权限列表信息分页 包括了查询queryString
//     * @param pageParam
//     * @return
//     */
//    @PostMapping("permission/permissionList")
//    public Result listPermission(@RequestBody PageParam pageParam){
//        return permissionService.listPermission(pageParam);
//    }
//
//    /**
//     * 增加权限
//     * @param permission
//     * @return
//     */
//    @PostMapping("permission/add")
//    public Result add(@RequestBody Permission permission){
//        return permissionService.add(permission);
//    }
//
//    /**
//     * 更新权限
//     * @param permission
//     * @return
//     */
//    @PostMapping("permission/update")
//    public Result update(@RequestBody Permission permission){
//        return permissionService.update(permission);
//    }
//
//    /**
//     * 根据ID删除权限
//     * @param id
//     * @return
//     */
//    @GetMapping("permission/delete/{id}")
//    public Result delete(@PathVariable("id") Long id){
//        return permissionService.delete(id);
//    }
//}
