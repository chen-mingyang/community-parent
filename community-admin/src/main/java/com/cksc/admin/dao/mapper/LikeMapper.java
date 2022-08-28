package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Like;


/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 19:01
 * @description 喜欢接口类
 */
public interface LikeMapper extends BaseMapper<Like> {
    /**
     * 根据用户ID查询 用户得到的喜欢数
     * @param id
     * @return
     */
    int findLikeCountsById(Long id);

    /**
     * 个人主页-收藏管理 收藏列表
     * @param page
     * @param uid
     * @param title
     * @return
     */
    IPage<Like> getUserLikeList(Page<Like> page, Long uid, String title);

}
