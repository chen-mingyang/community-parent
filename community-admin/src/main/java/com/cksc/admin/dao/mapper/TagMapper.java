package com.cksc.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.pojo.Tag;


import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id 在文章-标签表中 查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);


    //根据标签名称 查询标签列表分页
    IPage<Tag> getTagList(Page<Tag> page, String tagName);

}
