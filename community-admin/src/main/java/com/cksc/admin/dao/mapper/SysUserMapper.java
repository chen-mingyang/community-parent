package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.SysUser;


public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过有无nickname 获取用户管理列表
     * @param page
     * @param nickname
     * @return
     */
    IPage<SysUser> getUserList(Page<SysUser> page, String nickname);
}
