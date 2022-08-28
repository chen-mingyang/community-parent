package com.cksc.admin.common.aop;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cksc.admin.utils.HttpContextUtils;
import com.cksc.admin.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 开发AOP切面 让日志注解生效
 */

@Component
@Aspect //切面 定义了通知和切点的关系
@Slf4j //记录日志
public class LogAspect {

    //定义切点-用annotation标识
    @Pointcut("@annotation(com.cksc.admin.common.aop.LogAnnotation)")
    public void pt(){}

    //环绕通知 执行后抛出异常
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        //系统时间
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(joinPoint, time);
        return result;
    }


    //记录日志
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        //通过面向切面点joinPoint 得到执行方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //通过method 拿到Annotation日志
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        //设置日志参数
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module()); //模块名
        log.info("operation:{}",logAnnotation.operator()); //操作方法名

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className + "." + methodName + "()");

        //请求的参数 有无参数判断
        Object[] args = joinPoint.getArgs();
        String params;
        if (args.length==0){
            params="";
        }else{
            //加入：SerializerFeature.IgnoreNonFieldGetter
            params = JSON.toJSONString(args[0], SerializerFeature.IgnoreNonFieldGetter);
        }
        log.info("params:{}",params);

        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request)); //Remote Address: [::1]:8888

        //执行时间
        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");
    }
}
