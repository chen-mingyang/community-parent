package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cksc.admin.dao.pojo.ThumbsUp;


/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 19:14
 * @description 点赞接口类
 */
public interface ThumbsUpMapper  extends BaseMapper<ThumbsUp> {
    /**
     * 根据用户ID查询 用户得到的点赞数
     * @param id
     * @return
     */
    int findThumbsUpCountsById(Long id);
}