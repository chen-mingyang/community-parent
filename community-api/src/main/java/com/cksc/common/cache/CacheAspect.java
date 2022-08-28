package com.cksc.common.cache;

import com.alibaba.fastjson.JSON;
import com.cksc.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

//aop缓存工具类 定义一个切面，切面定义了切点和通知的关系
@Aspect
@Component
@Slf4j
public class CacheAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //AOP切点 注解加哪个地方，哪个地方就是切点
    @Pointcut("@annotation(com.cksc.common.cache.Cache)")
    public void pt(){}

    //AOP环绕通知 关联了切点pt()
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();

            //拿到参数类
            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            //参数判断
            String params = "";
            for(int i=0; i<args.length; i++) {
                if(args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                }else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况 充当Key
                params = DigestUtils.md5Hex(params);
            }

            //拿到对应的方法
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //使用反射，获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();
            //先从redis获取
            String redisKey = name + "::" + className+"::"+methodName+"::"+params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);

            if (StringUtils.isNotEmpty(redisValue)){
                log.info("走了缓存~~~,{}",redisKey);
                //走了缓存出现精度损失，点击查询文章详情失败
                Result result = JSON.parseObject(redisValue, Result.class);
                return result;
            }
            //为空则存入数据
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~ {},{}",className,methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }

}
