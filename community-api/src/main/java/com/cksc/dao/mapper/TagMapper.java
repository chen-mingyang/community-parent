package com.cksc.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.pojo.Tag;


import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id 在文章-标签表中 查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热的标签 前n条
     * @param limit
     * @return
     */
    //查询 根据tag_id 分组 计数，从大到小 排列 取前 limit个
    List<Long> findHotsTagIds(int limit);

    //根据标签ID列表 查询热门标签
    List<Tag> findTagsByTagIds(List<Long> tagIds);

    //根据用户ID列表、标签名称 查询标签列表分页
    IPage<Tag> getUserTagList(Page<Tag> page, Long uid, String tagName);

}
