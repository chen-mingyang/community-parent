package com.cksc.service.mq;

import com.alibaba.fastjson.JSON;

import com.cksc.service.ArticleService;
import com.cksc.vo.ArticleMessage;
import com.cksc.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;


//日志注解
@Slf4j
//扫描组件
@Component
// 消息组接收消息注解 topic名称 consumerGroup消息组
@RocketMQMessageListener(topic = "blog-update-article",consumerGroup = "blog-update-article-group")
public class ArticleListener implements RocketMQListener<ArticleMessage> {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(ArticleMessage message) {
        log.info("收到的消息:{}",message);
        //做什么了，更新缓存

        //1. 更新查看文章详情的缓存
        Long articleId = message.getArticleId();
        //加密文章ID
        String params = DigestUtils.md5Hex(articleId.toString());
        //拼接得出redisKey
        String redisKey = "view_article::ArticleController::findArticleById::"+params;
        //查询最新的文章详情信息
        Result articleResult = articleService.findArticleById(articleId);
        //根据key、文章详情更新缓存 设置时间Duration.ofMillis毫秒值
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(articleResult), Duration.ofMillis(2 * 60 * 1000));
        log.info("更新了缓存:{}",redisKey);

        //2. 文章列表的缓存 不知道参数,解决办法 直接删除缓存 *代表所有
        //此处可优化，定向地对文章列表缓存更新，不是直接删除
        Set<String> keys = redisTemplate.keys("listArticle*");
        //循环删除
        keys.forEach(s -> {
            redisTemplate.delete(s);
            log.info("删除了文章列表的缓存:{}",s);
        });

    }
}
