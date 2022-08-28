package com.cksc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cksc.dao.mapper.ArticleMapper;
import com.cksc.dao.mapper.ProblemMapper;
import com.cksc.dao.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 线程服务：
 * 1 浏览量计数
 */

@Component
public class ThreadService {

    @Resource
    private ArticleMapper articleMapper;

    @PostConstruct
    public void initViewCount(){
        //为了 保证 启动项目的时候，redis中的浏览量 如果没有，读取数据库的数据，进行初始化
        //便于更新的时候 自增
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<>());
        for (Article article : articles) {
            String viewCountStr = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(article.getId()));
            if (viewCountStr == null){
                //初始化
                redisTemplate.opsForHash().put("view_count", String.valueOf(article.getId()),String.valueOf(article.getViewCounts()));
            }
        }
    }

    @Autowired
    private StringRedisTemplate redisTemplate;  //加入redisTemplate操作redis

    //期望此增加浏览量操作在线程池 执行 不会影响原有的主线程
    @Async("taskExecutor") //加入线程池，把一些任务放入线程池中，异步进行
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        //方法一 把多线程任务加入线程池，操作数据库直接更新，异步进行 刷新显示
        int viewCounts = article.getViewCounts();

        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts +1);
        //添加更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下 线程安全 乐观锁-限制条件 防止被其他线程修改
        //增加时 发现现实article表中的getViewCounts等于从传参中取出的viewCounts，则增加操作执行，反之则失败

        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // update article set view_count=100 where view_count=99 and id=11
        // 阅读数更新后 不是实时显示，刷新即可
        articleMapper.update(articleUpdate,updateWrapper);

        ////把一些任务放入线程池中，异步进行 睡眠5秒
        //try {
        //    Thread.sleep(5000);
        //    System.out.println("更新完成了....");
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        ////采用redis进行浏览量的增加
        ////hash结构 key 浏览量标识 field 文章id  后面1 表示自增加1
        //redisTemplate.opsForHash().increment("view_count",String.valueOf(article.getId()),1);
        ////定时任务在ViewCountHandler中

        //还有一种方式是，redis自增之后，直接发送消息到消息队列中，由消息队列进行消费 来同步数据库，比定时任务要好一些
    }

    //期望此增加浏览量操作在线程池 执行 不会影响原有的主线程
    @Async("taskExecutor") //加入线程池，把一些任务放入线程池中，异步进行
    public void updateArticleViewCount2(ProblemMapper problemMapper, Article article) {

        //方法一 把多线程任务加入线程池，操作数据库直接更新，异步进行 刷新显示
        int viewCounts = article.getViewCounts();

        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts +1);
        //添加更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下 线程安全 乐观锁-限制条件 防止被其他线程修改
        //增加时 发现现实article表中的getViewCounts等于从传参中取出的viewCounts，则增加操作执行，反之则失败
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // update article set view_count=100 where view_count=99 and id=11
        // 阅读数更新后 不是实时显示，刷新即可
        problemMapper.update(articleUpdate,updateWrapper);

        ////把一些任务放入线程池中，异步进行 睡眠5秒
        //try {
        //    Thread.sleep(5000);
        //    System.out.println("更新完成了....");
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        ////采用redis进行浏览量的增加
        ////hash结构 key 浏览量标识 field 文章id  后面1 表示自增加1
        //redisTemplate.opsForHash().increment("view_count",String.valueOf(article.getId()),1);
        ////定时任务在ViewCountHandler中

        //还有一种方式是，redis自增之后，直接发送消息到消息队列中，由消息队列进行消费 来同步数据库，比定时任务要好一些
    }
}
