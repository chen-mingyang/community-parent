package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.Like;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.params.LikeParam;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 18:37
 * @description 喜欢服务类
 */
public interface LikeService {
    /**
     * 根据用户ID查询 用户得到的喜欢数
     * @param id
     * @return
     */
    int findLikeCountsById (Long id);

    /**
     * 个人主页-收藏管理 收藏列表
     * @param likeParam
     * @return
     */
    Result getUserLikeList(LikeParam likeParam);
    /**
     * 个人主页-收藏管理-删除收藏
     * @param liId
     * @return
     */
    Result deleteUserLikeById(Long liId);
    /**
     * 通过uid、文章id  查询浏览者对文章的收藏状态
     * @param like
     * @return
     */
    Result queryLikeState(Like like);
    /**
     * 通过uid、文章id  添加收藏信息
     * @param like
     * @return
     */
    Result addLike(Like like);
    /**
     * 通过uid、文章id  删除收藏信息
     * @param like
     * @return
     */
    Result deleteLike(Like like);
}
