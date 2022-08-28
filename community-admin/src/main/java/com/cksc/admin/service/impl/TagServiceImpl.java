package com.cksc.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.admin.dao.mapper.TagMapper;
import com.cksc.admin.dao.pojo.SysUser;
import com.cksc.admin.dao.pojo.Tag;
import com.cksc.admin.service.SysUserService;
import com.cksc.admin.service.TagService;
import com.cksc.admin.vo.PageResult;
import com.cksc.admin.vo.Result;
import com.cksc.admin.vo.TagVo;
import com.cksc.admin.vo.params.TagPageParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SysUserService sysUserService;

    //Tag 转移到 TagVo
    public TagVo copy(Tag tag){
        SysUser sysUser = sysUserService.findUserById(tag.getUid());
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setCreateTime(tag.getCreateTime());
        tagVo.setId(String.valueOf(tag.getId()));
        tagVo.setSysUser(sysUser);
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

    @Override
    public Result getTagList(TagPageParam tagPageParam) {

        PageResult pageResult = new PageResult();
        //Mybatis-plus分页
        Page<Tag> page = new Page<>(tagPageParam.getPage(),tagPageParam.getPageSize());
        //apType=1 文章
        IPage<Tag> articleIPage = tagMapper.getTagList(page,tagPageParam.getTagName());

        List<Tag> records = articleIPage.getRecords(); //在分页中得到List<Article>
        pageResult.setTotal((int) articleIPage.getTotal());
        List<TagVo> tagVoList = copyList(records);

        pageResult.setList(tagVoList);
        //返回VO
        return Result.success(pageResult);
    }

    @Override
    public Result deleteTagById(Long id) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        //注意 删除 标签-文章问题表中的关联
        tagMapper.deleteByMap(map);
        return Result.success("删除成功");
    }

    @Override
    public Result addTag(Tag tag) {
        //从token中获取当前用户ID
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

    @Override
    public Result changeTagState(Tag tag) {
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        return Result.success("改变成功");
    }


}
