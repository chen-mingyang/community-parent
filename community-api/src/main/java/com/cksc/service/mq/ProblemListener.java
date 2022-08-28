package com.cksc.service.mq;

import com.alibaba.fastjson.JSON;
import com.cksc.service.ArticleService;
import com.cksc.service.ProblemService;
import com.cksc.vo.ArticleMessage;
import com.cksc.vo.ProblemMessage;
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
// 消息接收者 topic名称 consumerGroup消息组
@RocketMQMessageListener(topic = "blog-update-problem",consumerGroup = "blog-update-problem-group")
public class ProblemListener implements RocketMQListener<ProblemMessage> {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(ProblemMessage message) {
        log.info("收到的消息:{}",message);
        //做什么了，更新缓存

        //1. 更新查看问题详情的缓存
        Long problemId = message.getProblemId();
        //加密问题ID
        String params = DigestUtils.md5Hex(problemId.toString());
        //拼接得出redisKey
        String redisKey = "view_problem::ProblemController::findProblemById::"+params;
        //查询最新的问题详情信息
        Result problemResult = problemService.findProblemById(problemId);
        //根据key、问题详情更新缓存 设置时间Duration.ofMillis毫秒值
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(problemResult), Duration.ofMillis(2 * 60 * 1000));
        log.info("更新了缓存:{}",redisKey);

        //2. 问题列表的缓存 不知道参数,解决办法 直接删除缓存 *代表所有
        //此处可优化，定向地对问题列表缓存更新，不是直接删除
        Set<String> keys = redisTemplate.keys("listProblem*");
        //循环删除
        keys.forEach(s -> {
            redisTemplate.delete(s);
            log.info("删除了问题列表的缓存:{}",s);
        });

    }
}
