//package com.cksc.admin.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    //BCrypt密码加密策略Sping自带
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    //手动生成密码
//    public static void main(String[] args) {
//        //加密策略 MD5 不安全 彩虹表  MD5 加盐
//        String mszlu = new BCryptPasswordEncoder().encode("mszlu");
//        System.out.println(mszlu);
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests() //开启登录认证
////                .antMatchers("/user/findAll").hasRole("admin") //访问接口需要admin的角色
//                //前端资源放行不拦截验证
//                .antMatchers("/css/**").permitAll()
//                .antMatchers("/img/**").permitAll()
//                .antMatchers("/js/**").permitAll()
//                .antMatchers("/plugins/**").permitAll()
//
//                // 在/admin/**路径上要拦截 进行认证 在authService中权限认证  注意这里Security优化 加角色验证
//                .antMatchers("/admin/**").access("@authService.auth(request,authentication)") //自定义service 来去实现实时的权限认证
//                // 认证登录成功就通过
//                .antMatchers("/pages/**").authenticated()
//
//                //登录信息认证自定义 响应登录请求  实际有token不这样子用
//                .and().formLogin()
//                .loginPage("/login.html") //自定义的登录页面
//                .loginProcessingUrl("/login") //登录处理接口
//
//                .usernameParameter("username") //定义登录时的用户名的key 默认为username
//                .passwordParameter("password") //定义登录时的密码key，默认是password
//                    .defaultSuccessUrl("/pages/main.html")
//                .failureUrl("/login.html")
//                .permitAll() //通过 不拦截，更加前面配的路径决定，这是指和登录表单相关的接口 都通过
//                .and().logout() //退出登录配置
//
//                .logoutUrl("/logout") //退出登录接口
//                .logoutSuccessUrl("/login.html")
//                .permitAll() //退出登录的接口放行
//
//                 //拦截单纯用postman进行HTTP请求
//                .and()
//                .httpBasic()
//                //拦截伪造请求
//                .and()
//                .csrf().disable() //csrf关闭 如果自定义登录 需要关闭
//                // 支持iframe页面嵌套
//                .headers().frameOptions().sameOrigin();
//    }
//}
