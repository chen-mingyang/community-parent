package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.UserVo;
import com.cksc.admin.vo.params.UserParam;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    /**
     * 根据账号 密码查询用户信息
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);
    /**
     * 根据Id查询用户信息
     * @param userId
     * @return
     */
    SysUser findUserById(Long userId);
    /**
     * 通过有无nickname 获取用户管理列表
     * @param userParam
     * @return
     */
    Result getUserList(UserParam userParam);
    /**
     * 通过用户ID 删除用户
     * @param id
     * @return
     */
    Result deleteUserById(Long id);
    /**
     * 通过表单信息 授权用户
     * @param userParam
     * @return
     */
    Result changeUserType(UserParam userParam);
    /**
     * 通过表单信息 改变用户状态
     * @param userParam
     * @return
     */
    Result changeUserState(UserParam userParam);
}
