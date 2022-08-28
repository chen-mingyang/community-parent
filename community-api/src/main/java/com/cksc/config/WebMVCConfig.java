package com.cksc.config;

import com.cksc.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/7 0:58
 * @description WebMVC配置类
 *
 * 加入spring security 可以替换这个类
 */

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor; //登录验证拦截器

    //Spring Boot WebMvcConfigurer配置 @CrossOrigin跨域(CORS)支持
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置-配置允许哪些域名访问后端API
        registry.addMapping("/**").allowedOrigins("https://admin.cmyshare.cn","https://www.cmyshare.cn","https://cmyshare.cn","http://localhost:8080");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口，后续实际遇到需要拦截的接口时，在配置为真正的拦截接口
        //方法二 拦截所有请求 除了/login /register
        //.addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register")

        //方法一 加入要拦截的
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change") //评论拦截
                .addPathPatterns("/articles/publish") //发布文章拦截
                .addPathPatterns("/problems/publish"); //发布问题拦截
    }
}
