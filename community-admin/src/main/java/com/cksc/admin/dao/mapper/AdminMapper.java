//package com.cksc.admin.dao.mapper;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//
//import com.cksc.admin.dao.pojo.Admin;
//import com.cksc.admin.dao.pojo.Permission;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//
//public interface AdminMapper extends BaseMapper<Admin> {
//
//    /**
//     * 多表嵌套查询 通过管理员ID 先查询管理员-权限关系表得到权限ID列表，最后查询拿到权限信息列表
//     * @param adminId
//     * @return
//     */
//    @Select("SELECT * FROM ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{adminId})")
//    List<Permission> findPermissionByAdminId(Long adminId);
//}
