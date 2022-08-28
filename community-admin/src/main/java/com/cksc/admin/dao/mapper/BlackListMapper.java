package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Blacklist;


/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/17 22:54
 * @description 黑名单接口类
 */
public interface BlackListMapper extends BaseMapper<Blacklist> {
    /**
     * 个人主页-黑名单管理-黑名单列表
     * @param page
     * @param bsUid
     * @param nickname
     * @return
     */
    IPage<Blacklist> getUserBlackList(Page<Blacklist> page, Long bsUid, String nickname);
}
