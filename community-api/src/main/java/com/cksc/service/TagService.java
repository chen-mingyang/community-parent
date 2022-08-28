package com.cksc.service;



import com.cksc.dao.pojo.Tag;
import com.cksc.vo.Result;
import com.cksc.vo.TagVo;
import com.cksc.vo.params.TagPageParam;

import java.util.List;

public interface TagService {

    /**
     * 根据文章id 在文章-标签表中 查询标签列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);
    /**
     * 查询热门标签
     * @param limit
     * @return
     */
    Result hots(int limit);
    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

    /**
     * 查询所有标签-详情
     * @return
     */
    Result findAllDetail();

    /**
     * 根据ID查询特定标签信息详情
     * @param id
     * @return
     */
    Result findDetailById(Long id);

    /**
     * 获取标签列表分页信息 传递参数
     * @param tagPageParam
     * @return
     */
    Result getUserTagList(TagPageParam tagPageParam);
    /**
     * 个人主页-标签管理 删除问标签 根据用户ID
     * @param id
     * @return
     */
    Result deleteUserTagById(Long id);
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
}
