package com.cksc.admin.config;


import com.cksc.admin.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/21 20:05
 * @description Spring Security配置类
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_LIST = {
            "/**/v2/api-docs",
            "/**/configuration/ui",
            "/**/swagger-resources/**",
            "/**/configuration/security",
            "/**/swagger-ui.html",
            "/**/webjars/**",
    };

    //BCrypt密码加密策略Sping自带
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    ////手动生成密码
    //public static void main(String[] args) {
    //    //加密策略 MD5 不安全 彩虹表  MD5 加盐
    //    String mszlu = new BCryptPasswordEncoder().encode("mszlu");
    //    System.out.println(mszlu);
    //}

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    // Spring Security接管Swagger认证授权 不影响后台管理的工程
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER");
    }


    /**
     * 第一步：将JWT过滤器添加到默认的账号密码过滤器之前，表示token验证成功后无需登录
     * 第二步：配置异常处理器和登出处理器
     * 第三步：开启权限拦截，对所有请求进行拦截
     * 第四步：开放不需要拦截的请求，比如用户注册、OPTIONS请求和静态资源等
     * 第五步：允许OPTIONS请求，为跨域配置做准备
     * 第六步：允许访问静态资源，访问swagger时需要
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //先暂时关闭跨站请求伪造，它限制除了get以外的大多数方法。
                .cors()  //允许跨域访问
                .and()

                .authorizeRequests() //开启用户权限拦截
                .antMatchers("/").authenticated() //配置那些url需要进行校验--所有请求都需要校验"/"
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //允许所有OPTIONS请求

                .antMatchers("/public/**").permitAll() //那些请求不需要校验

                .antMatchers(AUTH_LIST).permitAll() //接口文档不需要校验

                .anyRequest().authenticated() //自定义校验类
                .and()

                .httpBasic() //拦截单纯用postman进行HTTP请求
                .and()

                .addFilterBefore(new JwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)  //***添加jwt过滤器
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //关闭session
        ;
    }

    //使用CORS跨域请求全局配置，Spring Security提供了新的CORS规则配置方法CorsConfigurationSource。
    //使用这种方法实现的效果等同于注入一个CorsFilter过滤器，实现全局配置的跨域资源共享。可以去除Controller中跨域注解
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        //CORS认证内容
        CorsConfiguration configuration = new CorsConfiguration();
        //跨域配置-配置允许哪些域名访问后端API 不用在后端加请求头
        configuration.setAllowedOrigins(Arrays.asList("https://admin.cmyshare.cn","https://www.cmyshare.cn","https://cmyshare.cn","http://localhost:8081"));
        configuration.addAllowedHeader("*"); //默认允许所有请求头
        configuration.addAllowedMethod("*"); //需要指明允许所有方法
        //定义Spring Security CORS 加入相关配置内容
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
