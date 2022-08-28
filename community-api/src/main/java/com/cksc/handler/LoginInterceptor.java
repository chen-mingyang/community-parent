package com.cksc.handler;

import com.alibaba.fastjson.JSON;

import com.cksc.dao.pojo.SysUser;
import com.cksc.service.LoginService;
import com.cksc.utils.UserThreadLocal;
import com.cksc.vo.ErrorCode;
import com.cksc.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一登录拦截验证 过滤器
 */

@Component //让spring识别到
@Slf4j //日志
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行controller方法(Handler)之前进行执行
        /**
         * 1. 需要判断 请求的接口路径 是否为 HandlerMethod (controller方法)
         * 2. 判断 token是否为空，如果为空 未登录
         * 3. 如果token 不为空，登录验证 loginService checkToken
         * 4. 如果认证成功 放行即可
         */
        if (!(handler instanceof HandlerMethod)){
            //handler 可能是 RequestResourceHandler springboot程序 访问静态资源 默认去classpath下的static目录去查询
            return true;
        }
        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");


        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8"); //返回json
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token); //检查token
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登录验证成功，放行
        //我希望在controller中 直接获取用户的信息 怎么获取? 放入本地线程ThreadLocal
        UserThreadLocal.put(sysUser);
        return true;
    }

    //调用方法完后，执行这个方法 这个项目执行次数多了 ThreadLocal中信息没有删除干净 造成内存泄漏
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除 ThreadLocal中用完的信息 会有内存泄漏的风险
        //ThreadLocal内存泄漏原理，可以百度，JAVA进阶
        System.out.println("--------------------------->删除 ThreadLocal中用完的信息");
        UserThreadLocal.remove();
    }
}
