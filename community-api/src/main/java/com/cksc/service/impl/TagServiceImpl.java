package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.TagMapper;
import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.ArticleBody;
import com.cksc.dao.pojo.Tag;
import com.cksc.service.TagService;
import com.cksc.vo.*;
import com.cksc.vo.params.TagPageParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    //Tag 转移到 TagVo
    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setCreateTime(tag.getCreateTime());
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    //List<Tag> 转移到 List<TagVo>
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        //循环转换对象
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    //根据文章id 在文章-标签表中 查询标签列表
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybatisplus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    /**
     * 1. 标签所拥有的文章数量最多 最热标签
     * 2. 查询 根据tag_id 分组 计数，从大到小 排列 取前 limit个
     */
    @Override
    public Result hots(int limit) {
        //查询 根据tag_id 分组 计数，从大到小 排列 取前 limit个
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }

        //根据标签ID列表 查询热门标签
        //select * from tag where id in (1,2,3,4)
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        queryWrapper.eq(Tag::getState,1);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        //转换成VO对象，方便代码拓展
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getState,1);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    @Override
    public Result getUserTagList(TagPageParam tagPageParam) {

        UserTagVo userTagVo = new UserTagVo();
        //Mybatis-plus分页
        Page<Tag> page = new Page<>(tagPageParam.getPage(),tagPageParam.getPageSize());
        //apType=1 文章
        IPage<Tag> articleIPage = tagMapper.getUserTagList(page, tagPageParam.getUid(),tagPageParam.getTagName());
        List<Tag> records = articleIPage.getRecords(); //在分页中得到List<Article>

        userTagVo.setTotal((int) articleIPage.getTotal());

        List<TagVo> tagVoList = copyList(records);
        userTagVo.setTagVoList(tagVoList);
        //返回VO
        return Result.success(userTagVo);
    }

    @Override
    public Result deleteUserTagById(Long id) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        tagMapper.deleteByMap(map);
        return Result.success("删除成功");
    }

    @Override
    public Result addTag(Tag tag) {
        tag.setCreateTime(new Date());
        tag.setState(1);
        tagMapper.insert(tag);
        return Result.success("新增成功");
    }

    @Override
    public Result updateTag(Tag tag) {
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        return Result.success("编辑成功");
    }


}
