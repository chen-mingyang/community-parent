package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Focus;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 19:26
 * @description 关注接口类
 */
public interface FocusMapper extends BaseMapper<Focus> {
    /**
     * 个人主页-关注管理-关注列表
     * @param page
     * @param fsUid
     * @param nickname
     * @return
     */
    IPage<Focus> getUserFocusList(Page<Focus> page, Long fsUid, String nickname);
}
