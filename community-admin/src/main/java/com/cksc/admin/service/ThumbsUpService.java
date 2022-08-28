package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.ThumbsUp;
import com.cksc.admin.vo.Result;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 18:38
 * @description 点赞服务类
 */
public interface ThumbsUpService {
    /**
     * 根据用户ID查询 用户得到的点赞数
     * @param id
     * @return
     */
    int findThumbsUpCountsById(Long id);
    /**
     * 通过uid、文章id  查询浏览者对文章的点赞状态
     * @param thumbsUp
     * @return
     */
    Result queryThumbsUpState(ThumbsUp thumbsUp);
    /**
     * 通过uid、文章id  添加点赞信息
     * @param thumbsUp
     * @return
     */
    Result addThumbsUp(ThumbsUp thumbsUp);
    /**
     * 通过uid、文章id  删除点赞信息
     * @param thumbsUp
     * @return
     */
    Result deleteThumbsUp(ThumbsUp thumbsUp);
}
