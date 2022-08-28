package com.cksc.admin.service;


import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.TagVo;
import com.cksc.admin.vo.params.TagPageParam;

import java.util.List;

public interface TagService {

    /**
     * 根据文章id 在文章-标签表中 查询标签列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 获取标签列表分页信息 传递参数
     * @param tagPageParam
     * @return
     */
    Result getTagList(TagPageParam tagPageParam);
    /**
     * 个人主页-标签管理 删除问标签 根据用户ID
     * @param id
     * @return
     */
    Result deleteTagById(Long id);
    /**
     * 通过表单信息 新增标签
     * @param tag
     * @return
     */
    Result addTag(Tag tag);
    /**
     * 通过表单信息 编辑标签 根据标签ID
     * @param tag
     * @return
     */
    Result updateTag(Tag tag);
    /**
     * 通过表单信息 更新标签状态
     * @param tag
     * @return
     */
    Result changeTagState(Tag tag);


}
