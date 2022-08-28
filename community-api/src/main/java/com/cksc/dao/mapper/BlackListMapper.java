package com.cksc.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.pojo.Blacklist;
import org.apache.ibatis.annotations.Param;

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
    IPage<Blacklist> getUserBlackList(Page<Blacklist> page, @Param("bsUid") Long bsUid, @Param("nickname")String nickname);
}
