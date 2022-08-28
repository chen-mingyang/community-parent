package com.cksc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cksc.dao.mapper.ArticleMapper;
import com.cksc.dao.mapper.ThumbsUpMapper;
import com.cksc.dao.pojo.Article;
import com.cksc.dao.pojo.Focus;
import com.cksc.dao.pojo.ThumbsUp;
import com.cksc.service.ThumbsUpService;
import com.cksc.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/15 19:22
 * @description 点赞服务实现类
 */
@Service
public class ThumbsUpServiceImpl implements ThumbsUpService {
    @Autowired
    private ThumbsUpMapper thumbsUpMapper;
    @Autowired
    private ArticleMapper articleMapper;  //文章问题公用

    @Override
    public int findThumbsUpCountsById(Long id) {
        int upCounts = thumbsUpMapper.findThumbsUpCountsById(id);
        return upCounts;
    }

    @Override
    public Result queryThumbsUpState(ThumbsUp thumbsUp) {
        LambdaQueryWrapper<ThumbsUp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThumbsUp::getUid,thumbsUp.getUid());
        queryWrapper.eq(ThumbsUp::getApId,thumbsUp.getApId());
        int count = thumbsUpMapper.selectCount(queryWrapper);
        return Result.success(count);
    }

    @Override
    public Result addThumbsUp(ThumbsUp thumbsUp) {
        //添加点赞
        thumbsUp.setCreateTime(new Date());
        thumbsUpMapper.insert(thumbsUp);

        //更新文章 点赞数
        Article article = articleMapper.selectById(thumbsUp.getApId());
        //注意 发布文章时 让这些互动数据为0
        int UpCounts = article.getUpCounts();
        article.setUpCounts(UpCounts+1);

        //可以丢到线程池中 异步进行 加乐观锁
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,thumbsUp.getApId());
        updateWrapper.eq(Article::getUpCounts,UpCounts);
        articleMapper.update(article,updateWrapper);

        return Result.success("新增成功");
    }

    @Override
    public Result deleteThumbsUp(ThumbsUp thumbsUp) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("uid",thumbsUp.getUid());
        map.put("ap_id",thumbsUp.getApId());
        thumbsUpMapper.deleteByMap(map);

        //更新文章 点赞数
        Article article = articleMapper.selectById(thumbsUp.getApId());
        int UpCounts = article.getUpCounts();
        article.setUpCounts(UpCounts-1);

        //可以丢到线程池中 异步进行 加乐观锁
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,thumbsUp.getApId());
        updateWrapper.eq(Article::getUpCounts,UpCounts);
        articleMapper.update(article,updateWrapper);

        return Result.success("删除成功");
    }
}
