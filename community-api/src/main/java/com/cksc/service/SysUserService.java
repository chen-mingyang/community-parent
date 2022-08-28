package com.cksc.service;


import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.SysUser;
import com.cksc.vo.Result;
import com.cksc.vo.UserVo;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    //根据ID查询用户信息
    SysUser findUserById(Long id);

    //根据账号 密码查询用户信息
    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据ID查询用户信息 个人主页
     * @param id
     * @return
     */
    Result getUserById(Long id);

    /**
     * 通过传来的用户信息表单 更新用户信息
     * @param sysUser
     * @return
     */
    Result updateUser(SysUser sysUser);

    /**
     * 通过文章id 获取作者用户信息
     * @param id
     * @return
     */
    Result getUserByApId(Long id);

    /**
     * 通过发起id、被关注id  查询浏览者对文章作者的关注状态
     * @param focus
     * @return
     */
    Result queryFocusState(Focus focus);

    /**
     * 通过发起id、被关注id  添加关注信息
     * @param focus
     * @return
     */
    Result addFocusState(Focus focus);

    /**
     * 通过发起id、被关注id  删除关注信息
     * @param focus
     * @return
     */
    Result deleteFocusUser(Focus focus);
}
