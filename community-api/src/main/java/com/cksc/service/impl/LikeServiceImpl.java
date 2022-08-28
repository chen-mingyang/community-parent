package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cksc.dao.mapper.ArticleMapper;
import com.cksc.dao.mapper.LikeMapper;
import com.cksc.dao.pojo.*;
import com.cksc.service.LikeService;
import com.cksc.vo.*;
import com.cksc.vo.params.LikeParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 18:41
 * @description 喜欢服务实现类
 */

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private ArticleMapper articleMapper;

    //Tag 转移到 TagVo
    public LikeVo copy(Like like){
        LikeVo likeVo = new LikeVo();
        BeanUtils.copyProperties(like,likeVo);
        //查询文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,like.getApId());
        queryWrapper.select(Article::getId,Article::getTitle);
        Article article = articleMapper.selectOne(queryWrapper);
        likeVo.setArticle(article);
        likeVo.setLiId(String.valueOf(like.getLiId()));
        return likeVo;
    }
    //List<Tag> 转移到 List<TagVo>
    public List<LikeVo> copyList(List<Like> likeList){
        List<LikeVo> likeVoList = new ArrayList<>();
        //循环转换对象
        for (Like like : likeList) {
            likeVoList.add(copy(like));
        }
        return likeVoList;
    }

    @Override
    public int findLikeCountsById(Long id) {
        int likeCounts=likeMapper.findLikeCountsById(id);
        return likeCounts;
    }

    @Override
    public Result getUserLikeList(LikeParam likeParam) {
        UserLikeVo userLikeVo = new UserLikeVo();
        //Mybatis-plus分页
        Page<Like> page = new Page<>(likeParam.getPage(),likeParam.getPageSize());
        //apType=1 文章
        IPage<Like> articleIPage = likeMapper.getUserLikeList(
                page,
                likeParam.getUid(),
                likeParam.getTitle());
        List<Like> records = articleIPage.getRecords(); //在分页中得到List<Like>

        userLikeVo.setTotal((int) articleIPage.getTotal());

        List<LikeVo> likeVoList = copyList(records);
        userLikeVo.setLikeList(likeVoList);
        //返回VO
        return Result.success(userLikeVo);
    }

    @Override
    public Result deleteUserLikeById(Long liId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("li_id",liId);
        likeMapper.deleteByMap(map);

        //删除后要更新文章的收藏数
        return Result.success("删除成功");
    }

    @Override
    public Result queryLikeState(Like like) {
        LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getUid,like.getUid());
        queryWrapper.eq(Like::getApId,like.getApId());
        int count = likeMapper.selectCount(queryWrapper);
        return Result.success(count);
    }

    @Override
    public Result addLike(Like like) {
        //添加点赞
        like.setCreateTime(new Date());
        likeMapper.insert(like);

        //更新文章 点喜欢数
        Article article = articleMapper.selectById(like.getApId());
        int likeCounts = article.getLikeCounts();
        article.setLikeCounts(likeCounts+1);

        //可以丢到线程池中 异步进行 加乐观锁防止 出现时间不同 异常新增
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,like.getApId());
        updateWrapper.eq(Article::getLikeCounts,likeCounts);
        articleMapper.update(article,updateWrapper);

        return Result.success("新增成功");
    }

    @Override
    public Result deleteLike(Like like) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("uid",like.getUid());
        map.put("ap_id",like.getApId());
        likeMapper.deleteByMap(map);

        //更新文章 点赞数
        Article article = articleMapper.selectById(like.getApId());
        int likeCounts = article.getLikeCounts();
        article.setUpCounts(likeCounts-1);

        //可以丢到线程池中 异步进行 加乐观锁
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,like.getApId());
        updateWrapper.eq(Article::getLikeCounts,likeCounts);
        articleMapper.update(article,updateWrapper);

        return Result.success("删除成功");
    }
}
